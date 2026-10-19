package com.yub.edu.biz.controller;

import com.yub.edu.biz.dto.CourseExamStatsResult;
import com.yub.edu.biz.entity.*;
import com.yub.edu.biz.service.*;
import com.yub.edu.biz.vo.*;
import com.yub.framework.security.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentCatalogRepairTest {
    private final EduCourseService courses = mock(EduCourseService.class);
    private final ChapterService chapters = mock(ChapterService.class);
    private final EduExamRecordService records = mock(EduExamRecordService.class);
    private final CourseAccessService access = mock(CourseAccessService.class);
    private final StudentCourseController controller = new StudentCourseController(courses, chapters,
            mock(TeacherService.class), mock(EduAiConfigService.class), records,
            mock(EduPracticeRecordService.class), mock(StudyRecordService.class), mock(EduExamService.class), access);

    @AfterEach void clearIdentity() { SecurityContextHolder.clearContext(); }

    private EduCourse course() {
        EduCourse course = new EduCourse(); course.setId(2L); course.setName("数学"); course.setIsFree(1); course.setStatus(1);
        return course;
    }

    @Test void anonymousBrowseDoesNotGrantLearningOrPersonalData() {
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("test", "anonymousUser",
                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));
        when(courses.studentList(0L, -1, null)).thenReturn(List.of(course()));
        when(courses.studentList(0L, -1, "王老师")).thenReturn(List.of(course()));
        when(courses.selectById(2L)).thenReturn(course());
        when(chapters.getTree(2L)).thenReturn(List.of());
        when(access.batchAccessible(null, List.of(2L))).thenReturn(Map.of());
        assertThat(controller.list(0L, -1, 1, 10).getData().getList()).hasSize(1);
        assertThat(controller.search("王老师", 0L, -1).getData().getList()).hasSize(1);
        assertThat(controller.detail(2L).getData().getAccessible()).isFalse();
        assertThatThrownBy(() -> controller.score(2L)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(SecurityUtils::getCurrentUserId).isInstanceOf(RuntimeException.class);
    }

    @Test void missingSecurityContextIsAnonymousOnlyForPublicReads() {
        assertThat(SecurityUtils.getCurrentUserIdOrNull()).isNull();
        assertThatThrownBy(SecurityUtils::getCurrentUserId).isInstanceOf(RuntimeException.class);
    }

    @Test void disabledCourseIsNotExposedThroughPublicDetail() {
        EduCourse hidden = course(); hidden.setStatus(0);
        when(courses.selectById(2L)).thenReturn(hidden);
        assertThat(controller.detail(2L).getData()).isNull();
        verifyNoInteractions(chapters);
    }

    @Test void nullableAggregateFieldsProduceZeroStatistics() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("STUDENT:7", null, List.of()));
        when(courses.selectById(2L)).thenReturn(course());
        when(chapters.getTree(2L)).thenReturn(List.of());
        when(records.selectCourseExamStats(7L, 2L)).thenReturn(new CourseExamStatsResult());
        when(records.selectByUserAndCourse(7L, 2L)).thenReturn(List.of());
        CourseScoreRespVO score = controller.score(2L).getData();
        assertThat(score.getExamPassCount()).isZero();
        assertThat(score.getExamPassRate()).isZero();
        assertThat(score.getExamAvgScore()).isZero();
        assertThat(score.getCourseName()).isEqualTo("数学");
    }
}
