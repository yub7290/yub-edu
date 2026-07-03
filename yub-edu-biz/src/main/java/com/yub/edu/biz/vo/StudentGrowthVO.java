package com.yub.edu.biz.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生成长档案响应模型
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 学生成长档案聚合响应模型
 * @Version: 1.0.0
 */
public class StudentGrowthVO {

    /**
     * 成长档案首页数据
     */
    @Data
    public static class HomeData {
        /** 学生姓名 */
        private String studentName = "";
        /** 成长值 */
        private Integer growthValue = 0;
        /** 今日统计 */
        private TodayStats todayStats = new TodayStats();
        /** 本周目标 */
        private WeekStats weekTotal = new WeekStats();
        /** 本周完成 */
        private WeekStats weekDone = new WeekStats();
        /** 今日任务 */
        private List<DailyTask> todayTasks = new ArrayList<>();
        /** 成长徽章 */
        private List<Badge> badgeList = new ArrayList<>();
        /** 亮点提示 */
        private String highlightTip = "";
    }

    /**
     * 成长总览
     */
    @Data
    public static class Overview {
        /** 学生ID */
        private Long studentId = 0L;
        /** 学生姓名 */
        private String studentName = "";
        /** 头像地址 */
        private String avatarUrl = "";
        /** 累计学习天数 */
        private Integer totalStudyDays = 0;
        /** 累计学习时长 */
        private Integer totalStudyMinutes = 0;
        /** 完成课程数 */
        private Integer completedCourseCount = 0;
        /** 考试平均分 */
        private Integer averageExamScore = 0;
        /** 能力维度 */
        private List<AbilityDimension> abilityDimensions = new ArrayList<>();
        /** 最近学习记录 */
        private List<StudyRecord> recentStudyRecords = new ArrayList<>();
    }

    /**
     * 能力维度
     */
    @Data
    public static class AbilityDimension {
        /** 维度ID */
        private Long id;
        /** 维度名称 */
        private String name = "";
        /** 得分 */
        private Integer score = 0;
        /** 满分 */
        private Integer fullScore = 100;
    }

    /**
     * 最近学习记录
     */
    @Data
    public static class StudyRecord {
        /** 记录ID */
        private Long id;
        /** 课程名称 */
        private String courseName = "";
        /** 学习时长 */
        private Integer durationMinutes = 0;
        /** 学习时间 */
        private String studyTime = "";
    }

    /**
     * 兼容周计划
     */
    @Data
    public static class WeeklyPlan {
        /** 计划ID */
        private Long id = 0L;
        /** 周开始 */
        private String weekStartDate = "";
        /** 周结束 */
        private String weekEndDate = "";
        /** 任务列表 */
        private List<WeeklyTask> tasks = new ArrayList<>();
    }

    /**
     * 兼容周任务
     */
    @Data
    public static class WeeklyTask {
        /** 任务ID */
        private Long id;
        /** 标题 */
        private String title = "";
        /** 任务类型 */
        private Integer taskType = 1;
        /** 计划分钟 */
        private Integer plannedMinutes = 0;
        /** 完成分钟 */
        private Integer completedMinutes = 0;
        /** 是否完成 */
        private Boolean completed = false;
    }

    /**
     * 兼容知识图谱树
     */
    @Data
    public static class KnowledgeGraphNode {
        /** 节点ID */
        private Long id = 0L;
        /** 节点名 */
        private String name = "";
        /** 掌握度 */
        private Integer masteryPercent = 0;
        /** 父节点ID */
        private Long parentId;
        /** 子节点 */
        private List<KnowledgeGraphNode> children = new ArrayList<>();
    }

    /**
     * 今日统计
     */
    @Data
    public static class TodayStats {
        /** 今日学习时长(分钟) */
        private Integer duration = 0;
        /** 连续学习天数 */
        private Integer streak = 0;
        /** 今日掌握知识点数 */
        private Integer knowledge = 0;
    }

    /**
     * 周统计
     */
    @Data
    public static class WeekStats {
        /** 学习时长(分钟) */
        private Integer duration = 0;
        /** 试题数 */
        private Integer questions = 0;
        /** 知识点数 */
        private Integer knowledge = 0;
    }

    /**
     * 每日任务
     */
    @Data
    public static class DailyTask {
        /** 任务ID */
        private Long id;
        /** 任务名称 */
        private String name = "";
        /** 知识点 */
        private String knowledge = "";
        /** 任务描述 */
        private String desc = "";
        /** 任务类型 */
        private String type = "course";
        /** 任务类型文本 */
        private String typeText = "课程";
        /** 是否完成 */
        private Boolean done = false;
    }

    /**
     * 成长徽章
     */
    @Data
    public static class Badge {
        /** 徽章ID */
        private Long id;
        /** 徽章名称 */
        private String name = "";
        /** 图标样式 */
        private String iconClass = "icon-shijuan";
        /** 颜色 */
        private String color = "#409EFF";
    }

    /**
     * 周报告
     */
    @Data
    public static class WeekReport {
        /** 周名称 */
        private String weekName = "";
        /** 综合分 */
        private Integer totalScore = 0;
        /** 环比差值 */
        private Integer totalDiff = 0;
        /** 总学习时长(小时) */
        private Double totalDuration = 0D;
        /** 学习天数 */
        private Integer studyDays = 0;
        /** 知识掌握偏移 */
        private Integer masteryOffset = 0;
        /** 能力列表 */
        private List<AbilityScore> abilityList = new ArrayList<>();
        /** 时长列表 */
        private List<DurationItem> durationList = new ArrayList<>();
        /** 优势总结 */
        private String advantageSummary = "";
        /** 薄弱总结 */
        private String weakSummary = "";
        /** 学习建议 */
        private String studySuggestion = "";
    }

    /**
     * 能力得分
     */
    @Data
    public static class AbilityScore {
        /** 能力名称 */
        private String name = "";
        /** 当前分 */
        private Integer current = 0;
        /** 上周分 */
        private Integer lastWeek = 0;
        /** 差值 */
        private Integer diff = 0;
    }

    /**
     * 每日时长
     */
    @Data
    public static class DurationItem {
        /** 日期标签 */
        private String day = "";
        /** 分钟数 */
        private Integer minutes = 0;
    }

    /**
     * 学科项
     */
    @Data
    public static class SubjectItem {
        /** 学科标识 */
        private String key = "";
        /** 学科名称 */
        private String name = "";
        /** 颜色 */
        private String color = "#409EFF";
    }

    /**
     * 学科图谱
     */
    @Data
    public static class SubjectGraph {
        /** 前置节点 */
        private List<KnowledgeNode> preNodes = new ArrayList<>();
        /** 核心节点 */
        private KnowledgeNode coreNode = new KnowledgeNode();
        /** 后续节点 */
        private List<KnowledgeNode> nextNodes = new ArrayList<>();
        /** 掌握良好链路 */
        private String goodChain = "";
        /** 薄弱链路 */
        private String weakChain = "";
    }

    /**
     * 知识节点
     */
    @Data
    public static class KnowledgeNode {
        /** 节点ID */
        private Long id = 0L;
        /** 节点名称 */
        private String name = "";
        /** 掌握度 */
        private Integer mastery = 0;
        /** 节点类型 */
        private String type = "knowledge";
        /** 题型掌握列表 */
        private List<QuestionType> questionTypes = new ArrayList<>();
    }

    /**
     * 题型掌握
     */
    @Data
    public static class QuestionType {
        /** 题型名称 */
        private String name = "";
        /** 掌握度 */
        private Integer mastery = 0;
    }

    /**
     * 周计划详情
     */
    @Data
    public static class WeekPlanDetail {
        /** 计划ID */
        private Long planId = 0L;
        /** 周名称 */
        private String weekName = "";
        /** 状态文本 */
        private String statusText = "";
        /** 薄弱点 */
        private String weakPoints = "";
        /** 周目标 */
        private WeekStats weekTotal = new WeekStats();
        /** 每日计划 */
        private List<DailyPlan> dailyPlanList = new ArrayList<>();
        /** 推荐课程 */
        private List<RecommendCourse> recommendCourses = new ArrayList<>();
        /** 优势总结 */
        private String advantageSummary = "";
        /** 薄弱总结 */
        private String weakSummary = "";
        /** 学习建议 */
        private String studySuggestion = "";
    }

    /**
     * 每日计划
     */
    @Data
    public static class DailyPlan {
        /** 星期 */
        private String weekday = "";
        /** 日期 */
        private String date = "";
        /** 目标学习时长 */
        private Integer durationTarget = 0;
        /** 已完成学习时长 */
        private Integer durationDone = 0;
        /** 目标试题数 */
        private Integer questionTarget = 0;
        /** 已完成试题数 */
        private Integer questionDone = 0;
        /** 目标知识点 */
        private Integer knowledgeTarget = 0;
        /** 已完成知识点 */
        private Integer knowledgeDone = 0;
        /** 任务列表 */
        private List<TaskItem> tasks = new ArrayList<>();
    }

    /**
     * 任务项
     */
    @Data
    public static class TaskItem {
        /** 任务ID */
        private Long taskId;
        /** 跳转目标ID */
        private Long targetId;
        /** 任务类型 */
        private String type = "learn";
        /** 跳转类型 */
        private String jumpType = "course";
        /** 标题 */
        private String title = "";
        /** 知识点 */
        private String knowledge = "";
        /** 学习时长 */
        private Integer duration = 0;
        /** 题目数量 */
        private Integer questionCount = 0;
        /** 是否完成 */
        private Boolean done = false;
    }

    /**
     * 任务状态
     */
    @Data
    public static class TaskStatus {
        /** 任务ID */
        private Long taskId;
        /** 是否完成 */
        private Boolean completed = false;
    }

    /**
     * 推荐课程
     */
    @Data
    public static class RecommendCourse {
        /** 课程ID */
        private Long id;
        /** 标题 */
        private String title = "";
        /** 知识点 */
        private String knowledge = "";
        /** 时长 */
        private Integer duration = 0;
        /** 课时数 */
        private Integer lessonCount = 0;
        /** 难度 */
        private String difficulty = "";
        /** 封面颜色 */
        private String coverColor = "#409EFF";
    }
}
