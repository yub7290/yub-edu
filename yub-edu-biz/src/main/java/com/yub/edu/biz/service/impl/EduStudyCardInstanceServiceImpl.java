package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.mapper.EduStudyCardInstanceMapper;
import com.yub.edu.biz.service.EduStudyCardInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 学习卡实例服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 学习卡实例管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduStudyCardInstanceServiceImpl implements EduStudyCardInstanceService {

    private final EduStudyCardInstanceMapper mapper;

    @Override
    public int countAvailableByCardId(Long cardId) {
        return mapper.countAvailableByCardId(cardId);
    }
}
