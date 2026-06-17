package com.yub.edu.biz.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 文件上传服务
 * @Version: 1.0.0
 */
public interface FileUploadService {

    /**
     * 上传文件到七牛云
     *
     * @param file 文件
     * @param directory 存储目录（如 major/images）
     * @return 文件访问 URL
     */
    String upload(MultipartFile file, String directory);
}
