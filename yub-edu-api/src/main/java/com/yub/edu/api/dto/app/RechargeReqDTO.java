package com.yub.edu.api.dto.app;

import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 充值请求DTO
 * @Version 1.0
 */
@Data
public class RechargeReqDTO {
    /** 充值金额 */
    @NotNull(message = "充值金额不能为空")
    @DecimalMin(value = "0.01", message = "充值金额最小为0.01元")
    private BigDecimal amount;
}
