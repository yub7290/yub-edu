package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 考试详情响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端考试详情（含历史成绩）
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamInfoRespVO {

    /** 试卷ID */
    private Long id;

    /** 试卷标题 */
    private String name;

    /** 副标题 */
    private String subtitle;

    /** 课程ID */
    private Long courseId;

    /** 课程名称 */
    private String courseName;

    /** 考试时长（分钟） */
    private Integer duration;

    /** 满分 */
    private Integer totalScore;

    /** 及格分 */
    private Integer passScore;

    /** 简介 */
    private String introduction;

    /** 注意事项 */
    private String notes;

    /** 出卷人 */
    private String examiner;

    /** 最大参考次数（0=不限） */
    private Integer maxAttempts;

    /** 章节完成率准入门槛（%） */
    private Integer chapterPassRate;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    /** 历史成绩列表 */
    private List<ExamHistoryRespVO> historyList;
}
