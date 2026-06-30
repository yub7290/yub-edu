package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试记录实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生考试记录
 * @Version: 1.0.0
 */
@Data
public class EduExamRecord {

    /** 主键 */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 试卷ID */
    private Long examId;

    /** 第几次考试 */
    private Integer attemptNo;

    /** 状态 0:进行中 1:已提交 2:超时交卷 */
    private Integer status;

    /** 实际得分 */
    private Integer score;

    /** 试卷满分 */
    private Integer totalScore;

    /** 及格分 */
    private Integer passScore;

    /** 是否及格 1:是 0:否 */
    private Integer isPass;

    /** 答题用时（秒） */
    private Integer duration;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 提交时间 */
    private LocalDateTime submitTime;

    /** 最后心跳时间 */
    private LocalDateTime heartbeatTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
