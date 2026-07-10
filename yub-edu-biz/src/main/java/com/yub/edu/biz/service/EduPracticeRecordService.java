package com.yub.edu.biz.service;

/**
 * 练习记录服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 练习记录管理服务
 * @Version: 1.0.0
 */
public interface EduPracticeRecordService {

    /**
     * 统计用户在某课程下的练习次数
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 练习次数
     */
    int countByUserAndCourse(Long userId, Long courseId);

    /**
     * 统计用户在某课程下的正确答题数
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 正确答题数
     */
    int countCorrectByUserAndCourse(Long userId, Long courseId);
}
