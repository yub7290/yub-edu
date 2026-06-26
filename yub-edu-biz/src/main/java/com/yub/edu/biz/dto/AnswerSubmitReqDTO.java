package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 提交答案请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 提交试题答案请求参数
 * @Version: 1.0.0
 */
@Data
public class AnswerSubmitReqDTO {

    /** 课程ID */
    private Long courseId;

    /** 章节ID */
    private Long chapterId;

    /** 题目ID */
    private Long questionId;

    /** 学生答案 */
    private String userAnswer;

    /** 是否正确 0错误 1正确 */
    private Integer isCorrect;

    /** 答题用时(秒) */
    private Integer answerDuration;

    /** 练习模式 1章节 2错题 3收藏 4高频 5继续 */
    private Integer practiceMode;

    /** 重做来源记录ID */
    private Long sourceRecordId;
}
