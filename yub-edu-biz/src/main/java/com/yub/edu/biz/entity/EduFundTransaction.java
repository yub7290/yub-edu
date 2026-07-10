package com.yub.edu.biz.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 资金交易记录实体
 * @Version 1.0
 */
@Data
public class EduFundTransaction {
    /** 主键ID */
    private Long id;
    /** 用户ID */
    private Long userId;
    /** 交易类型: RECHARGE/COURSE_PURCHASE/REFUND */
    private String transactionType;
    /** 金额(正=收入, 负=支出) */
    private BigDecimal amount;
    /** 交易后余额快照 */
    private BigDecimal balanceAfter;
    /** 关联业务ID */
    private String relatedId;
    /** 交易描述 */
    private String description;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 逻辑删除 */
    private Integer deleted;
}
