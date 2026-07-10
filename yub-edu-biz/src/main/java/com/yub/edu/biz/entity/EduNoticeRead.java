package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知已读记录实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 通知已读记录实体
 * @Version: 1.0.0
 */
@Data
public class EduNoticeRead {

    /** 主键 */
    private Long id;

    /** 通知ID */
    private Long noticeId;

    /** 学员ID */
    private Long studentId;

    /** 阅读时间 */
    private LocalDateTime readTime;
}
