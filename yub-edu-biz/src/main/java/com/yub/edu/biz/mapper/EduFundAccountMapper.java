package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduFundAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 用户资金账户 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 用户资金账户数据访问
 * @Version: 1.0
 */
@Mapper
public interface EduFundAccountMapper {

    /**
     * 根据用户ID查询账户
     *
     * @param userId 用户ID
     * @return 资金账户实体
     */
    EduFundAccount selectByUserId(@Param("userId") Long userId);

    /**
     * 插入账户
     *
     * @param account 账户实体
     * @return 影响行数
     */
    int insert(EduFundAccount account);

    /**
     * 充值(乐观锁)
     *
     * @param id      主键ID
     * @param amount  充值金额
     * @param version 当前版本号
     * @return 影响行数
     */
    int recharge(@Param("id") Long id, @Param("amount") BigDecimal amount, @Param("version") Integer version);

    /**
     * 扣款(乐观锁 + 余额校验)
     *
     * @param id      主键ID
     * @param amount  扣款金额
     * @param version 当前版本号
     * @return 影响行数
     */
    int deduct(@Param("id") Long id, @Param("amount") BigDecimal amount, @Param("version") Integer version);
}
