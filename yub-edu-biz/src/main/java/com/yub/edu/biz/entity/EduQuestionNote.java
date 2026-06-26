package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试题笔记实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 试题笔记实体
 * @Version: 1.0.0
 */
@Data
public class EduQuestionNote {

    /** 主键 */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 试题ID */
    private Long questionId;

    /** 课程ID */
    private Long courseId;

    /** 笔记内容 */
    private String noteContent;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
