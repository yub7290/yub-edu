package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduPaymentOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 支付订单 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 支付订单数据访问
 * @Version: 1.0
 */
@Mapper
public interface EduPaymentOrderMapper {

    /**
     * 插入支付订单
     *
     * @param order 支付订单实体
     * @return 影响行数
     */
    int insert(EduPaymentOrder order);

    /**
     * 根据订单号查询
     *
     * @param orderNo 平台订单号
     * @return 支付订单实体
     */
    EduPaymentOrder selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 更新状态为支付中(0→1)
     *
     * @param orderNo 平台订单号
     * @return 影响行数
     */
    int updateToPaying(@Param("orderNo") String orderNo);

    /**
     * 更新状态为已支付(1→2)
     *
     * @param orderNo        平台订单号
     * @param channelOrderNo 渠道方订单号
     * @return 影响行数
     */
    int updateToPaid(@Param("orderNo") String orderNo, @Param("channelOrderNo") String channelOrderNo);

    /**
     * 更新状态为已关闭(0/1→3)
     *
     * @param orderNo 平台订单号
     * @return 影响行数
     */
    int updateToClosed(@Param("orderNo") String orderNo);
}
