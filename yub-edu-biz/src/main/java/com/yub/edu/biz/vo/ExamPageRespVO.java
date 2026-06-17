package com.yub.edu.biz.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷分页响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试卷分页列表响应
 * @Version: 1.0.0
 */
@Data
public class ExamPageRespVO {

    /** 试卷ID */
    private Long id;

    /** 试卷标题 */
    private String title;

    /** 专业名称 */
    private String majorName;

    /** 课程名称 */
    private String courseName;

    /** 满分 */
    private Integer totalScore;

    /** 考试时长（分钟） */
    private Integer duration;

    /** 是否结课考试 1:是 0:否 */
    private Integer isFinalExam;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 难度 1-5 */
    private Integer difficulty;

    /** 出卷人 */
    private String examiner;

    /** 创建时间 */
    private LocalDateTime createTime;
}
