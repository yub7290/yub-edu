package com.yub.edu.api.vo.app;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 资金余额概览VO
 * @Version 1.0
 */
@Data
public class FundSummaryVO {
    /** 可用余额 */
    private BigDecimal balance;
    /** 累计充值 */
    private BigDecimal totalRecharge;
    /** 累计消费 */
    private BigDecimal totalConsumption;
}
