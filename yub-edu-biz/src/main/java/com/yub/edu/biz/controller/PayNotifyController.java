package com.yub.edu.biz.controller;

import com.yub.edu.biz.service.fund.FundService;
import com.yub.edu.biz.service.fund.PaymentOrderService;
import com.yub.edu.biz.service.fund.payment.PaymentChannelFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付回调 Controller（公开接口，无需认证）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 支付回调处理接口（微信支付回调）
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/pay/notify")
@RequiredArgsConstructor
public class PayNotifyController {

    private final PaymentOrderService paymentOrderService;
    private final FundService fundService;
    private final PaymentChannelFactory channelFactory;

    /**
     * 微信支付回调
     *
     * @param request HTTP请求
     * @return 回调处理结果（SUCCESS/FAIL）
     */
    @PostMapping("/wechat")
    public Map<String, Object> wechatNotify(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderNo = request.getParameter("order_no");
            String channelOrderNo = request.getParameter("channel_order_no");
            log.info("收到微信支付回调: orderNo={}", orderNo);

            if (!channelFactory.getChannel("WECHAT_H5").verifyCallback(request)) {
                log.error("微信回调验签失败: orderNo={}", orderNo);
                result.put("code", "FAIL");
                result.put("message", "验签失败");
                return result;
            }

            paymentOrderService.handleRechargeCallback(orderNo, channelOrderNo);
            var order = paymentOrderService.getOrder(orderNo);
            fundService.recharge(order.getUserId(), order.getAmount(), orderNo);

            result.put("code", "SUCCESS");
            result.put("message", "成功");
        } catch (Exception e) {
            log.error("处理微信支付回调异常", e);
            result.put("code", "FAIL");
            result.put("message", e.getMessage());
        }
        return result;
    }
}
