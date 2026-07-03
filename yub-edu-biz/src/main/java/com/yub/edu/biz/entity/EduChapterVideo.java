package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节视频实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 章节视频实体
 * @Version: 1.0.0
 */
@Data
public class EduChapterVideo {

    /** 主键 */
    private Long id;

    /** 章节ID */
    private Long chapterId;

    /** 视频名称 */
    private String videoName;

    /** 视频URL */
    private String videoUrl;

    /** 文件大小（字节） */
    private Long fileSize;

    /** 排序 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人 */
    private Long createBy;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
