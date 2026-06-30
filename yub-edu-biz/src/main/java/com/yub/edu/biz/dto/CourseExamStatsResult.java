package com.yub.edu.biz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程考试统计结果 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 用于 mapper 返回课程级别的考试聚合统计
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseExamStatsResult {

    /** 平均分 */
    private Integer avgScore;

    /** 最高分 */
    private Integer maxScore;

    /** 总考试次数 */
    private Integer totalCount;

    /** 及格次数 */
    private Integer passCount;
}
