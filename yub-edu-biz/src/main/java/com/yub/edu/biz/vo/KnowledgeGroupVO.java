package com.yub.edu.biz.vo;

import lombok.Data;

import java.util.List;

/**
 * 知识点分组 VO（按章节分组）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 章节知识点分组
 * @Version: 1.0.0
 */
@Data
public class KnowledgeGroupVO {

    /** 章节ID */
    private Long chapterId;

    /** 章节名称 */
    private String chapterName;

    /** 知识点列表 */
    private List<KnowledgeItemVO> knowledgeList;
}
