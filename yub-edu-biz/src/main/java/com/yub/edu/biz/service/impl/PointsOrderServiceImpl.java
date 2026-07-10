package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.entity.EduPointsOrder;
import com.yub.edu.biz.entity.EduPointsProduct;
import com.yub.edu.biz.entity.EduStudyCardInstance;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduPointsOrderMapper;
import com.yub.edu.biz.mapper.EduPointsProductMapper;
import com.yub.edu.biz.mapper.EduStudyCardInstanceMapper;
import com.yub.edu.biz.service.PointsOrderService;
import com.yub.edu.biz.service.PointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 积分兑换订单 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分兑换订单业务实现
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PointsOrderServiceImpl implements PointsOrderService {

    private final EduPointsOrderMapper orderMapper;
    private final EduPointsProductMapper productMapper;
    private final EduStudyCardInstanceMapper cardInstanceMapper;
    // TODO: 架构治理 - Service间耦合: PointsOrderService 依赖 PointsService，应通过 Manager 层解耦
    private final PointsService pointsService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EduPointsOrder createOrder(Long userId, EduPointsProduct product, int exchangeType,
                                      String receiverName, String receiverPhone,
                                      String receiverAddress) {
        String orderNo = generateOrderNo();

        EduPointsOrder order = new EduPointsOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setProductId(product.getId());
        order.setProductName(product.getName());
        order.setProductType(product.getProductType());
        order.setRequiredPoints(product.getRequiredPoints());
        order.setExchangeType(exchangeType);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);

        if (Integer.valueOf(2).equals(product.getProductType())) {
            processStudyCardOrder(order, product, userId);
        } else {
            processPhysicalOrder(order, product);
        }

        order.setStatus(0);
        orderMapper.insert(order);

        pointsService.spendPoints(userId, product.getRequiredPoints(),
                2, "兑换商品: " + product.getName(),
                String.valueOf(product.getId()), "exchange");

        log.info("兑换订单创建成功, orderNo={}, userId={}, productId={}",
                orderNo, userId, product.getId());
        return order;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EduPointsOrder verifyByCode(String exchangeCode, Long operatorId) {
        EduPointsOrder order = orderMapper.selectByExchangeCode(exchangeCode);
        if (order == null) {
            throw new EduException(EduErrorCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != 0) {
            throw new EduException(EduErrorCode.ORDER_ALREADY_PROCESSED);
        }
        int rows = orderMapper.updateToVerified(order.getId(), operatorId);
        if (rows <= 0) {
            throw new EduException(EduErrorCode.ORDER_ALREADY_PROCESSED);
        }
        log.info("兑换码核销成功, exchangeCode={}, operatorId={}", exchangeCode, operatorId);
        return orderMapper.selectById(order.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResult<EduPointsOrder> pageOrders(String orderNo, String exchangeCode,
                                                  Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduPointsOrder> list = orderMapper.selectPage(orderNo, exchangeCode, status);
        PageInfo<EduPointsOrder> pageInfo = new PageInfo<>(list);
        return PageResult.of(list, pageInfo.getTotal());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shipOrder(Long id, String expressCompany, String expressNo) {
        EduPointsOrder order = orderMapper.selectById(id);
        if (order == null) {
            throw new EduException(EduErrorCode.ORDER_NOT_FOUND);
        }
        int rows = orderMapper.updateToShipped(id, expressCompany, expressNo);
        if (rows <= 0) {
            throw new EduException(EduErrorCode.ORDER_ALREADY_PROCESSED);
        }
        log.info("订单发货成功, id={}, expressCompany={}, expressNo={}", id, expressCompany, expressNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EduPointsOrder getDetail(Long id) {
        EduPointsOrder order = orderMapper.selectById(id);
        if (order == null) {
            throw new EduException(EduErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EduPointsOrder getByExchangeCode(String exchangeCode) {
        EduPointsOrder order = orderMapper.selectByExchangeCode(exchangeCode);
        if (order == null) {
            throw new EduException(EduErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    /**
     * 处理学习卡类型订单：绑定用户并填充卡号信息
     *
     * @param order   订单
     * @param product 商品
     * @param userId  用户ID
     */
    private void processStudyCardOrder(EduPointsOrder order, EduPointsProduct product, Long userId) {
        int assigned = cardInstanceMapper.bindUserByCardId(product.getStudyCardId(), userId);
        if (assigned <= 0) {
            throw new EduException(EduErrorCode.POINTS_INSUFFICIENT);
        }
        List<EduStudyCardInstance> instances = cardInstanceMapper.selectByUserId(userId);
        if (instances == null || instances.isEmpty()) {
            throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_NOT_FOUND);
        }
        EduStudyCardInstance instance = instances.get(0);
        order.setCardInstanceId(instance.getId());
        order.setCardNo(instance.getCardNo());
        order.setCardSecret(instance.getSecretCode());
    }

    /**
     * 处理实物类型订单：扣减库存
     *
     * @param order   订单
     * @param product 商品
     */
    private void processPhysicalOrder(EduPointsOrder order, EduPointsProduct product) {
        int rows = productMapper.decrementStock(product.getId());
        if (rows <= 0) {
            throw new EduException(EduErrorCode.POINTS_INSUFFICIENT);
        }
        // 仅线下自取生成兑换码，邮寄到家不生成
        if (order.getExchangeType() == 1) {
            order.setExchangeCode(generateExchangeCode());
        }
    }

    /**
     * 生成订单编号
     *
     * @return 订单编号
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis()
                + String.format("%04d", (int) (Math.random() * 10000));
    }

    /**
     * 生成兑换码
     *
     * @return 兑换码
     */
    private String generateExchangeCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
