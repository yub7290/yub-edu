package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 作业批改详情 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业批改详情展示
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkCorrectionVO {

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

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 题目列表 */
    private List<HomeworkQuestionVO> questions;
}
