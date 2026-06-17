package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增教师职称请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 新增教师职称请求参数
 * @Version: 1.0.0
 */
@Data
public class TeacherTitleCreateReqDTO {

    /** 职称名称 */
    @NotBlank(message = "职称名称不能为空")
    @Size(max = 100, message = "职称名称长度不能超过100个字符")
    private String name;

    /** 备注 */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /** 状态（1=启用 0=禁用，默认启用） */
    private Integer status = 1;
}
