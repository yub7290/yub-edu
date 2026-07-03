package com.yub.edu.biz.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生成长档案计划 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 成长档案周计划与任务持久化传输对象
 * @Version: 1.0.0
 */
public class StudentGrowthPlanDTO {

    /**
     * 周计划行
     */
    @Data
    public static class WeekPlanRow {
        /** 主键 */
        private Long id;
        /** 学员ID */
        private Long studentId;
        /** 周开始日期 */
        private LocalDate weekStartDate;
        /** 周结束日期 */
        private LocalDate weekEndDate;
        /** 状态：1进行中 2已完成 3历史 */
        private Integer status;
        /** 薄弱知识点 */
        private String weakPoints;
        /** 优势总结 */
        private String advantageSummary;
        /** 薄弱总结 */
        private String weakSummary;
        /** 学习建议 */
        private String studySuggestion;
        /** 学习时长目标 */
        private Integer durationTarget;
        /** 练习题目目标 */
        private Integer questionTarget;
        /** 知识点目标 */
        private Integer knowledgeTarget;
        /** 创建时间 */
        private LocalDateTime createTime;
        /** 更新时间 */
        private LocalDateTime updateTime;
    }

    /**
     * 计划任务行
     */
    @Data
    public static class TaskRow {
        /** 主键 */
        private Long id;
        /** 周计划ID */
        private Long planId;
        /** 学员ID */
        private Long studentId;
        /** 计划日期 */
        private LocalDate planDate;
        /** 任务类型 */
        private String taskType;
        /** 跳转类型 */
        private String jumpType;
        /** 任务标题 */
        private String title;
        /** 知识点ID */
        private Long knowledgePointId;
        /** 知识点名称 */
        private String knowledgeName;
        /** 跳转目标ID */
        private Long targetId;
        /** 学习时长目标 */
        private Integer durationTarget;
        /** 题目数量目标 */
        private Integer questionTarget;
        /** 是否完成 */
        private Boolean completed;
        /** 完成时间 */
        private LocalDateTime completeTime;
        /** 创建时间 */
        private LocalDateTime createTime;
        /** 更新时间 */
        private LocalDateTime updateTime;
    }

    /**
     * 题型掌握度
     */
    @Data
    public static class QuestionTypeMastery {
        /** 知识点ID */
        private Long knowledgePointId;
        /** 题型 */
        private Integer questionType;
        /** 练习数 */
        private Integer practiceCount;
        /** 正确数 */
        private Integer correctCount;
        /** 掌握度 */
        private Integer mastery;
    }
}
