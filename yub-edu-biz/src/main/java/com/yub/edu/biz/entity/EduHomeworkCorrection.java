package com.yub.edu.biz.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 作业批改记录实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业批改记录
 * @Version: 1.0.0
 */
@Data
public class EduHomeworkCorrection {

    /** 主键 */
    private Long id;

    /** 课程ID */
    private Long courseId;

    /** 学生ID */
    private Long studentId;

    /** 图片URL，多个用逗号分隔 */
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

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;

    /** 课程名称（非持久化） */
    private transient String courseName;

    /** 学生名称（非持久化） */
    private transient String studentName;
}
