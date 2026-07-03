package com.yub.edu.biz.service;

import com.yub.edu.biz.vo.StudentGrowthVO;

import java.util.List;

/**
 * 学生成长档案服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 学生成长档案聚合服务
 * @Version: 1.0.0
 */
public interface StudentGrowthService {

    /**
     * 获取成长档案首页数据
     *
     * @param studentId 学员ID
     * @return 首页数据
     */
    StudentGrowthVO.HomeData getHomeData(Long studentId);

    /**
     * 获取成长总览
     *
     * @param studentId 学员ID
     * @return 成长总览
     */
    StudentGrowthVO.Overview getOverview(Long studentId);

    /**
     * 获取兼容周计划
     *
     * @param studentId      学员ID
     * @param weekStartDate 周开始日期
     * @return 周计划
     */
    StudentGrowthVO.WeeklyPlan getWeeklyPlan(Long studentId, String weekStartDate);

    /**
     * 获取兼容知识图谱树
     *
     * @param studentId 学员ID
     * @return 知识图谱
     */
    StudentGrowthVO.KnowledgeGraphNode getKnowledgeGraph(Long studentId);

    /**
     * 获取周报告列表
     *
     * @param studentId 学员ID
     * @return 周报告列表
     */
    List<StudentGrowthVO.WeekReport> getWeekReports(Long studentId);

    /**
     * 获取学科列表
     *
     * @param studentId 学员ID
     * @return 学科列表
     */
    List<StudentGrowthVO.SubjectItem> getSubjects(Long studentId);

    /**
     * 获取学科图谱
     *
     * @param studentId 学员ID
     * @param subject   学科标识
     * @return 学科图谱
     */
    StudentGrowthVO.SubjectGraph getSubjectGraph(Long studentId, String subject);

    /**
     * 获取周计划详情
     *
     * @param studentId 学员ID
     * @param weekIndex 周索引
     * @return 周计划详情
     */
    StudentGrowthVO.WeekPlanDetail getWeekPlanDetail(Long studentId, Integer weekIndex);

    /**
     * 重新生成周计划
     *
     * @param studentId 学员ID
     * @param weekIndex 周索引
     * @return 周计划详情
     */
    StudentGrowthVO.WeekPlanDetail regenerateWeekPlan(Long studentId, Integer weekIndex);

    /**
     * 更新周计划任务状态
     *
     * @param studentId 学员ID
     * @param taskId 任务ID
     * @param completed 是否完成
     * @return 任务状态
     */
    StudentGrowthVO.TaskStatus updateWeeklyTaskStatus(Long studentId, Long taskId, Boolean completed);

    /**
     * 获取周计划列表
     *
     * @param studentId 学员ID
     * @return 周计划列表
     */
    List<StudentGrowthVO.WeekPlanDetail> getWeekPlanDetailList(Long studentId);
}
