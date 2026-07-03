package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单个课程的练习概览响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 包含课程信息和练习概览，用于"我的学习记录"页面
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePracticeOverviewRespVO {

    /** 课程ID */
    private Long courseId;

    /** 课程名称 */
    private String courseName;

    /** 练习概览 */
    private PracticeOverviewRespVO overview;
}
