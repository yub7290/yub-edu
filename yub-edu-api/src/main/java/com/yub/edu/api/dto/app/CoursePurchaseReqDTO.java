package com.yub.edu.api.dto.app;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 课程购买请求DTO
 * @Version 1.0
 */
@Data
public class CoursePurchaseReqDTO {
    /** 课程ID */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;
    /** 支付方式: BALANCE */
    @NotNull(message = "支付方式不能为空")
    private String paymentMethod;
}
