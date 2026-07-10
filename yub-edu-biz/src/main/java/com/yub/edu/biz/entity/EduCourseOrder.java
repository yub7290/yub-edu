package com.yub.edu.biz.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 课程订单实体
 * @Version 1.0
 */
@Data
public class EduCourseOrder {
    /** 主键ID */
    private Long id;
    /** 平台订单号 */
    private String orderNo;
    /** 用户ID */
    private Long userId;
    /** 课程ID */
    private Long courseId;
    /** 课程名称 */
    private String courseName;
    /** 订单金额 */
    private BigDecimal amount;
    /** 支付方式: BALANCE/WECHAT/FREE */
    private String paymentMethod;
    /** 0=待支付 1=已支付 2=已退款 3=已关闭 */
    private Integer status;
    /** 退款金额 */
    private BigDecimal refundAmount;
    /** 支付时间 */
    private LocalDateTime paidAt;
    /** 退款时间 */
    private LocalDateTime refundedAt;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 逻辑删除 */
    private Integer deleted;
}
