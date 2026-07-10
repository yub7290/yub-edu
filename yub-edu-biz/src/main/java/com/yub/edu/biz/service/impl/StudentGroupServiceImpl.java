package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.framework.security.SecurityUtils;
import com.yub.edu.biz.dto.GroupCourseReqDTO;
import com.yub.edu.biz.dto.GroupMemberReqDTO;
import com.yub.edu.biz.dto.StudentGroupCreateReqDTO;
import com.yub.edu.biz.dto.StudentGroupQueryDTO;
import com.yub.edu.biz.dto.StudentGroupUpdateReqDTO;
import com.yub.edu.biz.entity.EduStudentGroup;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduStudentGroupMapper;
import com.yub.edu.biz.service.StudentGroupService;
import com.yub.edu.biz.vo.StudentGroupDetailRespVO;
import com.yub.edu.biz.vo.StudentGroupPageRespVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 学员组 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组业务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentGroupServiceImpl implements StudentGroupService {

    private final EduStudentGroupMapper eduStudentGroupMapper;

    @Override
    public PageResult<StudentGroupPageRespVO> page(PageQuery<StudentGroupQueryDTO> pageQuery) {
        PageHelper.startPage(pageQuery.getPageParam().getPageNum(), pageQuery.getPageParam().getPageSize());
        List<EduStudentGroup> list = eduStudentGroupMapper.selectPage(pageQuery.getQueryParam());
        PageInfo<EduStudentGroup> pageInfo = new PageInfo<>(list);

        List<StudentGroupPageRespVO> records = list.stream()
                .map(this::convertToPageVO)
                .collect(Collectors.toList());

        return PageResult.of(records, pageInfo.getTotal());
    }

    private StudentGroupPageRespVO convertToPageVO(EduStudentGroup group) {
        return StudentGroupPageRespVO.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .memberCount(group.getMemberCount())
                .courseCount(group.getCourseCount())
                .createTime(group.getCreateTime())
                .build();
    }

    @Override
    public StudentGroupDetailRespVO getDetail(Long id) {
        EduStudentGroup group = eduStudentGroupMapper.selectById(id);
        if (group == null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NOT_FOUND);
        }

        Integer memberCount = eduStudentGroupMapper.selectMemberCount(id);
        Integer courseCount = eduStudentGroupMapper.selectCourseCount(id);
        List<StudentGroupDetailRespVO.MemberItem> members = eduStudentGroupMapper.selectMembers(id);
        List<StudentGroupDetailRespVO.CourseItem> courses = eduStudentGroupMapper.selectCourses(id);

        return StudentGroupDetailRespVO.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .sortOrder(group.getSortOrder())
                .status(group.getStatus())
                .createTime(group.getCreateTime())
                .updateTime(group.getUpdateTime())
                .memberCount(memberCount)
                .courseCount(courseCount)
                .members(members)
                .courses(courses)
                .build();
    }

    @Override
    @Transactional
    public Long create(StudentGroupCreateReqDTO req) {
        EduStudentGroup exist = eduStudentGroupMapper.selectByName(req.getName());
        if (exist != null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NAME_EXISTS);
        }

        EduStudentGroup group = new EduStudentGroup();
        group.setName(req.getName());
        group.setDescription(req.getDescription());
        group.setSortOrder(req.getSortOrder());
        group.setStatus(req.getStatus());

        Long currentUserId = SecurityUtils.getCurrentUserId();
        group.setCreateBy(currentUserId);
        group.setUpdateBy(currentUserId);
        eduStudentGroupMapper.insert(group);
        return group.getId();
    }

    @Override
    @Transactional
    public void update(StudentGroupUpdateReqDTO req) {
        EduStudentGroup exist = eduStudentGroupMapper.selectById(req.getId());
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NOT_FOUND);
        }

        EduStudentGroup nameCheck = eduStudentGroupMapper.selectByName(req.getName());
        if (nameCheck != null && !nameCheck.getId().equals(req.getId())) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NAME_EXISTS);
        }

        EduStudentGroup group = new EduStudentGroup();
        group.setId(req.getId());
        group.setName(req.getName());
        group.setDescription(req.getDescription());
        group.setSortOrder(req.getSortOrder());
        group.setStatus(req.getStatus());
        group.setUpdateBy(SecurityUtils.getCurrentUserId());

        eduStudentGroupMapper.updateById(group);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EduStudentGroup exist = eduStudentGroupMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NOT_FOUND);
        }

        Integer memberCount = eduStudentGroupMapper.selectMembersByGroupIdForCheck(id);
        if (memberCount != null && memberCount > 0) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_HAS_MEMBERS);
        }

        Integer courseCount = eduStudentGroupMapper.selectCoursesByGroupIdForCheck(id);
        if (courseCount != null && courseCount > 0) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_HAS_COURSES);
        }

        eduStudentGroupMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, Integer status) {
        EduStudentGroup exist = eduStudentGroupMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NOT_FOUND);
        }
        eduStudentGroupMapper.updateStatus(id, status);
    }

    @Override
    @Transactional
    public void setCourses(Long id, List<GroupCourseReqDTO> courses) {
        EduStudentGroup exist = eduStudentGroupMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NOT_FOUND);
        }

        List<Long> courseIds = courses.stream()
                .map(GroupCourseReqDTO::getCourseId)
                .collect(Collectors.toList());

        if (!courseIds.isEmpty()) {
            eduStudentGroupMapper.insertCourses(id, courseIds, SecurityUtils.getCurrentUserId());
        }
    }

    @Override
    @Transactional
    public void removeCourses(Long id, List<Long> courseIds) {
        EduStudentGroup exist = eduStudentGroupMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NOT_FOUND);
        }

        if (courseIds != null && !courseIds.isEmpty()) {
            eduStudentGroupMapper.deleteCourses(id, courseIds);
        }
    }

    @Override
    @Transactional
    public void sortCourses(Long id, List<Long> courseIds) {
        EduStudentGroup exist = eduStudentGroupMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NOT_FOUND);
        }

        if (courseIds == null || courseIds.isEmpty()) {
            return;
        }

        for (int i = 0; i < courseIds.size(); i++) {
            eduStudentGroupMapper.updateCourseSortOrder(id, courseIds.get(i), i);
        }
    }

    @Override
    @Transactional
    public void addMembers(Long id, List<GroupMemberReqDTO> members) {
        EduStudentGroup exist = eduStudentGroupMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NOT_FOUND);
        }

        List<Long> studentIds = members.stream()
                .map(GroupMemberReqDTO::getStudentId)
                .collect(Collectors.toList());

        if (!studentIds.isEmpty()) {
            eduStudentGroupMapper.insertMembers(id, studentIds, SecurityUtils.getCurrentUserId());
        }
    }

    @Override
    @Transactional
    public void removeMembers(Long id, List<Long> studentIds) {
        EduStudentGroup exist = eduStudentGroupMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_GROUP_NOT_FOUND);
        }

        if (studentIds != null && !studentIds.isEmpty()) {
            eduStudentGroupMapper.deleteMembers(id, studentIds);
        }
    }
}
