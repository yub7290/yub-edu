package com.yub.edu.biz.vo;

import lombok.Data;

/**
 * 知识点详情 VO（学员端）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 知识点详情
 * @Version: 1.0.0
 */
@Data
public class KnowledgeDetailRespVO {

    /** 知识点ID */
    private Long id;

    /** 标题 */
    private String title;

    /** 内容（富文本） */
    private String content;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;
}
