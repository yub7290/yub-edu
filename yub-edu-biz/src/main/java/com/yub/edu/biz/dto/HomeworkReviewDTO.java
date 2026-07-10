package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 作业复审请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业题目复审请求参数
 * @Version: 1.0.0
 */
@Data
public class HomeworkReviewDTO {

    /** 题目ID */
    @NotNull(message = "题目ID不能为空")
    private Long questionId;

    /** 复审结果 1:正确 0:错误 */
    @NotNull(message = "复审结果不能为空")
    private Integer isCorrect;

    /** 修正后的学员答案（AI识别错误时人工修正） */
    private String studentAnswer;

    /** 复审答案 */
    private String reviewAnswer;

    /** 复审解析 */
    private String reviewAnalysis;
}
