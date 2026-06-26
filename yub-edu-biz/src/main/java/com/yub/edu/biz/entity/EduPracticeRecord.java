package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 练习记录实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 练习记录实体
 * @Version: 1.0.0
 */
@Data
public class EduPracticeRecord {

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

    /** 用户答案 */
    private String userAnswer;

    /** 是否正确 0:错误 1:正确 */
    private Integer isCorrect;

    /** 答题耗时（秒） */
    private Integer answerDuration;

    /** 练习模式 0:章节练习 1:随机练习 2:错题重练 */
    private Integer practiceMode;

    /** 源练习记录ID（错题重练时关联原记录） */
    private Long sourceRecordId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
