package com.yub.edu.biz.service;

import com.yub.edu.biz.entity.EduAiConfig;

/**
 * AI助教配置服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: AI助教配置管理服务
 * @Version: 1.0.0
 */
public interface EduAiConfigService {

    /**
     * 根据课程ID查询配置
     *
     * @param courseId 课程ID
     * @return AI助教配置
     */
    EduAiConfig selectByCourseId(Long courseId);

    /**
     * 新增配置
     *
     * @param config 配置信息
     * @return 影响行数
     */
    int insert(EduAiConfig config);

    /**
     * 更新配置
     *
     * @param config 配置信息
     * @return 影响行数
     */
    int update(EduAiConfig config);
}
