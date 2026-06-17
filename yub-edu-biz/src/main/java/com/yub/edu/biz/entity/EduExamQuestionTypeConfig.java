package com.yub.edu.biz.entity;

import lombok.Data;

/**
 * 试卷试题类型配置实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 记录每种题型抽取的数量和分值
 * @Version: 1.0.0
 */
@Data
public class EduExamQuestionTypeConfig {

    /** 主键 */
    private Long id;

    /** 试卷ID */
    private Long examId;

    /** 试题类型 0:单选 1:多选 2:判断 3:简答 4:填空 */
    private Integer questionType;

    /** 抽取题数 */
    private Integer questionCount;

    /** 每题分值 */
    private Integer scorePerQuestion;
}
