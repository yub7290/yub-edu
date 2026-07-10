package com.yub.edu.biz.service;

import com.yub.edu.biz.entity.EduPointsProduct;

import java.util.List;

/**
 * 积分商品服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 积分商品管理服务
 * @Version: 1.0.0
 */
public interface EduPointsProductService {

    /**
     * 分页查询积分商品
     *
     * @param name   商品名称(模糊查询)
     * @param status 状态
     * @return 积分商品列表
     */
    List<EduPointsProduct> selectPage(String name, Integer status);

    /**
     * 根据ID查询积分商品
     *
     * @param id 商品ID
     * @return 积分商品实体
     */
    EduPointsProduct selectById(Long id);

    /**
     * 新增积分商品
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
     * @param id 商品ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 更新商品状态
     *
     * @param id     商品ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(Long id, Integer status);
}
