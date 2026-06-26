package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 考试题目 VO（学生端，不含正确答案）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生端考试题目详情
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionRespVO {

    /** 题目ID */
    private Long questionId;

    /** 试题类型 0:单选 1:多选 2:判断 3:填空 4:简答 */
    private Integer questionType;

    /** 题干 */
    private String content;

    /** 本题分值 */
    private Integer score;

    /** 排序 */
    private Integer sort;

    /** 选项列表（单选/多选） */
    private List<ExamQuestionOptionRespVO> options;
}
