package com.yub.edu.biz.service.fund;

import com.yub.edu.biz.entity.EduPaymentOrder;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduPaymentOrderMapper;
import com.yub.edu.biz.service.fund.payment.PaymentChannel;
import com.yub.edu.biz.service.fund.payment.PaymentChannelFactory;
import com.yub.edu.biz.service.fund.payment.PayResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 支付订单服务
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOrderService {
    private final EduPaymentOrderMapper paymentOrderMapper;
    private final PaymentChannelFactory channelFactory;

    public PayResult createRechargeOrder(Long userId, BigDecimal amount, String channel) {
        String orderNo = "RC" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
        EduPaymentOrder order = new EduPaymentOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setPaymentChannel(channel);
        order.setAmount(amount);
        order.setStatus(0);
        order.setExpireAt(LocalDateTime.now().plusMinutes(30));
        paymentOrderMapper.insert(order);
        log.info("创建充值订单: orderNo={}, userId={}, amount={}", orderNo, userId, amount);
        PaymentChannel paymentChannel = channelFactory.getChannel(channel);
        return paymentChannel.createPay(order);
    }

    public void handleRechargeCallback(String orderNo, String channelOrderNo) {
        EduPaymentOrder order = paymentOrderMapper.selectByOrderNo(orderNo);
        if (order == null) throw new EduException(EduErrorCode.PAYMENT_ORDER_NOT_FOUND);
        if (order.getStatus() == 2) { log.warn("重复支付回调, orderNo={}", orderNo); return; }
        if (LocalDateTime.now().isAfter(order.getExpireAt())) {
            paymentOrderMapper.updateToClosed(orderNo);
            throw new EduException(EduErrorCode.PAYMENT_ORDER_EXPIRED);
        }
        int rows = paymentOrderMapper.updateToPaid(orderNo, channelOrderNo);
        if (rows == 0) throw new EduException(EduErrorCode.DUPLICATE_PAYMENT);
    }

    public EduPaymentOrder getOrder(String orderNo) {
        EduPaymentOrder order = paymentOrderMapper.selectByOrderNo(orderNo);
        if (order == null) throw new EduException(EduErrorCode.PAYMENT_ORDER_NOT_FOUND);
        return order;
    }
}
