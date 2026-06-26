package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考试题目选项 VO（学生端，不含正确答案）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生端题目选项
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionOptionRespVO {

    /** 选项标签（A/B/C/D） */
    private String label;

    /** 选项内容 */
    private String content;

    /** 排序 */
    private Integer sort;
}
