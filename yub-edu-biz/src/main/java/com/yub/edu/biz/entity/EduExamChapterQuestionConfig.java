package com.yub.edu.biz.entity;

import lombok.Data;

/**
 * 试卷章节出题配置实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 按章节出题时，每章节各题型抽取数量和分值
 * @Version: 1.0.0
 */
@Data
public class EduExamChapterQuestionConfig {

    /** 主键 */
    private Long id;

    /** 试卷ID */
    private Long examId;

    /** 章节ID */
    private Long chapterId;

    /** 试题类型 0:单选 1:多选 2:判断 3:简答 4:填空 */
    private Integer questionType;

    /** 抽取题数 */
    private Integer questionCount;

    /** 每题分值 */
    private Integer scorePerQuestion;
}
