package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduPointsAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 积分账户 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分账户数据访问层
 * @Version: 1.0
 */
@Mapper
public interface EduPointsAccountMapper {

    /**
     * 根据用户ID查询积分账户
     *
     * @param userId 用户ID
     * @return 积分账户
     */
    EduPointsAccount selectByUserId(@Param("userId") Long userId);

    /**
     * 新增积分账户
     *
     * @param account 积分账户
     * @return 影响行数
     */
    int insert(EduPointsAccount account);

    /**
     * 更新积分（使用乐观锁）
     *
     * @param account 积分账户
     * @return 影响行数
     */
    int updateByUserId(EduPointsAccount account);
}
