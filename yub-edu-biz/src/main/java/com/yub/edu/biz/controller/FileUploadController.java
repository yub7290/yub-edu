package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

/**
 * 文件上传 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 文件上传接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

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
        // 路径穿越防护：限制目录名只能包含字母、数字、下划线、中划线、斜杠
        if (!DIR_PATTERN.matcher(directory).matches()) {
            return Response.error(400, "存储目录格式不合法");
        }
        String url = fileUploadService.upload(file, directory);
        return Response.success(url);
    }
}
