package com.yub.edu.biz.controller;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.config.QiniuConfig;
import com.yub.edu.biz.dto.VideoTranscodeStartReqDTO;
import com.yub.edu.biz.service.FileUploadService;
import com.yub.edu.biz.service.VideoTranscodeService;
import com.yub.edu.biz.vo.VideoTranscodeRespVO;
import com.yub.framework.security.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 文件上传 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 文件上传接口
 * @Version: 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/edu/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final QiniuConfig qiniuConfig;
    private final VideoTranscodeService videoTranscodeService;

    /** 合法目录名正则：只允许字母数字、下划线、中划线、斜杠，禁止路径穿越 */
    private static final Pattern DIR_PATTERN = Pattern.compile("^[\\w\\-/]+$");
    private static final Set<String> HLS_SOURCE_EXTENSIONS = Set.of("mp4", "mov");
    private static final long MAX_VIDEO_SIZE = 500L * 1024 * 1024;

    /**
     * 上传图片
     *
     * @param file      文件
     * @param directory 存储目录
     * @return 文件访问 URL
     */
    @PostMapping("/image")
    public Response<String> uploadImage(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "directory", defaultValue = "edu/images") String directory) {
        if (file.isEmpty()) {
            return Response.error(400, "请选择文件");
        }
        // 校验图片格式与大小
        fileUploadService.validateImage(file);
        // 路径穿越防护：限制目录名只能包含字母、数字、下划线、中划线、斜杠
        if (!DIR_PATTERN.matcher(directory).matches()) {
            return Response.error(400, "存储目录格式不合法");
        }
        String url = fileUploadService.upload(file, directory);
        return Response.success(url);
    }

    /**
     * 上传视频
     *
     * @param file      文件
     * @param directory 存储目录
     * @return 文件访问 URL
     */
    @PostMapping("/video")
    public Response<String> uploadVideo(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "directory", defaultValue = "edu/videos") String directory) {
        if (file.isEmpty()) {
            return Response.error(400, "请选择文件");
        }
        // 校验视频格式与大小
        fileUploadService.validateVideo(file);
        // 路径穿越防护
        if (!DIR_PATTERN.matcher(directory).matches()) {
            return Response.error(400, "存储目录格式不合法");
        }
        String url = fileUploadService.uploadToQiniu(file, directory);
        return Response.success(url);
    }

    /**
     * 上传附件（通用文件）
     *
     * @param file      文件
     * @param directory 存储目录
     * @return 文件访问 URL
     */
    @PostMapping("/file")
    public Response<String> uploadFile(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "directory", defaultValue = "edu/chapter/attachments") String directory) {
        if (file.isEmpty()) {
            return Response.error(400, "请选择文件");
        }
        fileUploadService.validateFile(file);
        if (!DIR_PATTERN.matcher(directory).matches()) {
            return Response.error(400, "存储目录格式不合法");
        }
        String url = fileUploadService.upload(file, directory);
        return Response.success(url);
    }

    /**
     * 代理下载文件，保留原始文件名
     *
     * @param fileUrl  文件存储URL
     * @param fileName 原始文件名
     * @param response HTTP响应
     */
    @GetMapping("/download")
    public void download(@RequestParam String fileUrl,
                         @RequestParam String fileName,
                         HttpServletResponse response) {
        java.net.URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(fileUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(60000);
            conn.connect();

            if (conn.getResponseCode() != 200) {
                response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "文件获取失败");
                return;
            }

            // 设置下载响应头，使用URLEncode处理中文文件名
            String encodedName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
            response.setContentType(conn.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedName + "\"; filename*=UTF-8''" + encodedName);
            response.setContentLengthLong(conn.getContentLengthLong());

            try (InputStream in = conn.getInputStream();
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            log.error("文件下载失败: url={}, fileName={}", fileUrl, fileName, e);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    /**
     * 获取七牛云上传凭证（浏览器直传七牛云，跳过后端中转）
     *
     * @param directory 存储目录
     * @param ext       文件扩展名
     * @return 上传凭证、存储 key、CDN 域名
     */
    @GetMapping("/qiniu-token")
    public Response<Map<String, String>> getQiniuUploadToken(
            @RequestParam(value = "ext", defaultValue = "mp4") String ext) {
        String normalizedExt = ext.toLowerCase().replace(".", "");
        if (!HLS_SOURCE_EXTENSIONS.contains(normalizedExt)) {
            return Response.error(1006, "视频仅支持 MP4、MOV 格式");
        }
        if (!StringUtils.hasText(qiniuConfig.getAccessKey())
                || !StringUtils.hasText(qiniuConfig.getSecretKey())
                || !StringUtils.hasText(qiniuConfig.getBucket())
                || !StringUtils.hasText(qiniuConfig.getDomain())
                || !StringUtils.hasText(qiniuConfig.getVideoSourcePrefix())) {
            return Response.error(503, "七牛云配置不完整");
        }
        String prefix = qiniuConfig.getVideoSourcePrefix().replaceAll("^/+|/+$", "");
        String key = prefix + "/" + UUID.randomUUID().toString().replace("-", "") + "." + normalizedExt;
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        StringMap policy = new StringMap()
                .put("insertOnly", 1)
                .put("fsizeLimit", MAX_VIDEO_SIZE)
                .put("mimeLimit", "video/mp4;video/quicktime")
                .put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"fsize\":$(fsize)}");
        String upToken = auth.uploadToken(qiniuConfig.getBucket(), key, 3600, policy);
        Map<String, String> result = new HashMap<>(6);
        result.put("token", upToken);
        result.put("key", key);
        result.put("domain", qiniuConfig.getDomain());
        result.put("uploadUrl", getQiniuUploadUrl(qiniuConfig.getRegion()));
        result.put("maxSize", String.valueOf(MAX_VIDEO_SIZE));
        return Response.success(result);
    }

    /** 上传完成后提交七牛多码率 HLS 异步转码。 */
    @PostMapping("/video/transcode")
    public Response<VideoTranscodeRespVO> startVideoTranscode(
            @Valid @RequestBody VideoTranscodeStartReqDTO request) {
        return Response.success(videoTranscodeService.start(request, SecurityUtils.getCurrentUserId()));
    }

    /** 查询七牛 pfop 状态；只有 READY 状态的视频才允许保存到章节。 */
    @GetMapping("/video/transcode/{assetId}")
    public Response<VideoTranscodeRespVO> getVideoTranscodeStatus(@PathVariable Long assetId) {
        return Response.success(videoTranscodeService.refresh(assetId, SecurityUtils.getCurrentUserId()));
    }

    /** 删除当前用户上传且尚未关联章节的视频资产。 */
    @Log(value = "删除视频资产", type = "DELETE")
    @DeleteMapping("/video/asset/{assetId}")
    public Response<Void> deleteVideoAsset(@PathVariable Long assetId) {
        videoTranscodeService.delete(assetId, SecurityUtils.getCurrentUserId());
        return Response.success();
    }

    /**
     * 根据七牛云区域返回对应的上传地址
     *
     * @param region 区域代码（z0/z1/z2/na0/as0）
     * @return 上传地址
     */
    private String getQiniuUploadUrl(String region) {
        return switch (region) {
            case "z1" -> "https://upload-z1.qiniup.com";
            case "z2" -> "https://upload-z2.qiniup.com";
            case "na0" -> "https://upload-na0.qiniup.com";
            case "as0" -> "https://upload-as0.qiniup.com";
            default -> "https://upload.qiniup.com";
        };
    }
}
