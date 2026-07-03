package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduPointsOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分兑换订单 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分兑换订单数据访问
 * @Version: 1.0
 */
@Mapper
public interface EduPointsOrderMapper {

    /**
     * 分页查询积分兑换订单
     *
     * @param orderNo     订单编号(模糊查询)
     * @param exchangeCode 兑换码(模糊查询)
     * @param status      状态
     * @return 订单列表
     */
    List<EduPointsOrder> selectPage(@Param("orderNo") String orderNo,
                                    @Param("exchangeCode") String exchangeCode,
                                    @Param("status") Integer status);

    /**
     * 根据ID查询订单
     *
     * @param id 主键
     * @return 订单实体
     */
    EduPointsOrder selectById(@Param("id") Long id);

    /**
     * 根据兑换码查询订单
     *
     * @param exchangeCode 兑换码
     * @return 订单实体
     */
    EduPointsOrder selectByExchangeCode(@Param("exchangeCode") String exchangeCode);

    /**
     * 插入订单
     *
     * @param order 订单实体
     * @return 影响行数
     */
    int insert(EduPointsOrder order);

    /**
     * 更新订单状态为已核销
     *
     * @param id       主键
     * @param verifyBy 核销人ID
     * @return 影响行数
     */
    int updateToVerified(@Param("id") Long id, @Param("verifyBy") Long verifyBy);

    /**
     * 更新订单为已发货
     *
     * @param id             主键
     * @param expressCompany 快递公司
     * @param expressNo      快递单号
     * @return 影响行数
     */
    int updateToShipped(@Param("id") Long id,
                        @Param("expressCompany") String expressCompany,
                        @Param("expressNo") String expressNo);
}
