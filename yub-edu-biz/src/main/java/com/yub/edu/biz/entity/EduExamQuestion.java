package com.yub.edu.biz.entity;

import lombok.Data;

/**
 * 试卷题目关联实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试卷题目关联
 * @Version: 1.0.0
 */
@Data
public class EduExamQuestion {

    /** 主键 */
    private Long id;

    /** 试卷ID */
    private Long examId;

    /** 试题ID */
    private Long questionId;

    /** 排序 */
    private Integer sort;

    /** 本题分值 */
    private Integer score;
}
