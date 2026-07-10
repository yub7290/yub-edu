package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.mapper.EduPracticeRecordMapper;
import com.yub.edu.biz.service.EduPracticeRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 练习记录服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 练习记录管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduPracticeRecordServiceImpl implements EduPracticeRecordService {

    private final EduPracticeRecordMapper mapper;

    @Override
    public int countByUserAndCourse(Long userId, Long courseId) {
        return mapper.countByUserAndCourse(userId, courseId);
    }

    @Override
    public int countCorrectByUserAndCourse(Long userId, Long courseId) {
        return mapper.countCorrectByUserAndCourse(userId, courseId);
    }
}
