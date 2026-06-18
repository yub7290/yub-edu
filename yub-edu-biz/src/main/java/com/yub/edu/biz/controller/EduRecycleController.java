package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.entity.EduQuestion;
import com.yub.edu.biz.service.RecycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 回收站 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 回收站管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/recycle")
@RequiredArgsConstructor
public class EduRecycleController {

    private final RecycleService recycleService;

    // ========== 专业回收 ==========

    @GetMapping("/major")
    public Response<List<EduMajor>> majorList() {
        return Response.success(recycleService.getMajorList());
    }

    @PutMapping("/major/{id}/restore")
    public Response<Void> majorRestore(@PathVariable Long id) {
        recycleService.restoreMajor(id);
        return Response.success();
    }

    @DeleteMapping("/major/{id}")
    public Response<Void> majorDelete(@PathVariable Long id) {
        recycleService.deleteMajor(id);
        return Response.success();
    }

    // ========== 课程回收 ==========

    @GetMapping("/course")
    public Response<List<EduCourse>> courseList() {
        return Response.success(recycleService.getCourseList());
    }

    @PutMapping("/course/{id}/restore")
    public Response<Void> courseRestore(@PathVariable Long id) {
        recycleService.restoreCourse(id);
        return Response.success();
    }

    @DeleteMapping("/course/{id}")
    public Response<Void> courseDelete(@PathVariable Long id) {
        recycleService.deleteCourse(id);
        return Response.success();
    }

    // ========== 试题回收 ==========

    @GetMapping("/question")
    public Response<List<EduQuestion>> questionList() {
        return Response.success(recycleService.getQuestionList());
    }

    @PutMapping("/question/{id}/restore")
    public Response<Void> questionRestore(@PathVariable Long id) {
        recycleService.restoreQuestion(id);
        return Response.success();
    }

    @DeleteMapping("/question/{id}")
    public Response<Void> questionDelete(@PathVariable Long id) {
        recycleService.deleteQuestion(id);
        return Response.success();
    }

    // ========== 试卷回收 ==========

    @GetMapping("/exam")
    public Response<List<EduExam>> examList() {
        return Response.success(recycleService.getExamList());
    }

    @PutMapping("/exam/{id}/restore")
    public Response<Void> examRestore(@PathVariable Long id) {
        recycleService.restoreExam(id);
        return Response.success();
    }

    @DeleteMapping("/exam/{id}")
    public Response<Void> examDelete(@PathVariable Long id) {
        recycleService.deleteExam(id);
        return Response.success();
    }
}
