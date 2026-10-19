package com.yub.edu.biz.controller;

import com.yub.edu.biz.entity.*;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.service.*;
import com.yub.edu.biz.vo.CourseRecommendedRespVO;
import com.yub.system.service.banner.SysBannerService;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentTeacherRepairTest {
    @Test void exposesOnlySelectedTeachersPublicFieldsAndActualCourses() {
        TeacherService teachers = mock(TeacherService.class);
        EduCourseService courses = mock(EduCourseService.class);
        CourseAccessService access = mock(CourseAccessService.class);
        StudentHomeController home = new StudentHomeController(courses, teachers, mock(SysBannerService.class),
                mock(EduTeacherTitleService.class), access);
        EduTeacher teacher = new EduTeacher(); teacher.setId(3L); teacher.setName("测试老师1"); teacher.setBio("真实简介");
        when(teachers.selectStudentById(3L)).thenReturn(teacher);
        EduCourse course = new EduCourse(); course.setId(27L); course.setName("真实课程");
        when(courses.studentTeacherCourses(3L, "测试老师1")).thenReturn(List.of(course));
        when(access.batchAccessible(null, List.of(27L))).thenReturn(Map.of());
        var result = home.teacherInfo(3L).getData();
        assertThat(((Map<?, ?>) result.getInfo()).containsKey("name")).isTrue();
        assertThat(((Map<?, ?>) result.getInfo()).get("name")).isEqualTo("测试老师1");
        assertThat(((Map<?, ?>) result.getInfo()).keySet()).noneMatch(key ->
                List.of("password", "phone", "idCard", "account", "address").contains(key));
        assertThat(((CourseRecommendedRespVO) result.getCourse().get(0)).getId()).isEqualTo(27L);
        assertThatThrownBy(() -> home.teacherInfo(999L)).isInstanceOf(EduException.class);
        verify(courses, never()).studentTeacherCourses(eq(999L), anyString());
    }
}
