package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduFundTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资金交易记录 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 资金交易记录数据访问
 * @Version: 1.0
 */
@Mapper
public interface EduFundTransactionMapper {

    /**
     * 插入交易记录
     *
     * @param transaction 交易记录实体
     * @return 影响行数
     */
    int insert(EduFundTransaction transaction);

    /**
     * 分页查询用户本人的交易记录
     *
     * @param userId          用户ID
     * @param transactionType 交易类型(可选过滤)
     * @return 交易记录列表
     */
    List<EduFundTransaction> selectPage(@Param("userId") Long userId,
    @Param("transactionType") String transactionType);


    /**
     * 分页查询全部交易记录(管理员)
     *
     * @param userId          用户ID(可选过滤)
     * @param userName        用户名(可选模糊查询)
     * @param transactionType 交易类型(可选过滤)
     * @param startTime       开始时间(可选)
     * @param endTime         结束时间(可选)
     * @return 交易记录列表
     */
    List<EduFundTransaction> selectAllPage(@Param("userId") Long userId,
    @Param("userName") String userName,
    @Param("transactionType") String transactionType,
    @Param("startTime") String startTime,
    @Param("endTime") String endTime);
}
