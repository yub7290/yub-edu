package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试答题明细实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生考试答题明细
 * @Version: 1.0.0
 */
@Data
public class EduExamRecordDetail {

    /** 主键 */
    private Long id;

    /** 考试记录ID */
    private Long recordId;

    /** 试卷ID */
    private Long examId;

    /** 试题ID */
    private Long questionId;

    /** 用户答案 */
    private String userAnswer;

    /** 正确答案 */
    private String correctAnswer;

    /** 是否正确 1:是 0:否 */
    private Integer isCorrect;

    /** 本题得分 */
    private Integer score;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
