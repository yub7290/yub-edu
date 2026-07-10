package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.dto.GrowthTaskStatusReqDTO;
import com.yub.edu.biz.dto.GrowthWeekPlanReqDTO;
import com.yub.edu.biz.service.StudentGrowthService;
import com.yub.edu.biz.vo.StudentGrowthVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学生端成长档案 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 学生端成长档案接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/student/growth")
@RequiredArgsConstructor
public class StudentGrowthController {

    private final StudentGrowthService studentGrowthService;

    /**
     * 首页数据
     *
     * @return 成长档案首页数据
     */
    @GetMapping("/home")
    public Response<StudentGrowthVO.HomeData> home() {
        return Response.success(studentGrowthService.getHomeData(SecurityUtils.getCurrentUserId()));
    }

    /**
     * 成长总览
     *
     * @return 成长总览
     */
    @GetMapping("/overview")
    public Response<StudentGrowthVO.Overview> overview() {
        return Response.success(studentGrowthService.getOverview(SecurityUtils.getCurrentUserId()));
    }

    /**
     * 兼容周计划
     *
     * @param weekStartDate 周开始日期
     * @return 周计划
     */
    @GetMapping("/weekly-plan")
    public Response<StudentGrowthVO.WeeklyPlan> weeklyPlan(@RequestParam(name = "weekStartDate", required = false) String weekStartDate) {
        return Response.success(studentGrowthService.getWeeklyPlan(SecurityUtils.getCurrentUserId(), weekStartDate));
    }

    /**
     * 兼容知识图谱
     *
     * @return 知识图谱
     */
    @GetMapping("/knowledge-graph")
    public Response<StudentGrowthVO.KnowledgeGraphNode> knowledgeGraph() {
        return Response.success(studentGrowthService.getKnowledgeGraph(SecurityUtils.getCurrentUserId()));
    }

    /**
     * 周报告列表
     *
     * @return 周报告列表
     */
    @GetMapping("/week-reports")
    public Response<List<StudentGrowthVO.WeekReport>> weekReports() {
        return Response.success(studentGrowthService.getWeekReports(SecurityUtils.getCurrentUserId()));
    }

    /**
     * 学科列表
     *
     * @return 学科列表
     */
    @GetMapping("/subjects")
    public Response<List<StudentGrowthVO.SubjectItem>> subjects() {
        return Response.success(studentGrowthService.getSubjects(SecurityUtils.getCurrentUserId()));
    }

    /**
     * 学科图谱
     *
     * @param subject 学科标识
     * @return 学科图谱
     */
    @GetMapping("/subject-graph")
    public Response<StudentGrowthVO.SubjectGraph> subjectGraph(@RequestParam(name = "subject", required = false) String subject) {
        return Response.success(studentGrowthService.getSubjectGraph(SecurityUtils.getCurrentUserId(), subject));
    }

    /**
     * 周计划详情
     *
     * @param weekIndex 周索引
     * @return 周计划详情
     */
    @GetMapping("/week-plan")
    public Response<StudentGrowthVO.WeekPlanDetail> weekPlan(@RequestParam(name = "weekIndex", required = false) Integer weekIndex) {
        return Response.success(studentGrowthService.getWeekPlanDetail(SecurityUtils.getCurrentUserId(), weekIndex));
    }

    /**
     * 重新生成周计划
     *
     * @param weekIndex 周索引
     * @return 周计划详情
     */
    @PostMapping("/week-plan/regenerate")
    public Response<StudentGrowthVO.WeekPlanDetail> regenerateWeekPlan(@RequestBody(required = false) GrowthWeekPlanReqDTO dto,
                                                                       @RequestParam(name = "weekIndex", required = false) Integer weekIndex) {
        Integer safeWeekIndex = dto != null && dto.getWeekIndex() != null ? dto.getWeekIndex() : weekIndex;
        return Response.success(studentGrowthService.regenerateWeekPlan(SecurityUtils.getCurrentUserId(), safeWeekIndex));
    }

    /**
     * 更新任务状态
     *
     * @param taskId    任务ID
     * @param completed 是否完成
     * @return 任务状态
     */
    @PutMapping("/task/{taskId}/status")
    public Response<StudentGrowthVO.TaskStatus> updateTaskStatus(@PathVariable("taskId") Long taskId,
                                                                 @RequestBody(required = false) GrowthTaskStatusReqDTO dto,
                                                                 @RequestParam(name = "completed", required = false) Boolean completed) {
        Boolean safeCompleted = dto != null && dto.getCompleted() != null ? dto.getCompleted() : completed;
        return Response.success(studentGrowthService.updateWeeklyTaskStatus(
                SecurityUtils.getCurrentUserId(), taskId, safeCompleted));
    }

    /**
     * 周计划列表
     *
     * @return 周计划列表
     */
    @GetMapping("/week-plan/list")
    public Response<List<StudentGrowthVO.WeekPlanDetail>> weekPlanList() {
        return Response.success(studentGrowthService.getWeekPlanDetailList(SecurityUtils.getCurrentUserId()));
    }
}
