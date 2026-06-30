package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增知识点请求
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 新增知识点请求参数
 * @Version: 1.0.0
 */
@Data
public class KnowledgePointCreateReqDTO {

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    private Long courseId;

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;

    private String content;

    @NotNull(message = "状态不能为空")
    private Integer status = 1;
}
