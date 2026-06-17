package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        String url = fileUploadService.upload(file, directory);
        return Response.success(url);
    }
}
