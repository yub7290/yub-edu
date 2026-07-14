package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EduKnowledgeRelation {

    private Long id;

    private Long sourceId;

    private Long targetId;

    private Integer relationType;

    private String description;

    private Integer sort;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createBy;

    private Long updateBy;

    private Integer deleted;
}