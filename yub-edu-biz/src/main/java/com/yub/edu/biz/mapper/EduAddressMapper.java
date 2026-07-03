package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户地址 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 用户地址簿数据访问
 * @Version: 1.0
 */
@Mapper
public interface EduAddressMapper {

    /**
     * 查询用户地址列表
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    List<EduAddress> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据ID查询地址
     *
     * @param id 主键
     * @return 地址实体
     */
    EduAddress selectById(@Param("id") Long id);

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
     * @param id     主键
     * @param userId 用户ID（用于权限校验）
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 取消用户所有默认地址
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int clearDefault(@Param("userId") Long userId);

    /**
     * 设置默认地址
     *
     * @param id     主键
     * @param userId 用户ID
     * @return 影响行数
     */
    int setDefault(@Param("id") Long id, @Param("userId") Long userId);
}
