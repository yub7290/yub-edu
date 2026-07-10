package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节附件实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 章节附件实体
 * @Version: 1.0.0
 */
@Data
public class EduChapterAttachment {

    /** 主键 */
    private Long id;

    /** 章节ID */
    private Long chapterId;

    /** 原始文件名 */
    private String fileName;

    /** 文件存储URL */
    private String fileUrl;

    /** 文件大小（字节） */
    private Long fileSize;

    /** 文件扩展名 */
    private String fileType;

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
