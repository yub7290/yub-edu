package com.yub.edu.biz.controller;

import com.qiniu.util.Auth;
import com.yub.common.model.Response;
import com.yub.edu.biz.config.QiniuConfig;
import com.yub.edu.biz.service.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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

    /** 合法目录名正则：只允许字母数字、下划线、中划线、斜杠，禁止路径穿越 */
    private static final Pattern DIR_PATTERN = Pattern.compile("^[\\w\\-/]+$");

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
            @RequestParam(value = "directory", defaultValue = "edu/videos") String directory,
            @RequestParam(value = "ext", defaultValue = "mp4") String ext) {
        if (!DIR_PATTERN.matcher(directory).matches()) {
            return Response.error(400, "存储目录格式不合法");
        }
        String key = directory + "/" + UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        Map<String, String> result = new HashMap<>(5);
        result.put("token", upToken);
        result.put("key", key);
        result.put("domain", qiniuConfig.getDomain());
        result.put("uploadUrl", getQiniuUploadUrl(qiniuConfig.getRegion()));
        return Response.success(result);
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
