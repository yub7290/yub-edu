package com.yub.edu.biz.entity;

import lombok.Data;

/**
 * 试题选项实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试题选项（单选/多选/填空用）
 * @Version: 1.0.0
 */
@Data
public class EduQuestionOption {

    /** 主键 */
    private Long id;

    /** 试题ID */
    private Long questionId;

    /** 选项标签（A/B/C/D） */
    private String label;

    /** 选项内容（富文本） */
    private String content;

    /** 是否为正确答案 1:是 0:否 */
    private Integer isCorrect;

    /** 排序 */
    private Integer sort;
}
