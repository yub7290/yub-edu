package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 课程考试历史成绩 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 综合成绩页面中展示的考试历史（含考试名称）
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseExamHistoryVO {

    /** 考试记录ID */
    private Long recordId;

    /** 考试ID */
    private Long examId;

    /** 考试名称 */
    private String examName;

    /** 是否结课考试 */
    private Integer isFinalExam;

    /** 实际得分 */
    private Integer score;

    /** 试卷满分 */
    private Integer totalScore;

    /** 及格分 */
    private Integer passScore;

    /** 是否及格 1:是 0:否 */
    private Integer isPass;

    /** 第几次考试 */
    private Integer attemptNo;

    /** 提交时间 */
    private LocalDateTime submitTime;
}
