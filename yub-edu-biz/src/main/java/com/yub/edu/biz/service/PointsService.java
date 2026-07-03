package com.yub.edu.biz.service;

import com.yub.common.model.PageResult;
import com.yub.edu.api.vo.app.PointsAccountVO;
import com.yub.edu.api.vo.app.PointsRecordVO;

/**
 * 积分 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分业务接口
 * @Version: 1.0
 */
public interface PointsService {

    /**
     * 获取用户积分账户信息（若不存在则自动创建）
     *
     * @param userId 用户ID
     * @return 积分账户信息
     */
    PointsAccountVO getAccountInfo(Long userId);

    /**
     * 获得积分（乐观锁重试，最多3次）
     *
     * @param userId      用户ID
     * @param points      积分数量
     * @param changeType  变动类型
     * @param description 变动描述
     * @param bizId       关联业务ID
     * @param bizType     关联业务类型
     */
    void earnPoints(Long userId, Integer points, Integer changeType, String description, String bizId, String bizType);

    /**
     * 消耗积分（余额不足时抛出异常）
     *
     * @param userId      用户ID
     * @param points      积分数量
     * @param changeType  变动类型
     * @param description 变动描述
     * @param bizId       关联业务ID
     * @param bizType     关联业务类型
     */
    void spendPoints(Long userId, Integer points, Integer changeType, String description, String bizId, String bizType);

    /**
     * 分页查询用户积分记录
     *
     * @param userId   用户ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 分页积分记录
     */
    PageResult<PointsRecordVO> getRecords(Long userId, Integer pageNum, Integer pageSize);
}
