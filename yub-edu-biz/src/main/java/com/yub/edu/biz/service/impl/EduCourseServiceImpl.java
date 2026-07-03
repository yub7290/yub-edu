package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageParam;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.api.dto.app.MyCoursePageQueryDTO;
import com.yub.edu.api.vo.app.MyCourseVO;
import com.yub.edu.biz.dto.CourseCreateReqDTO;
import com.yub.edu.biz.dto.CourseQueryDTO;
import com.yub.edu.biz.dto.CourseUpdateReqDTO;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduMajorMapper;
import com.yub.edu.biz.service.EduCourseService;
import com.yub.edu.biz.vo.CourseDetailRespVO;
import com.yub.edu.biz.vo.CourseOverviewRespVO;
import com.yub.edu.biz.vo.CoursePageRespVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 课程管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduCourseServiceImpl implements EduCourseService {

    private final EduCourseMapper eduCourseMapper;
    private final EduMajorMapper eduMajorMapper;

    @Override
    public PageResult<CoursePageRespVO> page(PageQuery<CourseQueryDTO> pageQuery) {
        CourseQueryDTO queryParam = pageQuery.getQueryParam();
        com.yub.common.model.PageParam pageParam = pageQuery.getPageParam();

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<CoursePageRespVO> list = eduCourseMapper.selectPage(queryParam);
        PageInfo<CoursePageRespVO> pageInfo = new PageInfo<>(list);

        return PageResult.of(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public CourseDetailRespVO getDetail(Long id) {
        EduCourse course = eduCourseMapper.selectById(id);
        if (course == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }

        String majorName = null;
        if (course.getMajorId() != null) {
            EduMajor major = eduMajorMapper.selectById(course.getMajorId());
            if (major != null) {
                majorName = major.getName();
            }
        }

        return CourseDetailRespVO.builder()
                .id(course.getId())
                .name(course.getName())
                .imageUrl(course.getImageUrl())
                .majorId(course.getMajorId())
                .majorName(majorName)
                .courseType(course.getCourseType())
                .status(course.getStatus())
                .recommended(course.getRecommended())
                .sort(course.getSort())
                .learningObjectives(course.getLearningObjectives())
                .introduction(course.getIntroduction())
                .monthsRequired(course.getMonthsRequired())
                .totalPrice(course.getTotalPrice())
                .isFree(course.getIsFree())
                .isFreeLimited(course.getIsFreeLimited())
                .freeStartTime(course.getFreeStartTime())
                .freeEndTime(course.getFreeEndTime())
                .allowTrial(course.getAllowTrial())
                .teacher(course.getTeacher())
                .studentCount(course.getStudentCount())
                .viewCount(course.getViewCount())
                .chapterCount(course.getChapterCount())
                .questionCount(course.getQuestionCount())
                .examCount(course.getExamCount())
                .videoCount(course.getVideoCount())
                .createTime(course.getCreateTime())
                .updateTime(course.getUpdateTime())
                .build();
    }

    @Override
    public CourseOverviewRespVO getOverview(Long id) {
        EduCourse course = eduCourseMapper.selectById(id);
        if (course == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }

        String majorName = null;
        if (course.getMajorId() != null) {
            EduMajor major = eduMajorMapper.selectById(course.getMajorId());
            if (major != null) {
                majorName = major.getName();
            }
        }

        return CourseOverviewRespVO.builder()
                .name(course.getName())
                .imageUrl(course.getImageUrl())
                .majorName(majorName)
                .teacher(course.getTeacher())
                .totalPrice(course.getTotalPrice())
                .isFree(course.getIsFree())
                .studentCount(course.getStudentCount())
                .viewCount(course.getViewCount())
                .chapterCount(course.getChapterCount())
                .questionCount(course.getQuestionCount())
                .examCount(course.getExamCount())
                .videoCount(course.getVideoCount())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(CourseCreateReqDTO dto) {
        EduCourse exist = eduCourseMapper.selectByName(dto.getName());
        if (exist != null) {
            throw new EduException(EduErrorCode.COURSE_NAME_EXISTS);
        }

        EduCourse course = new EduCourse();
        course.setName(dto.getName());
        course.setImageUrl(dto.getImageUrl());
        course.setMajorId(dto.getMajorId());
        course.setCourseType(dto.getCourseType());
        course.setStatus(dto.getStatus());
        course.setRecommended(dto.getRecommended() != null ? dto.getRecommended() : 0);
        course.setSort(dto.getSort() != null ? dto.getSort() : 0);
        course.setLearningObjectives(dto.getLearningObjectives());
        course.setIntroduction(dto.getIntroduction());
        course.setMonthsRequired(dto.getMonthsRequired());
        course.setTotalPrice(dto.getTotalPrice());
        course.setIsFree(dto.getIsFree() != null ? dto.getIsFree() : 0);
        course.setIsFreeLimited(dto.getIsFreeLimited() != null ? dto.getIsFreeLimited() : 0);
        course.setFreeStartTime(dto.getFreeStartTime());
        course.setFreeEndTime(dto.getFreeEndTime());
        course.setAllowTrial(dto.getAllowTrial() != null ? dto.getAllowTrial() : 0);
        course.setTeacher(dto.getTeacher());
        Long currentUserId = SecurityUtils.getCurrentUserId();
        course.setCreateBy(currentUserId);
        course.setUpdateBy(currentUserId);
        eduCourseMapper.insert(course);
        return course.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CourseUpdateReqDTO dto) {
        EduCourse course = eduCourseMapper.selectById(dto.getId());
        if (course == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }

        EduCourse exist = eduCourseMapper.selectByName(dto.getName());
        if (exist != null && !exist.getId().equals(dto.getId())) {
            throw new EduException(EduErrorCode.COURSE_NAME_EXISTS);
        }

        course.setName(dto.getName());
        course.setImageUrl(dto.getImageUrl());
        course.setMajorId(dto.getMajorId());
        course.setCourseType(dto.getCourseType());
        course.setStatus(dto.getStatus());
        course.setRecommended(dto.getRecommended() != null ? dto.getRecommended() : 0);
        course.setSort(dto.getSort() != null ? dto.getSort() : 0);
        course.setLearningObjectives(dto.getLearningObjectives());
        course.setIntroduction(dto.getIntroduction());
        course.setMonthsRequired(dto.getMonthsRequired());
        course.setTotalPrice(dto.getTotalPrice());
        course.setIsFree(dto.getIsFree() != null ? dto.getIsFree() : 0);
        course.setIsFreeLimited(dto.getIsFreeLimited() != null ? dto.getIsFreeLimited() : 0);
        course.setFreeStartTime(dto.getFreeStartTime());
        course.setFreeEndTime(dto.getFreeEndTime());
        course.setAllowTrial(dto.getAllowTrial() != null ? dto.getAllowTrial() : 0);
        course.setTeacher(dto.getTeacher());
        course.setUpdateBy(SecurityUtils.getCurrentUserId());
        eduCourseMapper.updateById(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduCourse course = eduCourseMapper.selectById(id);
        if (course == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }
        eduCourseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        EduCourse course = eduCourseMapper.selectById(id);
        if (course == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }
        eduCourseMapper.updateStatus(id, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setRecommended(Long id, Integer recommended) {
        EduCourse course = eduCourseMapper.selectById(id);
        if (course == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }
        eduCourseMapper.updateRecommended(id, recommended);
    }

    @Override
    public List<EduCourse> listRecommended() {
        return eduCourseMapper.selectByRecommended();
    }

    @Override
    public List<EduCourse> studentList(Long cateId, Integer tabType, String keyword) {
        return eduCourseMapper.selectStudentList(cateId, tabType, keyword);
    }

    @Override
    public PageResult<MyCourseVO> myCourse(PageQuery<MyCoursePageQueryDTO> pageQuery) {
        PageParam pageParam = pageQuery.getPageParam();
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize(), pageParam.getOrderBy());

        MyCoursePageQueryDTO queryParam = pageQuery.getQueryParam();
        queryParam.setStudentId(SecurityUtils.getCurrentUserId());
        List<MyCourseVO> myCourseVOList = eduCourseMapper.myCourse(queryParam);

        return PageResult.of(myCourseVOList, new PageInfo<>(myCourseVOList).getTotal());
    }
}
