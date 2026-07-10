package com.yub.edu.biz.service;

import com.yub.edu.biz.entity.EduAddress;

import java.util.List;

/**
 * 用户地址服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 用户地址管理服务
 * @Version: 1.0.0
 */
public interface EduAddressService {

    /**
     * 查询用户地址列表
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    List<EduAddress> selectByUserId(Long userId);

    /**
     * 根据ID查询地址
     *
     * @param id 地址ID
     * @return 地址实体
     */
    EduAddress selectById(Long id);

    /**
     * 插入地址
     *
     * @param address 地址实体
     * @return 影响行数
     */
    int insert(EduAddress address);

    /**
     * 更新地址
     *
     * @param address 地址实体
     * @return 影响行数
     */
    int updateById(EduAddress address);

    /**
     * 逻辑删除地址
     *
     * @param id     地址ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteById(Long id, Long userId);

    /**
     * 取消用户所有默认地址
     *
     * @param userId 用户ID
     */
    void clearDefault(Long userId);

    /**
     * 设置默认地址
     *
     * @param id     地址ID
     * @param userId 用户ID
     */
    void setDefault(Long id, Long userId);
}
