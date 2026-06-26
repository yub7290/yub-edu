package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 练习会话实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 练习会话实体
 * @Version: 1.0.0
 */
@Data
public class EduPracticeSession {

    /** 主键 */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 课程ID */
    private Long courseId;

    /** 章节ID */
    private Long chapterId;

    /** 试题ID */
    private Long questionId;

    /** 练习模式 0:章节练习 1:随机练习 2:错题重练 */
    private Integer practiceMode;

    /** 会话状态 0:进行中 1:已完成 */
    private Integer status;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
