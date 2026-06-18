package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduChapterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 学生端课程 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端课程接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/student/course")
@RequiredArgsConstructor
public class StudentCourseController {

    private final EduCourseMapper eduCourseMapper;
    private final EduChapterMapper eduChapterMapper;

    /**
     * 课程分类
     */
    @GetMapping("/category")
    public Response<Map<String, Object>> category() {
        Map<String, Object> data = new HashMap<>();
        data.put("list", new ArrayList<>());
        return Response.success(data);
    }

    /**
     * 课程列表（分页）
     */
    @GetMapping("/list")
    public Response<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") Long cateId,
            @RequestParam(defaultValue = "0") Integer tabType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> data = new HashMap<>();
        // TODO: 实现分页查询，当前返回空列表
        data.put("list", new ArrayList<>());
        return Response.success(data);
    }

    /**
     * 课程搜索
     */
    @GetMapping("/search")
    public Response<Map<String, Object>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Long cateId,
            @RequestParam(defaultValue = "0") Integer tabType) {
        Map<String, Object> data = new HashMap<>();
        // TODO: 实现搜索，当前返回空列表
        data.put("list", new ArrayList<>());
        return Response.success(data);
    }

    /**
     * 课程详情
     */
    @GetMapping("/detail")
    public Response<Map<String, Object>> detail(@RequestParam Long cid) {
        EduCourse course = eduCourseMapper.selectById(cid);
        if (course == null) {
            return Response.success(null);
        }

        Map<String, Object> data = new HashMap<>();

        // 课程信息
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("title", course.getName());
        courseInfo.put("bannerImg", course.getImageUrl() != null ? course.getImageUrl() : "");
        courseInfo.put("viewNum", course.getViewCount() != null ? course.getViewCount() : 0);
        courseInfo.put("freeFlag", course.getIsFree() != null && course.getIsFree() == 1);
        courseInfo.put("desc", course.getIntroduction() != null ? course.getIntroduction() : "");
        courseInfo.put("target", course.getLearningObjectives() != null ? course.getLearningObjectives() : "");
        data.put("course", courseInfo);

        // 章节列表
        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(cid);
        List<Map<String, Object>> chapterList = new ArrayList<>();
        for (int i = 0; i < chapters.size(); i++) {
            EduChapter ch = chapters.get(i);
            Map<String, Object> chMap = new HashMap<>();
            chMap.put("id", ch.getId());
            chMap.put("name", ch.getName());
            chapterList.add(chMap);
        }
        data.put("chapter", chapterList);

        // 教师信息
        Map<String, Object> teacherInfo = new HashMap<>();
        teacherInfo.put("avatar", "");
        teacherInfo.put("name", course.getTeacher() != null ? course.getTeacher() : "");
        teacherInfo.put("intro", "");
        data.put("teacher", teacherInfo);

        return Response.success(data);
    }
}
