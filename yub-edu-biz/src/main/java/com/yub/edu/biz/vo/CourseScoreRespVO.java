package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 课程综合成绩 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 综合成绩页面展示数据
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseScoreRespVO {

    /** 课程ID */
    private Long courseId;

    /** 课程名称 */
    private String courseName;

    /** 课程封面 */
    private String courseImage;

    // ===== 考试统计 =====

    /** 考试平均分 */
    private Integer examAvgScore;

    /** 考试最高分 */
    private Integer examMaxScore;

    /** 考试总次数 */
    private Integer examTotalCount;

    /** 考试及格次数 */
    private Integer examPassCount;

    /** 考试及格率（%） */
    private Integer examPassRate;

    // ===== 练习统计 =====

    /** 练习总题数 */
    private Integer practiceTotalCount;

    /** 练习正确题数 */
    private Integer practiceCorrectCount;

    /** 练习正确率（%） */
    private Integer practiceAccuracyRate;

    // ===== 学习进度 =====

    /** 总章节数 */
    private Integer chapterTotalCount;

    /** 已学习章节数 */
    private Integer chapterStudiedCount;

    /** 章节学习进度（%） */
    private Integer chapterProgressRate;

    // ===== 考试历史 =====

    /** 该课程下所有考试的历史成绩列表 */
    private List<CourseExamHistoryVO> examHistoryList;
}
