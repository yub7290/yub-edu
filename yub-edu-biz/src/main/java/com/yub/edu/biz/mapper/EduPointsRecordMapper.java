package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduPointsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分记录 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分记录数据访问层
 * @Version: 1.0
 */
@Mapper
public interface EduPointsRecordMapper {

    /**
     * 根据用户ID查询积分记录列表
     *
     * @param userId 用户ID
     * @return 积分记录列表
     */
    List<EduPointsRecord> selectByUserId(@Param("userId") Long userId);

    /**
     * 新增积分记录
     *
     * @param record 积分记录
     * @return 影响行数
     */
    int insert(EduPointsRecord record);

    /**
     * 统计用户今日指定业务类型的积分获得次数
     *
     * @param userId  用户ID
     * @param bizType 业务类型
     * @return 当日获得次数
     */
    int countTodayByUserIdAndBizType(@Param("userId") Long userId, @Param("bizType") String bizType);
}
