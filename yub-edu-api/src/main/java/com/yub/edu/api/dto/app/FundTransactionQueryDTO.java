package com.yub.edu.api.dto.app;

import lombok.Data;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 资金交易记录查询DTO
 * @Version 1.0
 */
@Data
public class FundTransactionQueryDTO {
    /** 交易类型: RECHARGE/COURSE_PURCHASE/REFUND，空=全部 */
    private String transactionType;
}
