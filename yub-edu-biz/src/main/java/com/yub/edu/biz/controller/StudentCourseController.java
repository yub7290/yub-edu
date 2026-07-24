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
import com.yub.edu.biz.service.ChapterService;
import com.yub.edu.biz.service.EduAiConfigService;
import com.yub.edu.biz.service.EduCourseService;
import com.yub.edu.biz.service.EduExamRecordService;
import com.yub.edu.biz.service.EduExamService;
import com.yub.edu.biz.service.EduPracticeRecordService;
import com.yub.edu.biz.service.StudyRecordService;
import com.yub.edu.biz.service.TeacherService;
import com.yub.edu.biz.service.CourseAccessService;
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

    private final EduCourseService eduCourseService;
    private final ChapterService chapterService;
    private final TeacherService teacherService;
    private final EduAiConfigService eduAiConfigService;
    private final EduExamRecordService eduExamRecordService;
    private final EduPracticeRecordService eduPracticeRecordService;
    private final StudyRecordService studyRecordService;
    private final EduExamService eduExamService;
    private final CourseAccessService courseAccessService;

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

        Long studentId = SecurityUtils.getCurrentUserId();
        Map<Long, Boolean> accessMap = courseAccessService.batchAccessible(
                studentId, list.stream().map(EduCourse::getId).toList());

        List<CourseRecommendedRespVO> voList = list.stream().map(c ->
            CourseRecommendedRespVO.builder()
                    .id(c.getId())
                    .imageUrl(c.getImageUrl())
                    .name(c.getName())
                    .courseType(c.getCourseType())
                    .teacherName(c.getTeacher())
                    .accessible(accessMap.getOrDefault(c.getId(), false))
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

        Long studentId = SecurityUtils.getCurrentUserId();
        Map<Long, Boolean> accessMap = courseAccessService.batchAccessible(
                studentId, list.stream().map(EduCourse::getId).toList());

        List<CourseRecommendedRespVO> voList = list.stream().map(c ->
            CourseRecommendedRespVO.builder()
                    .id(c.getId())
                    .imageUrl(c.getImageUrl())
                    .name(c.getName())
                    .courseType(c.getCourseType())
                    .teacherName(c.getTeacher())
                    .accessible(accessMap.getOrDefault(c.getId(), false))
                    .build()
        ).toList();
        return Response.success(CourseListRespVO.builder().list(voList).total(list.size()).build());
    }

    /**
     * 课程详情
     */
    @GetMapping("/detail")
    public Response<StudentCourseDetailRespVO> detail(@RequestParam("cid") Long cid) {
        EduCourse course = eduCourseService.selectById(cid);
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

        // 章节列表（树形结构）
        List<EduChapter> allChapters = flattenChapterTree(chapterService.getTree(cid));
        List<Map<String, Object>> chapterList = buildChapterTreeMap(allChapters);

        // 教师信息
        Map<String, Object> teacherInfo = new HashMap<>();
        String teacherName = course.getTeacher();
        if (teacherName != null && !teacherName.isEmpty()) {
            EduTeacher teacher = teacherService.selectByName(teacherName);
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
        EduAiConfig aiConfig = eduAiConfigService.selectByCourseId(cid);
        boolean aiEnabled = aiConfig != null && aiConfig.getEnabled() != null && aiConfig.getEnabled() == 1;
        aiAssistant.put("enabled", aiEnabled);
        // 默认系统提示词也返回前端，方便展示AI助教角色

        // 课程学习权限：免费课 / 已购买 / 所属组已绑定
        Long studentId = SecurityUtils.getCurrentUserId();
        boolean accessible = courseAccessService.canAccess(studentId, cid);

        return Response.success(StudentCourseDetailRespVO.builder()
                .course(courseInfo)
                .chapter(chapterList)
                .teacher(teacherInfo)
                .aiAssistant(aiAssistant)
                .accessible(accessible)
                .build());
    }

    /**
     * 课程综合成绩
     *
     * @param courseId 课程ID
     * @return 综合成绩数据
     */
    @GetMapping("/{courseId}/score")
    public Response<CourseScoreRespVO> score(@PathVariable("courseId") Long courseId) {
        Long userId = SecurityUtils.getCurrentUserId();

        // 课程信息
        EduCourse course = eduCourseService.selectById(courseId);
        if (course == null) {
            return Response.success(null);
        }

        // 1. 考试统计
        CourseExamStatsResult examStats = eduExamRecordService.selectCourseExamStats(userId, courseId);
        int examAvgScore = examStats != null ? examStats.getAvgScore() : 0;
        int examMaxScore = examStats != null ? examStats.getMaxScore() : 0;
        int examTotalCount = examStats != null ? examStats.getTotalCount() : 0;
        int examPassCount = examStats != null ? examStats.getPassCount() : 0;
        int examPassRate = examTotalCount > 0 ? examPassCount * 100 / examTotalCount : 0;

        // 2. 练习统计
        int practiceTotal = eduPracticeRecordService.countByUserAndCourse(userId, courseId);
        int practiceCorrect = eduPracticeRecordService.countCorrectByUserAndCourse(userId, courseId);
        int practiceAccuracyRate = practiceTotal > 0 ? practiceCorrect * 100 / practiceTotal : 0;

        // 3. 章节学习进度
        List<EduChapter> allChapters = flattenChapterTree(chapterService.getTree(courseId));
        int chapterTotal = allChapters.size();
        int chapterStudied = studyRecordService.countStudiedChapters(userId, courseId);
        int chapterProgressRate = chapterTotal > 0 ? chapterStudied * 100 / chapterTotal : 0;

        // 4. 考试历史列表
        List<EduExamRecord> records = eduExamRecordService.selectByUserAndCourse(userId, courseId);
        // 收集 examId 列表，批量查询 exam 信息
        Set<Long> examIds = records.stream().map(EduExamRecord::getExamId).collect(Collectors.toSet());
        Map<Long, EduExam> examMap = new HashMap<>();
        if (!examIds.isEmpty()) {
            for (Long eid : examIds) {
                EduExam exam = eduExamService.selectById(eid);
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

    /**
     * 将树形章节结构展平为列表
     *
     * @param chapters 树形章节列表
     * @return 展平后的章节列表
     */
    private List<EduChapter> flattenChapterTree(List<EduChapter> chapters) {
        List<EduChapter> result = new ArrayList<>();
        if (chapters == null) {
            return result;
        }
        for (EduChapter ch : chapters) {
            result.add(ch);
            if (ch.getChildren() != null && !ch.getChildren().isEmpty()) {
                result.addAll(flattenChapterTree(ch.getChildren()));
            }
        }
        return result;
    }

    /**
     * 将扁平章节列表构建为树形结构Map
     *
     * @param chapters 扁平章节列表（按parent_id, sort排序）
     * @return 树形结构章节列表
     */
    private List<Map<String, Object>> buildChapterTreeMap(List<EduChapter> chapters) {
        Map<Long, Map<String, Object>> map = new LinkedHashMap<>();
        List<Map<String, Object>> roots = new ArrayList<>();
        for (EduChapter ch : chapters) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", ch.getId());
            node.put("name", ch.getName());
            map.put(ch.getId(), node);
        }
        for (EduChapter ch : chapters) {
            Map<String, Object> node = map.get(ch.getId());
            if (ch.getParentId() == null || ch.getParentId() == 0L) {
                roots.add(node);
            } else {
                Map<String, Object> parent = map.get(ch.getParentId());
                if (parent != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
                    if (children == null) {
                        children = new ArrayList<>();
                        parent.put("children", children);
                    }
                    children.add(node);
                } else {
                    roots.add(node);
                }
            }
        }
        return roots;
    }
}
