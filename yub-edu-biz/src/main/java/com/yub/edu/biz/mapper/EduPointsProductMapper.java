package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduPointsProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分商品 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分商品数据访问
 * @Version: 1.0
 */
@Mapper
public interface EduPointsProductMapper {

    /**
     * 分页查询积分商品
     *
     * @param name   商品名称(模糊查询)
     * @param status 状态
     * @return 积分商品列表
     */
    List<EduPointsProduct> selectPage(@Param("name") String name, @Param("status") Integer status);

    /**
     * 根据ID查询积分商品
     *
     * @param id 主键
     * @return 积分商品实体
     */
    EduPointsProduct selectById(@Param("id") Long id);

    /**
     * 插入积分商品
     *
     * @param product 积分商品实体
     * @return 影响行数
     */
    int insert(EduPointsProduct product);

    /**
     * 更新积分商品
     *
     * @param product 积分商品实体
     * @return 影响行数
     */
    int updateById(EduPointsProduct product);

    /**
     * 逻辑删除积分商品
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 扣减库存（乐观锁）
     *
     * @param id 主键
     * @return 影响行数（0表示库存不足）
     */
    int decrementStock(@Param("id") Long id);

    /**
     * 更新商品状态
     *
     * @param id     主键
     * @param status 状态（1=上架 0=下架）
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
