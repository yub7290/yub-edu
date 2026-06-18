package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI助教会话实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: AI助教会话实体
 * @Version: 1.0.0
 */
@Data
public class EduAiConversation {

    /** 会话ID */
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 课程ID */
    private Long courseId;

    /** 会话标题 */
    private String title;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
