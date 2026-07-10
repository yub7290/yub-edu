package com.yub.edu.biz.service.fund.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 支付结果VO
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayResult {
    private boolean success;
    private String payUrl;
    private String errorMessage;
}
