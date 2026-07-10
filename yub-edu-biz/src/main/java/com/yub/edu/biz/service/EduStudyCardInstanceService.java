package com.yub.edu.biz.service;

/**
 * 学习卡实例服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 学习卡实例管理服务
 * @Version: 1.0.0
 */
public interface EduStudyCardInstanceService {

    /**
     * 统计未使用的学习卡数量
     *
     * @param cardId 卡模板ID
     * @return 未使用数量
     */
    int countAvailableByCardId(Long cardId);
}
