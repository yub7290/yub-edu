package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试题关联知识点实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 试题与知识点关联关系
 * @Version: 1.0.0
 */
@Data
public class EduQuestionKnowledgePoint {

    /** 主键 */
    private Long id;

    /** 试题ID */
    private Long questionId;

    /** 知识点ID */
    private Long knowledgePointId;

    /** 排序 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 创建人 */
    private Long createBy;
}
