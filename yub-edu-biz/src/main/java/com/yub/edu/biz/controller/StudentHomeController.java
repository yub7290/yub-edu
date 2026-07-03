package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduTeacher;
import com.yub.edu.biz.entity.EduTeacherTitle;
import com.yub.edu.biz.mapper.EduTeacherTitleMapper;
import com.yub.edu.biz.service.EduCourseService;
import com.yub.edu.biz.service.TeacherService;
import com.yub.edu.biz.vo.CourseRecommendedRespVO;
import com.yub.edu.biz.vo.TeacherListRespVO;
import com.yub.edu.biz.vo.TeacherRecommendedRespVO;
import com.yub.edu.biz.vo.BannerItemRespVO;
import com.yub.edu.biz.vo.HomeBaseRespVO;
import com.yub.edu.biz.vo.HomeCourseRespVO;
import com.yub.edu.biz.vo.HomeSearchRespVO;
import com.yub.edu.biz.vo.HomeTeacherListRespVO;
import com.yub.edu.biz.vo.HomeTeacherRespVO;
import com.yub.edu.biz.vo.NavItemRespVO;
import com.yub.edu.biz.vo.TeacherInfoRespVO;
import com.yub.system.entity.system.SysBanner;
import com.yub.system.mapper.system.SysBannerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

/**
 * 学生端首页 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端首页接口
 * @Version: 2.0.0
 */
@RestController
@RequestMapping("/student/home")
@RequiredArgsConstructor
public class StudentHomeController {

    private final EduCourseService eduCourseService;
    private final TeacherService teacherService;
    private final SysBannerMapper sysBannerMapper;
    private final EduTeacherTitleMapper eduTeacherTitleMapper;

    /**
     * 首页基础数据（Logo、Banner、导航）
     */
    @GetMapping("/base")
    public Response<HomeBaseRespVO> base() {
        // 查询启用的Banner
        List<SysBanner> banners = sysBannerMapper.selectAllEnabled();
        List<BannerItemRespVO> bannerList = banners.stream().map(b ->
            BannerItemRespVO.builder()
                    .id(b.getId())
                    .imageUrl(b.getImageUrl())
                    .linkUrl(b.getLinkUrl())
                    .build()
        ).toList();

        HomeBaseRespVO data = HomeBaseRespVO.builder()
                .logo("/static/logo.png")
                .banner(bannerList)
                .nav(new ArrayList<>())
                .build();
        return Response.success(data);
    }

    /**
     * 推荐教师列表
     */
    @GetMapping("/teacher")
    public Response<HomeTeacherRespVO> teacher() {
        List<EduTeacher> teachers = teacherService.selectRecommended();
        List<Long> titleIds = teachers.stream()
                .map(EduTeacher::getTitleId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, String> titleMap = titleIds.isEmpty() ? Collections.emptyMap() :
                eduTeacherTitleMapper.selectBatchByIds(titleIds).stream()
                .collect(Collectors.toMap(EduTeacherTitle::getId, EduTeacherTitle::getName));

        List<TeacherRecommendedRespVO> voList = teachers.stream().map(t -> {
            String titleName = titleMap.get(t.getTitleId());
            return TeacherRecommendedRespVO.builder()
                    .id(t.getId())
                    .avatarUrl(t.getAvatarUrl())
                    .name(t.getName())
                    .titleName(titleName)
                    .rating(t.getRating())
                    .build();
        }).toList();
        return Response.success(HomeTeacherRespVO.builder().list(voList).build());
    }

    /**
     * 推荐课程列表
     */
    @GetMapping("/course")
    public Response<HomeCourseRespVO> course() {
        List<EduCourse> courses = eduCourseService.listRecommended();
        LocalDateTime now = LocalDateTime.now();
        List<CourseRecommendedRespVO> voList = courses.stream().map(c -> {
            // 计算资费类型：免费 > 限免 > 试学（互斥取最高优先）
            String feeType = null;
            if (c.getIsFree() != null && c.getIsFree() == 1) {
                feeType = "免费";
            } else if (c.getIsFreeLimited() != null && c.getIsFreeLimited() == 1
                    && c.getFreeStartTime() != null && c.getFreeEndTime() != null
                    && !now.isBefore(c.getFreeStartTime()) && !now.isAfter(c.getFreeEndTime())) {
                feeType = "限免";
            } else if (c.getAllowTrial() != null && c.getAllowTrial() == 1) {
                feeType = "试学";
            }
            return CourseRecommendedRespVO.builder()
                    .id(c.getId())
                    .imageUrl(c.getImageUrl())
                    .name(c.getName())
                    .courseType(c.getCourseType())
                    .teacherName(c.getTeacher())
                    .feeType(feeType)
                    .build();
        }).toList();
        return Response.success(HomeCourseRespVO.builder().list(voList).build());
    }

    /**
     * 搜索课程
     */
    @GetMapping("/search")
    public Response<HomeSearchRespVO> search(@RequestParam(name = "keyword", required = false) String keyword) {
        return Response.success(HomeSearchRespVO.builder().list(new ArrayList<>()).build());
    }

    /**
     * 教师详情
     */
    @GetMapping("/teacher/info")
    public Response<TeacherInfoRespVO> teacherInfo(@RequestParam Long tid) {
        return Response.success(TeacherInfoRespVO.builder()
                .info(new HashMap<>())
                .course(new ArrayList<>())
                .build());
    }

    /**
     * 教师列表（所有启用教师）
     */
    @GetMapping("/teacher/list")
    public Response<HomeTeacherListRespVO> teacherList() {
        List<EduTeacher> teachers = teacherService.selectStudentList();
        List<Long> titleIds = teachers.stream()
                .map(EduTeacher::getTitleId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, String> titleMap = titleIds.isEmpty() ? Collections.emptyMap() :
                eduTeacherTitleMapper.selectBatchByIds(titleIds).stream()
                .collect(Collectors.toMap(EduTeacherTitle::getId, EduTeacherTitle::getName));

        List<TeacherListRespVO> voList = teachers.stream().map(t ->
            TeacherListRespVO.builder()
                    .id(t.getId())
                    .avatarUrl(t.getAvatarUrl())
                    .name(t.getName())
                    .titleName(titleMap.get(t.getTitleId()))
                    .signature(t.getSignature())
                    .rating(t.getRating())
                    .build()
        ).toList();
        return Response.success(HomeTeacherListRespVO.builder().list(voList).build());
    }
}
