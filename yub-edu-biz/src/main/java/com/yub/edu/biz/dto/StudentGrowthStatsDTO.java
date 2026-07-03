package com.yub.edu.biz.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 成长档案统计 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 成长档案聚合统计传输对象
 * @Version: 1.0.0
 */
public class StudentGrowthStatsDTO {

    /**
     * 周统计
     */
    @Data
    public static class WeekSummary {
        /** 周开始日期 */
        private LocalDate weekStartDate;
        /** 学习时长(秒) */
        private Integer studySeconds;
        /** 学习天数 */
        private Integer studyDays;
        /** 练习题数 */
        private Integer practiceCount;
        /** 练习正确数 */
        private Integer practiceCorrectCount;
        /** 考试次数 */
        private Integer examCount;
        /** 考试平均分 */
        private Integer examAvgScore;
        /** 学习知识点数 */
        private Integer knowledgeCount;
    }

    /**
     * 日统计
     */
    @Data
    public static class DaySummary {
        /** 日期 */
        private LocalDate studyDate;
        /** 学习时长(秒) */
        private Integer studySeconds;
        /** 练习题数 */
        private Integer practiceCount;
        /** 知识点数 */
        private Integer knowledgeCount;
    }

    /**
     * 学习记录
     */
    @Data
    public static class StudyRecordItem {
        /** 记录ID */
        private Long id;
        /** 课程名称 */
        private String courseName;
        /** 学习时长(分钟) */
        private Integer durationMinutes;
        /** 学习时间 */
        private String studyTime;
    }

    /**
     * 知识掌握统计
     */
    @Data
    public static class KnowledgeMastery {
        /** 知识点ID */
        private Long id;
        /** 知识点名称 */
        private String name;
        /** 课程ID */
        private Long courseId;
        /** 课程名称 */
        private String courseName;
        /** 练习数 */
        private Integer practiceCount;
        /** 正确数 */
        private Integer correctCount;
        /** 掌握度 */
        private Integer mastery;
    }

    /**
     * 推荐课程
     */
    @Data
    public static class CourseRecommend {
        /** 课程ID */
        private Long id;
        /** 课程名称 */
        private String title;
        /** 知识点 */
        private String knowledge;
        /** 章节数 */
        private Integer lessonCount;
        /** 难度 */
        private Integer difficulty;
    }
}
