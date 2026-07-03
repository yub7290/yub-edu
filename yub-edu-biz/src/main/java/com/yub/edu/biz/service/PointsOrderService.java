package com.yub.edu.biz.service;

import com.yub.common.model.PageResult;
import com.yub.edu.biz.entity.EduPointsOrder;
import com.yub.edu.biz.entity.EduPointsProduct;

/**
 * 积分兑换订单 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分兑换订单业务接口
 * @Version: 1.0
 */
public interface PointsOrderService {

    /**
     * 创建兑换订单
     *
     * @param userId          用户ID
     * @param product         兑换商品
     * @param exchangeType    兑换方式: 1=线下自取 2=邮寄到家
     * @param receiverName    收货人姓名
     * @param receiverPhone   收货人电话
     * @param receiverAddress 收货地址
     * @return 创建的订单
     */
    EduPointsOrder createOrder(Long userId, EduPointsProduct product, int exchangeType,
                               String receiverName, String receiverPhone, String receiverAddress);

    /**
     * 核销兑换码
     *
     * @param exchangeCode 兑换码
     * @param operatorId   操作人ID
     * @return 核销后的订单
     */
    EduPointsOrder verifyByCode(String exchangeCode, Long operatorId);

    /**
     * 分页查询订单
     *
     * @param orderNo      订单编号
     * @param exchangeCode 兑换码
     * @param status       状态
     * @param pageNum      页码
     * @param pageSize     每页大小
     * @return 分页订单列表
     */
    PageResult<EduPointsOrder> pageOrders(String orderNo, String exchangeCode,
                                          Integer status, int pageNum, int pageSize);

    /**
     * 发货
     *
     * @param id             订单ID
     * @param expressCompany 快递公司
     * @param expressNo      快递单号
     */
    void shipOrder(Long id, String expressCompany, String expressNo);

    /**
     * 获取订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    EduPointsOrder getDetail(Long id);

    /**
     * 根据兑换码查询订单
     *
     * @param exchangeCode 兑换码
     * @return 订单
     */
    EduPointsOrder getByExchangeCode(String exchangeCode);
}
