package com.yub.edu.biz.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.entity.EduTeacher;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduTeacherMapper;
import com.yub.edu.biz.service.EduCourseService;
import com.yub.edu.biz.vo.CourseCategoryRespVO;
import com.yub.edu.biz.vo.CourseListRespVO;
import com.yub.edu.biz.vo.CourseRecommendedRespVO;
import com.yub.edu.biz.vo.StudentCourseDetailRespVO;
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
    private final EduTeacherMapper eduTeacherMapper;
    private final EduCourseService eduCourseService;

    /**
     * 课程分类
     */
    @GetMapping("/category")
    public Response<CourseCategoryRespVO> category() {
        return Response.success(CourseCategoryRespVO.builder().list(new ArrayList<>()).build());
    }

    /**
     * 课程列表（分页）
     */
    @GetMapping("/list")
    public Response<CourseListRespVO> list(
            @RequestParam(defaultValue = "0") Long cateId,
            @RequestParam(defaultValue = "0") Integer tabType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<EduCourse> list = eduCourseService.studentList(cateId, tabType, null);
        PageInfo<EduCourse> pageInfo = new PageInfo<>(list);
        List<CourseRecommendedRespVO> voList = list.stream().map(c ->
            CourseRecommendedRespVO.builder()
                    .id(c.getId())
                    .imageUrl(c.getImageUrl())
                    .name(c.getName())
                    .courseType(c.getCourseType())
                    .teacherName(c.getTeacher())
                    .build()
        ).toList();
        return Response.success(CourseListRespVO.builder().list(voList).total(pageInfo.getTotal()).build());
    }

    /**
     * 课程搜索
     */
    @GetMapping("/search")
    public Response<CourseListRespVO> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Long cateId,
            @RequestParam(defaultValue = "0") Integer tabType) {
        List<EduCourse> list = eduCourseService.studentList(cateId, tabType, keyword);
        List<CourseRecommendedRespVO> voList = list.stream().map(c ->
            CourseRecommendedRespVO.builder()
                    .id(c.getId())
                    .imageUrl(c.getImageUrl())
                    .name(c.getName())
                    .courseType(c.getCourseType())
                    .teacherName(c.getTeacher())
                    .build()
        ).toList();
        return Response.success(CourseListRespVO.builder().list(voList).total(list.size()).build());
    }

    /**
     * 课程详情
     */
    @GetMapping("/detail")
    public Response<StudentCourseDetailRespVO> detail(@RequestParam("cid") Long cid) {
        EduCourse course = eduCourseMapper.selectById(cid);
        if (course == null) {
            return Response.success(null);
        }

        // 课程信息
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("title", course.getName());
        courseInfo.put("bannerImg", course.getImageUrl() != null ? course.getImageUrl() : "");
        courseInfo.put("viewNum", course.getViewCount() != null ? course.getViewCount() : 0);
        courseInfo.put("freeFlag", course.getIsFree() != null && course.getIsFree() == 1);
        courseInfo.put("desc", course.getIntroduction() != null ? course.getIntroduction() : "");
        courseInfo.put("target", course.getLearningObjectives() != null ? course.getLearningObjectives() : "");

        // 章节列表
        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(cid);
        List<Map<String, Object>> chapterList = new ArrayList<>();
        for (EduChapter ch : chapters) {
            Map<String, Object> chMap = new HashMap<>();
            chMap.put("id", ch.getId());
            chMap.put("name", ch.getName());
            chapterList.add(chMap);
        }

        // 教师信息
        Map<String, Object> teacherInfo = new HashMap<>();
        String teacherName = course.getTeacher();
        if (teacherName != null && !teacherName.isEmpty()) {
            EduTeacher teacher = eduTeacherMapper.selectByName(teacherName);
            if (teacher != null) {
                teacherInfo.put("avatar", teacher.getAvatarUrl() != null ? teacher.getAvatarUrl() : "");
                teacherInfo.put("name", teacher.getName() != null ? teacher.getName() : "");
                teacherInfo.put("intro", teacher.getSignature() != null ? teacher.getSignature() : "");
            } else {
                teacherInfo.put("avatar", "");
                teacherInfo.put("name", teacherName);
                teacherInfo.put("intro", "");
            }
        } else {
            teacherInfo.put("avatar", "");
            teacherInfo.put("name", "");
            teacherInfo.put("intro", "");
        }

        return Response.success(StudentCourseDetailRespVO.builder()
                .course(courseInfo)
                .chapter(chapterList)
                .teacher(teacherInfo)
                .build());
    }
}
