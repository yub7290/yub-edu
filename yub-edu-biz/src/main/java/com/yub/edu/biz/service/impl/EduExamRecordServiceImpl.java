package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.CourseExamStatsResult;
import com.yub.edu.biz.entity.EduExamRecord;
import com.yub.edu.biz.mapper.EduExamRecordMapper;
import com.yub.edu.biz.service.EduExamRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考试记录服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 考试记录管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduExamRecordServiceImpl implements EduExamRecordService {

    private final EduExamRecordMapper mapper;

    @Override
    public CourseExamStatsResult selectCourseExamStats(Long userId, Long courseId) {
        return mapper.selectCourseExamStats(userId, courseId);
    }

    @Override
    public List<EduExamRecord> selectByUserAndCourse(Long userId, Long courseId) {
        return mapper.selectByUserAndCourse(userId, courseId);
    }
}
