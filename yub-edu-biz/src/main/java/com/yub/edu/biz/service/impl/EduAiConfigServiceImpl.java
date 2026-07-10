package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduAiConfig;
import com.yub.edu.biz.mapper.EduAiConfigMapper;
import com.yub.edu.biz.service.EduAiConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AI助教配置服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: AI助教配置管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduAiConfigServiceImpl implements EduAiConfigService {

    private final EduAiConfigMapper mapper;

    @Override
    public EduAiConfig selectByCourseId(Long courseId) {
        return mapper.selectByCourseId(courseId);
    }

    @Override
    public int insert(EduAiConfig config) {
        return mapper.insert(config);
    }

    @Override
    public int update(EduAiConfig config) {
        return mapper.update(config);
    }
}
