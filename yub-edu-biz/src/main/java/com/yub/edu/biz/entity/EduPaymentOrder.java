package com.yub.edu.biz.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 支付订单实体
 * @Version 1.0
 */
@Data
public class EduPaymentOrder {
    /** 主键ID */
    private Long id;
    /** 平台订单号 */
    private String orderNo;
    /** 用户ID */
    private Long userId;
    /** 支付渠道: WECHAT_H5 */
    private String paymentChannel;
    /** 订单金额 */
    private BigDecimal amount;
    /** 0=待支付 1=支付中 2=已支付 3=已关闭 */
    private Integer status;
    /** 渠道方订单号 */
    private String channelOrderNo;
    /** 支付完成时间 */
    private LocalDateTime paidAt;
    /** 订单过期时间 */
    private LocalDateTime expireAt;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 逻辑删除 */
    private Integer deleted;
}
