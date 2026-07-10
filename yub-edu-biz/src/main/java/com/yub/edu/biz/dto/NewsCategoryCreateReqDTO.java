package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增新闻分类请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新增新闻分类请求参数
 * @Version: 1.0.0
 */
@Data
public class NewsCategoryCreateReqDTO {

    /** 分类名称 */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50个字符")
    private String name;

    /** 排序（从小到大） */
    private Integer sortOrder = 0;

    /** 状态 1:启用 0:禁用 */
    private Integer status = 1;
}
