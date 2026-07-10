package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduStudyRecord;
import com.yub.edu.biz.mapper.StudyRecordMapper;
import com.yub.edu.biz.service.StudyRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 学习记录服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 学习记录管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudyRecordServiceImpl implements StudyRecordService {

    private final StudyRecordMapper mapper;

    @Override
    public EduStudyRecord selectByStudentAndChapter(Long studentId, Long courseId, Long chapterId) {
        return mapper.selectByStudentAndChapter(studentId, courseId, chapterId);
    }

    @Override
    public int insert(EduStudyRecord record) {
        return mapper.insert(record);
    }

    @Override
    public int updateById(EduStudyRecord record) {
        return mapper.updateById(record);
    }

    @Override
    public Integer selectTotalStudySecond(Long studentId) {
        return mapper.selectTotalStudySecond(studentId);
    }

    @Override
    public Integer selectCourseCount(Long studentId) {
        return mapper.selectCourseCount(studentId);
    }

    @Override
    public int countStudiedChapters(Long studentId, Long courseId) {
        return mapper.countStudiedChapters(studentId, courseId);
    }
}
