package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程结课考试信息 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-27
 * @Description: 课程详情页展示结课考试信息
 * @Version: 2.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseFinalExamRespVO {

    /** 试卷ID */
    private Long examId;

    /** 试卷名称 */
    private String examName;

    /** 考试时长（分钟） */
    private Integer duration;

    /** 满分 */
    private Integer totalScore;

    /** 及格分 */
    private Integer passScore;

    /** 最大参考次数（0=不限） */
    private Integer maxAttempts;

    /** 章节完成率准入门槛（%，0=不校验） */
    private Integer chapterCompletionRate;

    /** 章节完成率（%，实际进度） */
    private Integer currentCompletionRate;

    /** 是否可考 */
    private Boolean canTake;

    /** 不可考原因 */
    private String cannotTakeReason;

    /** 已考次数 */
    private Integer attemptedCount;

    /** 剩余次数（-1=不限） */
    private Integer remainingAttempts;

    /** 章节完成率是否达标 */
    private Boolean chapterProgressMet;

    /** 历史最高分 */
    private Integer highestScore;

    /** 历史是否及格过 */
    private Boolean everPassed;

    /** 历史成绩列表 */
    private java.util.List<ExamHistoryRespVO> historyList;
}
