package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KnowledgeRelationUpdateReqDTO {

    @NotNull(message = "ID不能为空")
    private Long id;

    private Long sourceId;

    private Long targetId;

    private Integer relationType;

    private String description;

    private Integer sort;

    private Integer status;
}