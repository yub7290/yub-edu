package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 作业分页 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业分页列表展示
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkPageVO {

    /** 批改记录ID */
    private Long id;

    /** 课程ID */
    private Long courseId;

    /** 课程名称 */
    private String courseName;

    /** 学生ID */
    private Long studentId;

    /** 学生名称 */
    private String studentName;

    /** 图片URL */
    private String images;

    /** 题目总数 */
    private Integer totalQuestions;

    /** 正确题数 */
    private Integer correctCount;

    /** 得分 */
    private BigDecimal score;

    /** 状态 0:批改中 1:已完成 */
    private Integer status;

    /** 人工复查状态 0:未复查 1:复查中 2:已复查 */
    private Integer reviewStatus;

    /** 创建时间 */
    private LocalDateTime createTime;
}
