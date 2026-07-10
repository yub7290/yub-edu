package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 编辑学员组请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 编辑学员组请求参数
 * @Version: 1.0.0
 */
@Data
public class StudentGroupUpdateReqDTO {

    /** 学员组ID */
    @NotNull(message = "学员组ID不能为空")
    private Long id;

    /** 学员组名称 */
    @NotBlank(message = "学员组名称不能为空")
    @Size(max = 100, message = "学员组名称长度不能超过100个字符")
    private String name;

    /** 描述 */
    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;

    /** 排序 */
    private Integer sortOrder = 0;

    /** 状态（1=启用 0=禁用） */
    private Integer status = 1;
}
