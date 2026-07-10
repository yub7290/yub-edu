package com.yub.edu.api.vo.app;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 课程订单VO
 * @Version 1.0
 */
@Data
public class CourseOrderVO {
    /** 平台订单号 */
    private String orderNo;
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
    /** 支付时间 */
    private String paidAt;
}
