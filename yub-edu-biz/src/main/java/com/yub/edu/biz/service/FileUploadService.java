package com.yub.edu.biz.service;

import com.yub.common.exception.BaseException;
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

    /**
     * 上传文件到七牛云，不回退到本地存储
     *
     * @param file      文件
     * @param directory 存储目录
     * @return 文件访问 URL
     */
    String uploadToQiniu(MultipartFile file, String directory);

    /**
     * 校验上传图片（大小 ≤5MB，仅允许 JPG/JPEG/PNG/GIF）
     *
     * @param file 文件
     * @throws BaseException 校验不通过时抛出对应错误码
     */
    void validateImage(MultipartFile file);

    /**
     * 校验上传视频（大小 ≤500MB，仅允许 MP4/AVI/MOV/FLV/WMV/MKV）
     *
     * @param file 文件
     * @throws BaseException 校验不通过时抛出对应错误码
     */
    void validateVideo(MultipartFile file);

    /**
     * 校验上传附件（大小 ≤100MB，允许常见文档和媒体格式）
     *
     * @param file 文件
     * @throws BaseException 校验不通过时抛出对应错误码
     */
    void validateFile(MultipartFile file);
}
