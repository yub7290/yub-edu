package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 考试题目结果 VO（学生端，含用户答案和正确答案）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生端考试每题作答结果
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionResultRespVO {

    /** 题目ID */
    private Long questionId;

    /** 试题类型 0:单选 1:多选 2:判断 3:填空 4:简答 */
    private Integer questionType;

    /** 题干 */
    private String content;

    /** 选项列表（单选/多选） */
    private List<ExamQuestionOptionRespVO> options;

    /** 用户答案 */
    private String userAnswer;

    /** 正确答案 */
    private String correctAnswer;

    /** 是否正确 */
    private Boolean isCorrect;

    /** 本题得分 */
    private Integer score;

    /** 知识点 */
    private String knowledgePoint;

    /** 解析 */
    private String analysis;
}
