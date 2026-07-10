package com.yub.edu.biz.service.fund.payment;

import com.yub.edu.biz.entity.EduPaymentOrder;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 支付渠道接口
 * @Version 1.0
 */
public interface PaymentChannel {
    /** 创建支付 */
    PayResult createPay(EduPaymentOrder order);
    /** 查询支付状态 */
    PayStatus queryPay(String orderNo);
    /** 退款 */
    RefundResult refund(String orderNo, BigDecimal amount);
    /** 验证回调签名 */
    boolean verifyCallback(HttpServletRequest request);
}
