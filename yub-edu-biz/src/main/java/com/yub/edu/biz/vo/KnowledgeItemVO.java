package com.yub.edu.biz.vo;

import lombok.Data;

/**
 * 知识点项 VO（学员端）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 知识点简要信息
 * @Version: 1.0.0
 */
@Data
public class KnowledgeItemVO {

    /** 知识点ID */
    private Long id;

    /** 标题 */
    private String title;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;
}
