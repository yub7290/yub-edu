package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/** 七牛视频源文件及异步 HLS 转码任务。 */
@Data
public class EduVideoAsset {
    private Long id;
    private String originalName;
    private Long fileSize;
    private String sourceKey;
    private String sourceUrl;
    private String hlsKey;
    private String hlsUrl;
    private String persistentId;
    private String status;
    private String errorMessage;
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
