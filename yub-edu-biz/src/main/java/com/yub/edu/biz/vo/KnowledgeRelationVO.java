package com.yub.edu.biz.vo;

import lombok.Data;

@Data
public class KnowledgeRelationVO {

    private Long id;

    private Long sourceId;

    private String sourceTitle;

    private Long targetId;

    private String targetTitle;

    private Integer relationType;

    private String relationTypeName;

    private String description;

    private Integer sort;
}