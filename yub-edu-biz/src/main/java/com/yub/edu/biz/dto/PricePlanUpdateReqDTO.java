package com.yub.edu.biz.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 课程价格方案更新请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 更新价格方案请求参数
 * @Version: 1.0.0
 */
@Data
public class PricePlanUpdateReqDTO {

    /** 价格方案ID */
    @NotNull(message = "价格方案ID不能为空")
    private Long id;

    /** 月数 */
    @Min(value = 1, message = "月数不能小于1")
    private Integer months;

    /** 价格 */
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不能为负数")
    private BigDecimal price;

    /** 代金券金额 */
    @DecimalMin(value = "0.00", message = "代金券金额不能为负数")
    private BigDecimal voucherAmount;

    /** 状态 */
    private Integer status;

    /** 排序 */
    private Integer sort;
}
