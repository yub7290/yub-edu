package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.entity.EduQuestion;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduExamMapper;
import com.yub.edu.biz.mapper.EduMajorMapper;
import com.yub.edu.biz.mapper.EduQuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
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

    private final EduMajorMapper eduMajorMapper;
    private final EduCourseMapper eduCourseMapper;
    private final EduQuestionMapper eduQuestionMapper;
    private final EduExamMapper eduExamMapper;

    // ========== 专业回收 ==========

    @GetMapping("/major")
    public Response<List<EduMajor>> majorList() {
        return Response.success(eduMajorMapper.selectRecycleList());
    }

    @PutMapping("/major/{id}/restore")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> majorRestore(@PathVariable Long id) {
        eduMajorMapper.restoreById(id);
        return Response.success();
    }

    @DeleteMapping("/major/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> majorDelete(@PathVariable Long id) {
        eduMajorMapper.deletePhysicallyById(id);
        return Response.success();
    }

    // ========== 课程回收 ==========

    @GetMapping("/course")
    public Response<List<EduCourse>> courseList() {
        return Response.success(eduCourseMapper.selectRecycleList());
    }

    @PutMapping("/course/{id}/restore")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> courseRestore(@PathVariable Long id) {
        eduCourseMapper.restoreById(id);
        return Response.success();
    }

    @DeleteMapping("/course/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> courseDelete(@PathVariable Long id) {
        eduCourseMapper.deletePhysicallyById(id);
        return Response.success();
    }

    // ========== 试题回收 ==========

    @GetMapping("/question")
    public Response<List<EduQuestion>> questionList() {
        return Response.success(eduQuestionMapper.selectRecycleList());
    }

    @PutMapping("/question/{id}/restore")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> questionRestore(@PathVariable Long id) {
        eduQuestionMapper.restoreById(id);
        return Response.success();
    }

    @DeleteMapping("/question/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> questionDelete(@PathVariable Long id) {
        eduQuestionMapper.deletePhysicallyById(id);
        return Response.success();
    }

    // ========== 试卷回收 ==========

    @GetMapping("/exam")
    public Response<List<EduExam>> examList() {
        return Response.success(eduExamMapper.selectRecycleList());
    }

    @PutMapping("/exam/{id}/restore")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> examRestore(@PathVariable Long id) {
        eduExamMapper.restoreById(id);
        return Response.success();
    }

    @DeleteMapping("/exam/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> examDelete(@PathVariable Long id) {
        eduExamMapper.deletePhysicallyById(id);
        return Response.success();
    }
}
