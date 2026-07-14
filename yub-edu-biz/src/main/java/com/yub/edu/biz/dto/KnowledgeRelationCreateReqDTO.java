package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KnowledgeRelationCreateReqDTO {

    @NotNull(message = "源知识点ID不能为空")
    private Long sourceId;

    @NotNull(message = "目标知识点ID不能为空")
    private Long targetId;

    private Integer relationType = 1;

    private String description;

    private Integer sort = 0;
}