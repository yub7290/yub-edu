package com.yub.edu.biz.service.fund.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 支付渠道工厂
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class PaymentChannelFactory {
    private final Map<String, PaymentChannel> channelMap;
    public PaymentChannel getChannel(String channel) {
        PaymentChannel paymentChannel = channelMap.get(channel);
        if (paymentChannel == null) throw new IllegalArgumentException("不支持的支付渠道: " + channel);
        return paymentChannel;
    }
}
