package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 考试历史成绩 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生端考试历史成绩记录
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamHistoryRespVO {

    /** 考试记录ID */
    private Long recordId;

    /** 实际得分 */
    private Integer score;

    /** 试卷满分 */
    private Integer totalScore;

    /** 及格分 */
    private Integer passScore;

    /** 是否及格 1:是 0:否 */
    private Integer isPass;

    /** 提交时间 */
    private LocalDateTime submitTime;
}
