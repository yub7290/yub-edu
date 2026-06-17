package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 状态变更请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 启用/禁用状态变更请求参数
 * @Version: 1.0.0
 */
@Data
public class StatusReqDTO {

    /** 状态（1=启用 0=禁用） */
    @NotNull(message = "状态不能为空")
    private Integer status;
}
