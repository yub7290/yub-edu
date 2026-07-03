package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduPointsOrder;
import com.yub.edu.biz.service.PointsOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 积分兑换订单 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分兑换订单管理接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/edu/points-order")
@RequiredArgsConstructor
public class EduPointsOrderController {

    private final PointsOrderService pointsOrderService;

    /**
     * 分页查询积分兑换订单
     *
     * @param query 查询条件（含分页参数）
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<EduPointsOrder>> page(@RequestBody PageQuery<Map<String, Object>> query) {
        Map<String, Object> param = query.getQueryParam();
        String orderNo = param != null ? (String) param.get("orderNo") : null;
        String exchangeCode = param != null ? (String) param.get("exchangeCode") : null;
        Integer status = param != null ? (Integer) param.get("status") : null;
        return Response.success(pointsOrderService.pageOrders(
                orderNo, exchangeCode, status,
                query.getPageParam().getPageNum(), query.getPageParam().getPageSize()));
    }

    /**
     * 根据兑换码查询订单
     *
     * @param exchangeCode 兑换码
     * @return 订单详情
     */
    @GetMapping("/by-code/{exchangeCode}")
    public Response<EduPointsOrder> getByExchangeCode(@PathVariable("exchangeCode") String exchangeCode) {
        return Response.success(pointsOrderService.getByExchangeCode(exchangeCode));
    }

    /**
     * 获取订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public Response<EduPointsOrder> getDetail(@PathVariable("id") Long id) {
        return Response.success(pointsOrderService.getDetail(id));
    }

    /**
     * 核销兑换码
     *
     * @param body 包含 exchangeCode 的请求体
     * @return 响应
     */
    @Log(value = "核销兑换码", type = "UPDATE")
    @PostMapping("/verify")
    public Response<Void> verify(@RequestBody Map<String, String> body) {
        Long operatorId = com.yub.framework.security.SecurityUtils.getCurrentUserId();
        pointsOrderService.verifyByCode(body.get("exchangeCode"), operatorId);
        return Response.success();
    }

    /**
     * 发货
     *
     * @param id   订单ID
     * @param body 包含 expressCompany 和 expressNo 的请求体
     * @return 响应
     */
    @Log(value = "订单发货", type = "UPDATE")
    @PutMapping("/{id}/ship")
    public Response<Void> ship(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        pointsOrderService.shipOrder(id, body.get("expressCompany"), body.get("expressNo"));
        return Response.success();
    }
}
