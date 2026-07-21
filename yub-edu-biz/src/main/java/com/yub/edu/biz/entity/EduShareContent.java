package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分享内容实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-20
 * @Description: 分享内容管理
 * @Version: 1.0.0
 */
@Data
public class EduShareContent {

    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private String content;

    private Integer status;

    private Integer sort;

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;

    private Integer deleted;
}