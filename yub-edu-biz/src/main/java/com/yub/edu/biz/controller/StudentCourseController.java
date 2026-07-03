package com.yub.edu.biz.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.CourseExamStatsResult;
import com.yub.edu.biz.entity.EduAiConfig;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.entity.EduExamRecord;
import com.yub.edu.biz.entity.EduTeacher;
import com.yub.edu.biz.mapper.EduAiConfigMapper;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduExamMapper;
import com.yub.edu.biz.mapper.EduExamRecordMapper;
import com.yub.edu.biz.mapper.EduPracticeRecordMapper;
import com.yub.edu.biz.mapper.EduTeacherMapper;
import com.yub.edu.biz.mapper.StudyRecordMapper;
import com.yub.edu.biz.service.EduCourseService;
import com.yub.edu.biz.vo.CourseCategoryRespVO;
import com.yub.edu.biz.vo.CourseExamHistoryVO;
import com.yub.edu.biz.vo.CourseListRespVO;
import com.yub.edu.biz.vo.CourseRecommendedRespVO;
import com.yub.edu.biz.vo.CourseScoreRespVO;
import com.yub.edu.biz.vo.StudentCourseDetailRespVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    private final EduAiConfigMapper eduAiConfigMapper;
    private final EduExamRecordMapper eduExamRecordMapper;
    private final EduPracticeRecordMapper eduPracticeRecordMapper;
    private final StudyRecordMapper studyRecordMapper;
    private final EduExamMapper eduExamMapper;

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
            @RequestParam(name = "cateId", defaultValue = "0") Long cateId,
            @RequestParam(name = "tabType", defaultValue = "0") Integer tabType,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
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
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "cateId", defaultValue = "0") Long cateId,
            @RequestParam(name = "tabType", defaultValue = "0") Integer tabType) {
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

        // AI助教信息
        Map<String, Object> aiAssistant = new HashMap<>();
        EduAiConfig aiConfig = eduAiConfigMapper.selectByCourseId(cid);
        boolean aiEnabled = aiConfig != null && aiConfig.getEnabled() != null && aiConfig.getEnabled() == 1;
        aiAssistant.put("enabled", aiEnabled);
        // 默认系统提示词也返回前端，方便展示AI助教角色

        return Response.success(StudentCourseDetailRespVO.builder()
                .course(courseInfo)
                .chapter(chapterList)
                .teacher(teacherInfo)
                .aiAssistant(aiAssistant)
                .build());
    }

    /**
     * 课程综合成绩
     *
     * @param courseId 课程ID
     * @return 综合成绩数据
     */
    @GetMapping("/{courseId}/score")
    public Response<CourseScoreRespVO> score(@PathVariable Long courseId) {
        Long userId = SecurityUtils.getCurrentUserId();

        // 课程信息
        EduCourse course = eduCourseMapper.selectById(courseId);
        if (course == null) {
            return Response.success(null);
        }

        // 1. 考试统计
        CourseExamStatsResult examStats = eduExamRecordMapper.selectCourseExamStats(userId, courseId);
        int examAvgScore = examStats != null ? examStats.getAvgScore() : 0;
        int examMaxScore = examStats != null ? examStats.getMaxScore() : 0;
        int examTotalCount = examStats != null ? examStats.getTotalCount() : 0;
        int examPassCount = examStats != null ? examStats.getPassCount() : 0;
        int examPassRate = examTotalCount > 0 ? examPassCount * 100 / examTotalCount : 0;

        // 2. 练习统计
        int practiceTotal = eduPracticeRecordMapper.countByUserAndCourse(userId, courseId);
        int practiceCorrect = eduPracticeRecordMapper.countCorrectByUserAndCourse(userId, courseId);
        int practiceAccuracyRate = practiceTotal > 0 ? practiceCorrect * 100 / practiceTotal : 0;

        // 3. 章节学习进度
        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(courseId);
        int chapterTotal = chapters.size();
        int chapterStudied = studyRecordMapper.countStudiedChapters(userId, courseId);
        int chapterProgressRate = chapterTotal > 0 ? chapterStudied * 100 / chapterTotal : 0;

        // 4. 考试历史列表
        List<EduExamRecord> records = eduExamRecordMapper.selectByUserAndCourse(userId, courseId);
        // 收集 examId 列表，批量查询 exam 信息
        Set<Long> examIds = records.stream().map(EduExamRecord::getExamId).collect(Collectors.toSet());
        Map<Long, EduExam> examMap = new HashMap<>();
        if (!examIds.isEmpty()) {
            for (Long eid : examIds) {
                EduExam exam = eduExamMapper.selectById(eid);
                if (exam != null) {
                    examMap.put(eid, exam);
                }
            }
        }

        List<CourseExamHistoryVO> historyList = records.stream().map(r -> {
            EduExam exam = examMap.get(r.getExamId());
            return CourseExamHistoryVO.builder()
                    .recordId(r.getId())
                    .examId(r.getExamId())
                    .examName(exam != null ? exam.getTitle() : "")
                    .isFinalExam(exam != null ? exam.getIsFinalExam() : 0)
                    .score(r.getScore())
                    .totalScore(r.getTotalScore())
                    .passScore(r.getPassScore())
                    .isPass(r.getIsPass())
                    .attemptNo(r.getAttemptNo())
                    .submitTime(r.getSubmitTime())
                    .build();
        }).collect(Collectors.toList());

        return Response.success(CourseScoreRespVO.builder()
                .courseId(course.getId())
                .courseName(course.getName())
                .courseImage(course.getImageUrl())
                .examAvgScore(examAvgScore)
                .examMaxScore(examMaxScore)
                .examTotalCount(examTotalCount)
                .examPassCount(examPassCount)
                .examPassRate(examPassRate)
                .practiceTotalCount(practiceTotal)
                .practiceCorrectCount(practiceCorrect)
                .practiceAccuracyRate(practiceAccuracyRate)
                .chapterTotalCount(chapterTotal)
                .chapterStudiedCount(chapterStudied)
                .chapterProgressRate(chapterProgressRate)
                .examHistoryList(historyList)
                .build());
    }
}
