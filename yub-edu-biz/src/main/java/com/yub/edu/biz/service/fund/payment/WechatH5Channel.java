package com.yub.edu.biz.service.fund.payment;

import com.yub.edu.biz.entity.EduPaymentOrder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 微信H5支付渠道实现（骨架，待接入微信SDK）
 * @Version 1.0
 */
@Slf4j
@Component("WECHAT_H5")
public class WechatH5Channel implements PaymentChannel {
    @Override
    public PayResult createPay(EduPaymentOrder order) {
        log.info("创建微信H5支付, 订单号: {}, 金额: {}", order.getOrderNo(), order.getAmount());
        return new PayResult(false, null, "微信支付尚未接入");
    }
    @Override
    public PayStatus queryPay(String orderNo) {
        log.info("查询微信H5支付状态, 订单号: {}", orderNo);
        return PayStatus.PENDING;
    }
    @Override
    public RefundResult refund(String orderNo, BigDecimal amount) {
        log.info("微信H5退款, 订单号: {}, 金额: {}", orderNo, amount);
        return new RefundResult(false, "微信退款尚未接入");
    }
    @Override
    public boolean verifyCallback(HttpServletRequest request) {
        log.info("验证微信支付回调签名");
        return true;
    }
}
