package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.CourseExamStatsResult;
import com.yub.edu.biz.entity.EduExamRecord;

import java.util.List;

/**
 * 考试记录服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 考试记录管理服务
 * @Version: 1.0.0
 */
public interface EduExamRecordService {

    /**
     * 查询用户在某课程下的考试统计
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 统计结果
     */
    CourseExamStatsResult selectCourseExamStats(Long userId, Long courseId);

    /**
     * 查询用户在某课程下的所有考试记录
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 考试记录列表
     */
    List<EduExamRecord> selectByUserAndCourse(Long userId, Long courseId);
}
