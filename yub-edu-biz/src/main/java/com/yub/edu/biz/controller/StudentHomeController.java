package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.service.EduCourseService;
import com.yub.edu.biz.service.TeacherService;
import com.yub.edu.biz.vo.CourseOverviewRespVO;
import com.yub.edu.biz.vo.TeacherOptionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 学生端首页 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端首页接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/student/home")
@RequiredArgsConstructor
public class StudentHomeController {

    private final EduCourseService eduCourseService;
    private final TeacherService teacherService;

    /**
     * 首页基础数据（Logo、Banner、导航）
     */
    @GetMapping("/base")
    public Response<Map<String, Object>> base() {
        Map<String, Object> data = new HashMap<>();
        data.put("logo", "/static/logo.png");
        data.put("banner", new ArrayList<>());
        data.put("nav", Arrays.asList(
            Map.of("img", "/static/nav/course.png", "name", "全部课程", "link", "/pages/course/course"),
            Map.of("img", "/static/nav/exam.png", "name", "在线考试", "link", "/pages/exam/exam"),
            Map.of("img", "/static/nav/study.png", "name", "我的学习", "link", "/pages/mine/mine"),
            Map.of("img", "/static/nav/teacher.png", "name", "名师风采", "link", "")
        ));
        return Response.success(data);
    }

    /**
     * 推荐教师列表
     */
    @GetMapping("/teacher")
    public Response<Map<String, Object>> teacher() {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> teacherList = new ArrayList<>();
        // TODO: 从数据库查询推荐教师，当前返回空列表
        data.put("list", teacherList);
        return Response.success(data);
    }

    /**
     * 推荐课程列表
     */
    @GetMapping("/course")
    public Response<Map<String, Object>> course() {
        Map<String, Object> data = new HashMap<>();
        // TODO: 查询推荐课程（recommended=1），当前返回空列表
        data.put("list", new ArrayList<>());
        return Response.success(data);
    }

    /**
     * 搜索课程
     */
    @GetMapping("/search")
    public Response<Map<String, Object>> search(@RequestParam(required = false) String keyword) {
        Map<String, Object> data = new HashMap<>();
        // TODO: 根据关键词搜索课程，当前返回空列表
        data.put("list", new ArrayList<>());
        return Response.success(data);
    }

    /**
     * 教师详情
     */
    @GetMapping("/teacher/info")
    public Response<Map<String, Object>> teacherInfo(@RequestParam Long tid) {
        Map<String, Object> data = new HashMap<>();
        // TODO: 查询教师详情，当前返回空
        data.put("info", new HashMap<>());
        data.put("course", new ArrayList<>());
        return Response.success(data);
    }
}
