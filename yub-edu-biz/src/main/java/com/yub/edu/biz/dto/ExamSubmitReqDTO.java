package com.yub.edu.biz.dto;

import lombok.Data;

import java.util.List;

/**
 * 提交考试请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生提交考试请求参数
 * @Version: 1.0.0
 */
@Data
public class ExamSubmitReqDTO {

    /** 试卷ID */
    private Long examId;

    /** 答题用时（秒） */
    private Integer duration;

    /** 答题列表 */
    private List<AnswerItem> answers;

    /**
     * 单个答题项
     */
    @Data
    public static class AnswerItem {

        /** 题目ID */
        private Long questionId;

        /** 用户答案 */
        private String userAnswer;
    }
}
