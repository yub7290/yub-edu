package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 作业批改题目 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业批改题目展示
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkQuestionVO {

    /** 题目ID */
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

    /**
     * 展示答案：有复审结果则用复审答案，否则用AI判定答案
     *
     * @return 展示答案
     */
    public String getDisplayAnswer() {
        if (reviewStatus != null && reviewStatus == 1 && reviewAnswer != null) {
            return reviewAnswer;
        }
        return correctAnswer;
    }

    /**
     * 展示解析：有复审结果则用复审解析，否则用AI判定解析
     *
     * @return 展示解析
     */
    public String getDisplayAnalysis() {
        if (reviewStatus != null && reviewStatus == 1 && reviewAnalysis != null) {
            return reviewAnalysis;
        }
        return analysis;
    }
}
