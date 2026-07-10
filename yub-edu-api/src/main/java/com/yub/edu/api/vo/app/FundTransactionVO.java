package com.yub.edu.api.vo.app;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 资金交易记录VO
 * @Version 1.0
 */
@Data
public class FundTransactionVO {
    /** 主键ID */
    private Long id;
    /** 交易类型: RECHARGE/COURSE_PURCHASE/REFUND */
    private String transactionType;
    /** 金额(正=收入, 负=支出) */
    private BigDecimal amount;
    /** 交易后余额快照 */
    private BigDecimal balanceAfter;
    /** 交易描述 */
    private String description;
    /** 创建时间 */
    private String createTime;
}
