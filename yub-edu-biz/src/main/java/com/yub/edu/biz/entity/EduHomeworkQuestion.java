package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 作业批改题目实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业批改题目明细
 * @Version: 1.0.0
 */
@Data
public class EduHomeworkQuestion {

    /** 主键 */
    private Long id;

    /** 批改记录ID */
    private Long correctionId;

    /** 题号 */
    private Integer questionNo;

    /** 题目内容 */
    private String questionContent;

    /** 学生答案 */
    private String studentAnswer;

    /** AI判定的正确答案 */
    private String correctAnswer;

    /** 是否正确 1:正确 0:错误 */
    private Integer isCorrect;

    /** AI给出的解析 */
    private String analysis;

    /** 来源图片URL */
    private String sourceImage;

    /** 复审状态 0:待复审 1:已复审 */
    private Integer reviewStatus;

    /** 复审结果 1:正确 0:错误（教师覆盖） */
    private Integer reviewResult;

    /** 复审答案 */
    private String reviewAnswer;

    /** 复审解析 */
    private String reviewAnalysis;

    /** 复审人ID */
    private Long reviewedBy;

    /** 复审时间 */
    private LocalDateTime reviewTime;

    /** 排序 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;
}
