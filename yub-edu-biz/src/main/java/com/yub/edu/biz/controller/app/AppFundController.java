package com.yub.edu.biz.controller.app;

import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.api.dto.app.RechargeReqDTO;
import com.yub.edu.api.vo.app.FundSummaryVO;
import com.yub.edu.api.vo.app.FundTransactionVO;
import com.yub.edu.biz.entity.EduPaymentOrder;
import com.yub.edu.biz.service.fund.FundService;
import com.yub.edu.biz.service.fund.FundTransactionService;
import com.yub.edu.biz.service.fund.PaymentOrderService;
import com.yub.edu.biz.service.fund.payment.PayResult;
import com.yub.framework.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 移动端资金 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 移动端资金管理接口（资金概况、交易记录、充值）
 * @Version: 1.0
 */
@RestController
@RequestMapping("/app/fund")
@RequiredArgsConstructor
public class AppFundController {

    private final FundService fundService;
    private final FundTransactionService fundTransactionService;
    private final PaymentOrderService paymentOrderService;

    /**
     * 获取资金概况
     *
     * @return 资金概况（可用余额、冻结金额等）
     */
    @GetMapping("/summary")
    public Response<FundSummaryVO> summary() {
        Long userId = SecurityUtils.getCurrentUserId();
        return Response.success(fundService.getSummary(userId));
    }

    /**
     * 分页查询交易记录
     *
     * @param transactionType 交易类型（可选）
     * @param pageNum         页码
     * @param pageSize        每页条数
     * @return 分页交易记录
     */
    @PostMapping("/transactions")
    public Response<PageResult<FundTransactionVO>> transactions(
            @RequestParam(required = false) String transactionType,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = SecurityUtils.getCurrentUserId();
        return Response.success(fundTransactionService.page(userId, transactionType, pageNum, pageSize));
    }

    /**
     * 发起充值
     *
     * @param dto 充值参数（金额）
     * @return 充值结果（含支付链接）
     */
    @PostMapping("/recharge")
    public Response<Map<String, Object>> recharge(@Valid @RequestBody RechargeReqDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        PayResult result = paymentOrderService.createRechargeOrder(userId, dto.getAmount(), "WECHAT_H5");
        Map<String, Object> data = new HashMap<>();
        data.put("success", result.isSuccess());
        data.put("payUrl", result.getPayUrl());
        return Response.success(data);
    }

    /**
     * 查询充值结果
     *
     * @param orderNo 订单号
     * @return 充值结果（状态、订单号）
     */
    @GetMapping("/recharge/{orderNo}/result")
    public Response<Map<String, Object>> rechargeResult(@PathVariable("orderNo") String orderNo) {
        EduPaymentOrder order = paymentOrderService.getOrder(orderNo);
        Map<String, Object> data = new HashMap<>();
        data.put("status", order.getStatus());
        data.put("orderNo", order.getOrderNo());
        return Response.success(data);
    }
}
