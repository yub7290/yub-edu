package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.StudentGrowthPlanDTO;
import com.yub.edu.biz.dto.StudentGrowthStatsDTO;
import com.yub.edu.biz.entity.EduStudent;
import com.yub.edu.biz.mapper.EduStudentMapper;
import com.yub.edu.biz.mapper.StudentGrowthMapper;
import com.yub.edu.biz.service.impl.StudentGrowthServiceImpl;
import com.yub.edu.biz.vo.StudentGrowthVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 学生成长档案服务测试
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 验证成长档案计划生成、持久化与任务闭环
 * @Version: 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class StudentGrowthServiceImplTest {

    /**
     * 学生ID
     */
    private static final Long STUDENT_ID = 1001L;

    /**
     * 成长档案 Mapper
     */
    @Mock
    private StudentGrowthMapper studentGrowthMapper;

    /**
     * 学员 Mapper
     */
    @Mock
    private EduStudentMapper eduStudentMapper;

    /**
     * 被测服务
     */
    private StudentGrowthServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new StudentGrowthServiceImpl(studentGrowthMapper, eduStudentMapper);
    }

    @Test
    void getWeekPlanDetailGeneratesAndPersistsPlanWhenCurrentWeekDoesNotExist() {
        when(studentGrowthMapper.selectGrowthWeekPlan(eq(STUDENT_ID), any(LocalDate.class))).thenReturn(null);
        when(studentGrowthMapper.selectDaySummaries(eq(STUDENT_ID), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(daySummaries());
        when(studentGrowthMapper.selectKnowledgeMastery(STUDENT_ID)).thenReturn(knowledgeMasteries());
        when(studentGrowthMapper.selectWeakKnowledgeCourses(eq(STUDENT_ID), anyInt())).thenReturn(recommendCourses());
        when(studentGrowthMapper.insertGrowthWeekPlan(any(StudentGrowthPlanDTO.WeekPlanRow.class))).thenAnswer(invocation -> {
            StudentGrowthPlanDTO.WeekPlanRow row = invocation.getArgument(0);
            row.setId(55L);
            return 1;
        });

        StudentGrowthVO.WeekPlanDetail detail = service.getWeekPlanDetail(STUDENT_ID, 0);

        assertThat(detail.getPlanId()).isEqualTo(55L);
        assertThat(detail.getWeekTotal().getDuration()).isGreaterThan(0);
        assertThat(detail.getDailyPlanList()).hasSize(7);
        assertThat(detail.getDailyPlanList().get(0).getTasks()).isNotEmpty();
        assertThat(detail.getRecommendCourses()).hasSize(1);
        verify(studentGrowthMapper).insertGrowthWeekPlan(any(StudentGrowthPlanDTO.WeekPlanRow.class));
        verify(studentGrowthMapper).insertGrowthPlanTasks(anyList());
    }

    @Test
    void getWeekPlanDetailReadsPersistedPlanAndDoesNotRegenerate() {
        StudentGrowthPlanDTO.WeekPlanRow plan = persistedPlan();
        when(studentGrowthMapper.selectGrowthWeekPlan(eq(STUDENT_ID), any(LocalDate.class))).thenReturn(plan);
        when(studentGrowthMapper.selectGrowthPlanTasks(plan.getId())).thenReturn(persistedTasks(plan.getId()));
        when(studentGrowthMapper.selectDaySummaries(eq(STUDENT_ID), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(daySummaries());
        when(studentGrowthMapper.selectWeakKnowledgeCourses(eq(STUDENT_ID), anyInt())).thenReturn(recommendCourses());

        StudentGrowthVO.WeekPlanDetail detail = service.getWeekPlanDetail(STUDENT_ID, 0);

        assertThat(detail.getPlanId()).isEqualTo(plan.getId());
        assertThat(detail.getWeakPoints()).isEqualTo("函数单调性");
        assertThat(detail.getDailyPlanList()).hasSize(7);
        assertThat(detail.getDailyPlanList().get(0).getTasks()).extracting(StudentGrowthVO.TaskItem::getTaskId)
                .contains(9001L);
        verify(studentGrowthMapper, never()).insertGrowthWeekPlan(any(StudentGrowthPlanDTO.WeekPlanRow.class));
        verify(studentGrowthMapper, never()).insertGrowthPlanTasks(anyList());
    }

    @Test
    void updateWeeklyTaskStatusPersistsCompletionForStudentTask() {
        when(studentGrowthMapper.updateGrowthTaskStatus(9001L, STUDENT_ID, true)).thenReturn(1);

        StudentGrowthVO.TaskStatus status = service.updateWeeklyTaskStatus(STUDENT_ID, 9001L, true);

        assertThat(status.getTaskId()).isEqualTo(9001L);
        assertThat(status.getCompleted()).isTrue();
        verify(studentGrowthMapper).updateGrowthTaskStatus(9001L, STUDENT_ID, true);
    }

    @Test
    void regenerateWeekPlanReplacesPersistedPlanForRequestedWeek() {
        when(studentGrowthMapper.selectDaySummaries(eq(STUDENT_ID), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(daySummaries());
        when(studentGrowthMapper.selectKnowledgeMastery(STUDENT_ID)).thenReturn(knowledgeMasteries());
        when(studentGrowthMapper.selectWeakKnowledgeCourses(eq(STUDENT_ID), anyInt())).thenReturn(recommendCourses());
        when(studentGrowthMapper.insertGrowthWeekPlan(any(StudentGrowthPlanDTO.WeekPlanRow.class))).thenAnswer(invocation -> {
            StudentGrowthPlanDTO.WeekPlanRow row = invocation.getArgument(0);
            row.setId(66L);
            return 1;
        });

        StudentGrowthVO.WeekPlanDetail detail = service.regenerateWeekPlan(STUDENT_ID, 0);

        assertThat(detail.getPlanId()).isEqualTo(66L);
        verify(studentGrowthMapper).deleteGrowthTasksByWeek(eq(STUDENT_ID), any(LocalDate.class));
        verify(studentGrowthMapper).deleteGrowthPlanByWeek(eq(STUDENT_ID), any(LocalDate.class));
        verify(studentGrowthMapper).insertGrowthWeekPlan(any(StudentGrowthPlanDTO.WeekPlanRow.class));
        verify(studentGrowthMapper).insertGrowthPlanTasks(anyList());
    }

    @Test
    void weekReportsContainDataDrivenSummaryFields() {
        when(studentGrowthMapper.selectWeekSummaries(eq(STUDENT_ID), any(LocalDate.class), eq(4))).thenReturn(weekSummaries());
        when(studentGrowthMapper.selectDaySummaries(eq(STUDENT_ID), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(daySummaries());
        when(studentGrowthMapper.selectKnowledgeMastery(STUDENT_ID)).thenReturn(knowledgeMasteries());

        List<StudentGrowthVO.WeekReport> reports = service.getWeekReports(STUDENT_ID);

        assertThat(reports).isNotEmpty();
        assertThat(reports.get(0).getAdvantageSummary()).contains("专注力");
        assertThat(reports.get(0).getWeakSummary()).contains("函数单调性");
        assertThat(reports.get(0).getStudySuggestion()).contains("优先");
    }

    @Test
    void subjectGraphIncludesQuestionTypeMastery() {
        when(studentGrowthMapper.selectKnowledgeMastery(STUDENT_ID)).thenReturn(knowledgeMasteries());
        when(studentGrowthMapper.selectQuestionTypeMastery(STUDENT_ID, List.of(101L, 102L, 103L))).thenReturn(questionTypeMasteries());

        StudentGrowthVO.SubjectGraph graph = service.getSubjectGraph(STUDENT_ID, "7");

        assertThat(graph.getCoreNode().getName()).isEqualTo("函数单调性");
        assertThat(graph.getCoreNode().getQuestionTypes()).extracting(StudentGrowthVO.QuestionType::getName)
                .contains("单选题", "多选题");
    }

    private List<StudentGrowthStatsDTO.DaySummary> daySummaries() {
        LocalDate monday = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        List<StudentGrowthStatsDTO.DaySummary> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            StudentGrowthStatsDTO.DaySummary summary = new StudentGrowthStatsDTO.DaySummary();
            summary.setStudyDate(monday.plusDays(i));
            summary.setStudySeconds(i == 0 ? 1800 : 0);
            summary.setPracticeCount(i == 0 ? 12 : 0);
            summary.setKnowledgeCount(i == 0 ? 2 : 0);
            days.add(summary);
        }
        return days;
    }

    private List<StudentGrowthStatsDTO.KnowledgeMastery> knowledgeMasteries() {
        StudentGrowthStatsDTO.KnowledgeMastery weak = knowledge(101L, "函数单调性", 7L, "高等数学", 10, 4, 40);
        StudentGrowthStatsDTO.KnowledgeMastery mid = knowledge(102L, "导数应用", 7L, "高等数学", 8, 6, 75);
        StudentGrowthStatsDTO.KnowledgeMastery good = knowledge(103L, "极限运算", 7L, "高等数学", 9, 8, 89);
        return List.of(weak, mid, good);
    }

    private StudentGrowthStatsDTO.KnowledgeMastery knowledge(Long id, String name, Long courseId, String courseName,
                                                             int practiceCount, int correctCount, int mastery) {
        StudentGrowthStatsDTO.KnowledgeMastery item = new StudentGrowthStatsDTO.KnowledgeMastery();
        item.setId(id);
        item.setName(name);
        item.setCourseId(courseId);
        item.setCourseName(courseName);
        item.setPracticeCount(practiceCount);
        item.setCorrectCount(correctCount);
        item.setMastery(mastery);
        return item;
    }

    private List<StudentGrowthStatsDTO.CourseRecommend> recommendCourses() {
        StudentGrowthStatsDTO.CourseRecommend course = new StudentGrowthStatsDTO.CourseRecommend();
        course.setId(7L);
        course.setTitle("高等数学专项课");
        course.setKnowledge("函数单调性");
        course.setLessonCount(6);
        course.setDifficulty(2);
        return List.of(course);
    }

    private StudentGrowthPlanDTO.WeekPlanRow persistedPlan() {
        StudentGrowthPlanDTO.WeekPlanRow plan = new StudentGrowthPlanDTO.WeekPlanRow();
        plan.setId(88L);
        plan.setStudentId(STUDENT_ID);
        plan.setWeekStartDate(LocalDate.now().with(java.time.DayOfWeek.MONDAY));
        plan.setWeekEndDate(plan.getWeekStartDate().plusDays(6));
        plan.setStatus(1);
        plan.setWeakPoints("函数单调性");
        plan.setAdvantageSummary("专注力稳定");
        plan.setWeakSummary("函数单调性掌握不足");
        plan.setStudySuggestion("优先复习函数单调性");
        plan.setDurationTarget(210);
        plan.setQuestionTarget(70);
        plan.setKnowledgeTarget(5);
        return plan;
    }

    private List<StudentGrowthPlanDTO.TaskRow> persistedTasks(Long planId) {
        StudentGrowthPlanDTO.TaskRow task = new StudentGrowthPlanDTO.TaskRow();
        task.setId(9001L);
        task.setPlanId(planId);
        task.setStudentId(STUDENT_ID);
        task.setPlanDate(LocalDate.now().with(java.time.DayOfWeek.MONDAY));
        task.setTaskType("learn");
        task.setJumpType("course");
        task.setTitle("函数单调性专项学习");
        task.setKnowledgePointId(101L);
        task.setKnowledgeName("函数单调性");
        task.setTargetId(7L);
        task.setDurationTarget(30);
        task.setQuestionTarget(0);
        task.setCompleted(false);
        return List.of(task);
    }

    private List<StudentGrowthStatsDTO.WeekSummary> weekSummaries() {
        StudentGrowthStatsDTO.WeekSummary current = new StudentGrowthStatsDTO.WeekSummary();
        current.setWeekStartDate(LocalDate.now().with(java.time.DayOfWeek.MONDAY));
        current.setStudySeconds(18000);
        current.setStudyDays(5);
        current.setPracticeCount(40);
        current.setPracticeCorrectCount(30);
        current.setExamCount(1);
        current.setExamAvgScore(82);
        current.setKnowledgeCount(6);
        StudentGrowthStatsDTO.WeekSummary previous = new StudentGrowthStatsDTO.WeekSummary();
        previous.setWeekStartDate(current.getWeekStartDate().minusWeeks(1));
        previous.setStudySeconds(7200);
        previous.setStudyDays(3);
        previous.setPracticeCount(20);
        previous.setPracticeCorrectCount(12);
        previous.setExamCount(1);
        previous.setExamAvgScore(75);
        previous.setKnowledgeCount(4);
        return List.of(current, previous);
    }

    private List<StudentGrowthPlanDTO.QuestionTypeMastery> questionTypeMasteries() {
        StudentGrowthPlanDTO.QuestionTypeMastery single = new StudentGrowthPlanDTO.QuestionTypeMastery();
        single.setKnowledgePointId(101L);
        single.setQuestionType(0);
        single.setPracticeCount(5);
        single.setCorrectCount(2);
        single.setMastery(40);
        StudentGrowthPlanDTO.QuestionTypeMastery multi = new StudentGrowthPlanDTO.QuestionTypeMastery();
        multi.setKnowledgePointId(101L);
        multi.setQuestionType(1);
        multi.setPracticeCount(5);
        multi.setCorrectCount(2);
        multi.setMastery(40);
        return List.of(single, multi);
    }
}
