package com.yub.edu.biz.service.impl;

import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.yub.common.enums.ResponseCode;
import com.yub.common.exception.BaseException;
import com.yub.edu.biz.config.QiniuConfig;
import com.yub.edu.biz.service.FileUploadService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

/**
 * 文件上传服务实现（七牛云 + 本地回退）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 文件上传服务实现，七牛云配置有效时使用七牛云，否则本地存储
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final QiniuConfig qiniuConfig;

    @Value("${local.upload-path:${user.dir}/uploads}")
    private String localUploadPath;

    @PostConstruct
    public void init() {
        if (isQiniuConfigured()) {
            log.info("七牛云已配置，使用七牛云存储");
        } else {
            log.warn("七牛云未配置，将使用本地存储: {}", localUploadPath);
        }
    }

    @Override
    public String upload(MultipartFile file, String directory) {
        if (isQiniuConfigured()) {
            try {
                return uploadToQiniu(file, directory);
            } catch (Exception e) {
                log.warn("七牛云上传失败，回退到本地存储: {}", e.getMessage());
                return uploadToLocal(file, directory);
            }
        }
        return uploadToLocal(file, directory);
    }

    /**
     * 上传到七牛云 — 直接使用配置的 domain 拼接 URL
     */
    private String uploadToQiniu(MultipartFile file, String directory) throws IOException {
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());

        String fileName = generateFileName(file, directory);

        // 使用 InputStream 流式上传，避免大文件 OOM
        Response response = uploadManager.put(
                file.getInputStream(),
                fileName,
                upToken,
                (StringMap) null,   // 无自定义参数
                file.getContentType()  // 保持原始 MIME 类型
        );
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

        String fileUrl = qiniuConfig.getDomain() + "/" + putRet.key;
        log.info("七牛云上传成功: {}", fileUrl);
        return fileUrl;
    }

    /**
     * 上传到本地
     */
    private String uploadToLocal(MultipartFile file, String directory) {
        String fileName = generateFileName(file, directory);
        try {
            Path targetDir = Paths.get(localUploadPath, directory);
            Files.createDirectories(targetDir);
            Path targetFile = targetDir.resolve(Paths.get(fileName).getFileName());
            file.transferTo(targetFile.toFile());
            String fileUrl = "/uploads/" + directory + "/" + Paths.get(fileName).getFileName();
            log.info("本地上传成功: {}", fileUrl);
            return fileUrl;
        } catch (IOException e) {
            log.error("本地文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 生成唯一文件名
     */
    private String generateFileName(MultipartFile file, String directory) {
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return directory + "/" + UUID.randomUUID().toString().replace("-", "") + suffix;
    }

    /** 允许的图片 MIME 类型 */
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif"
    );

    /** 允许的图片文件扩展名（小写） */
    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif"
    );

    /** 最大图片字节数 */
    @Value("${upload.max-image-size:5242880}")
    private long maxImageSize;

    /** 允许的视频 MIME 类型 */
    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
            "video/mp4", "video/x-msvideo", "video/quicktime",
            "video/x-flv", "video/x-ms-wmv", "video/x-matroska"
    );

    /** 允许的视频文件扩展名（小写） */
    private static final Set<String> ALLOWED_VIDEO_EXTENSIONS = Set.of(
            "mp4", "avi", "mov", "flv", "wmv", "mkv"
    );

    /** 最大视频字节数 */
    @Value("${upload.max-video-size:524288000}")
    private long maxVideoSize;

    /**
     * 检查七牛云是否已配置（密钥不为占位值即视为已配置）
     */
    private boolean isQiniuConfigured() {
        return qiniuConfig.getAccessKey() != null
                && !"your-access-key".equals(qiniuConfig.getAccessKey())
                && qiniuConfig.getSecretKey() != null
                && !"your-secret-key".equals(qiniuConfig.getSecretKey())
                && qiniuConfig.getBucket() != null
                && !qiniuConfig.getBucket().isEmpty();
    }

    @Override
    public void validateImage(MultipartFile file) {
        // 校验文件大小
        if (file.getSize() > maxImageSize) {
            throw new BaseException(ResponseCode.FILE_SIZE_EXCEEDED);
        }
        // 校验文件格式：先检查 MIME 类型，再检查扩展名
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        boolean validType = false;
        if (contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            validType = true;
        }
        if (!validType && originalFilename != null && originalFilename.contains(".")) {
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            validType = ALLOWED_IMAGE_EXTENSIONS.contains(ext);
        }
        if (!validType) {
            throw new BaseException(ResponseCode.FILE_TYPE_NOT_ALLOWED);
        }
    }

    @Override
    public void validateVideo(MultipartFile file) {
        // 校验文件大小
        if (file.getSize() > maxVideoSize) {
            throw new BaseException(ResponseCode.FILE_SIZE_EXCEEDED);
        }
        // 校验文件格式：先检查 MIME 类型，再检查扩展名
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        boolean validType = false;
        if (contentType != null && ALLOWED_VIDEO_TYPES.contains(contentType.toLowerCase())) {
            validType = true;
        }
        if (!validType && originalFilename != null && originalFilename.contains(".")) {
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            validType = ALLOWED_VIDEO_EXTENSIONS.contains(ext);
        }
        if (!validType) {
            throw new BaseException(ResponseCode.FILE_TYPE_NOT_ALLOWED);
        }
    }
}
