package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.StudentGrowthPlanDTO;
import com.yub.edu.biz.dto.StudentGrowthStatsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 成长档案 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 成长档案聚合查询数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface StudentGrowthMapper {

    /**
     * 查询总学习时长分钟
     *
     * @param studentId 学员ID
     * @return 分钟数
     */
    Integer selectTotalStudyMinutes(@Param("studentId") Long studentId);

    /**
     * 查询学习天数
     *
     * @param studentId 学员ID
     * @return 学习天数
     */
    Integer selectStudyDays(@Param("studentId") Long studentId);

    /**
     * 查询完成课程数
     *
     * @param studentId 学员ID
     * @return 完成课程数
     */
    Integer selectCompletedCourseCount(@Param("studentId") Long studentId);

    /**
     * 查询平均考试分
     *
     * @param studentId 学员ID
     * @return 平均分
     */
    Integer selectAverageExamScore(@Param("studentId") Long studentId);

    /**
     * 查询指定日期学习时长秒
     *
     * @param studentId 学员ID
     * @param date      日期
     * @return 秒数
     */
    Integer selectStudySecondsByDate(@Param("studentId") Long studentId, @Param("date") LocalDate date);

    /**
     * 查询连续学习天数
     *
     * @param studentId 学员ID
     * @return 连续天数
     */
    Integer selectStudyStreak(@Param("studentId") Long studentId);

    /**
     * 查询指定日期掌握知识点数
     *
     * @param studentId 学员ID
     * @param date      日期
     * @return 知识点数
     */
    Integer selectKnowledgeCountByDate(@Param("studentId") Long studentId, @Param("date") LocalDate date);

    /**
     * 查询最近学习记录
     *
     * @param studentId 学员ID
     * @param limit     条数
     * @return 学习记录
     */
    List<StudentGrowthStatsDTO.StudyRecordItem> selectRecentStudyRecords(@Param("studentId") Long studentId,
                                                                          @Param("limit") Integer limit);

    /**
     * 查询周统计
     *
     * @param studentId 学员ID
     * @param fromDate  开始日期
     * @param weekCount 周数
     * @return 周统计列表
     */
    List<StudentGrowthStatsDTO.WeekSummary> selectWeekSummaries(@Param("studentId") Long studentId,
                                                                 @Param("fromDate") LocalDate fromDate,
                                                                 @Param("weekCount") Integer weekCount);

    /**
     * 查询日统计
     *
     * @param studentId 学员ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 日统计列表
     */
    List<StudentGrowthStatsDTO.DaySummary> selectDaySummaries(@Param("studentId") Long studentId,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);

    /**
     * 查询知识掌握度
     *
     * @param studentId 学员ID
     * @return 知识掌握列表
     */
    List<StudentGrowthStatsDTO.KnowledgeMastery> selectKnowledgeMastery(@Param("studentId") Long studentId);

    /**
     * 查询薄弱知识点推荐课程
     *
     * @param studentId 学员ID
     * @param limit     条数
     * @return 推荐课程
     */
    List<StudentGrowthStatsDTO.CourseRecommend> selectWeakKnowledgeCourses(@Param("studentId") Long studentId,
                                                                            @Param("limit") Integer limit);

    /**
     * 查询成长档案周计划
     *
     * @param studentId 学员ID
     * @param weekStartDate 周开始日期
     * @return 周计划
     */
    StudentGrowthPlanDTO.WeekPlanRow selectGrowthWeekPlan(@Param("studentId") Long studentId,
                                                           @Param("weekStartDate") LocalDate weekStartDate);

    /**
     * 新增成长档案周计划
     *
     * @param plan 周计划
     * @return 影响行数
     */
    int insertGrowthWeekPlan(StudentGrowthPlanDTO.WeekPlanRow plan);

    /**
     * 删除指定周计划
     *
     * @param studentId 学员ID
     * @param weekStartDate 周开始日期
     * @return 影响行数
     */
    int deleteGrowthPlanByWeek(@Param("studentId") Long studentId,
                               @Param("weekStartDate") LocalDate weekStartDate);

    /**
     * 删除指定周计划任务
     *
     * @param studentId 学员ID
     * @param weekStartDate 周开始日期
     * @return 影响行数
     */
    int deleteGrowthTasksByWeek(@Param("studentId") Long studentId,
                                @Param("weekStartDate") LocalDate weekStartDate);

    /**
     * 查询计划任务
     *
     * @param planId 计划ID
     * @return 任务列表
     */
    List<StudentGrowthPlanDTO.TaskRow> selectGrowthPlanTasks(@Param("planId") Long planId);

    /**
     * 批量新增计划任务
     *
     * @param tasks 任务列表
     * @return 影响行数
     */
    int insertGrowthPlanTasks(@Param("tasks") List<StudentGrowthPlanDTO.TaskRow> tasks);

    /**
     * 更新任务状态
     *
     * @param taskId 任务ID
     * @param studentId 学员ID
     * @param completed 是否完成
     * @return 影响行数
     */
    int updateGrowthTaskStatus(@Param("taskId") Long taskId,
                               @Param("studentId") Long studentId,
                               @Param("completed") Boolean completed);

    /**
     * 查询题型掌握度
     *
     * @param studentId 学员ID
     * @param knowledgePointIds 知识点ID列表
     * @return 题型掌握度列表
     */
    List<StudentGrowthPlanDTO.QuestionTypeMastery> selectQuestionTypeMastery(
            @Param("studentId") Long studentId,
            @Param("knowledgePointIds") List<Long> knowledgePointIds);
}
