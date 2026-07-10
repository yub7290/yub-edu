package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.GroupCourseReqDTO;
import com.yub.edu.biz.dto.GroupMemberReqDTO;
import com.yub.edu.biz.dto.StudentGroupCreateReqDTO;
import com.yub.edu.biz.dto.StudentGroupQueryDTO;
import com.yub.edu.biz.dto.StudentGroupUpdateReqDTO;
import com.yub.edu.biz.vo.StudentGroupDetailRespVO;
import com.yub.edu.biz.vo.StudentGroupPageRespVO;

import java.util.List;

/**
 * 学员组 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组业务接口
 * @Version: 1.0.0
 */
public interface StudentGroupService {

    /**
     * 分页查询学员组列表
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<StudentGroupPageRespVO> page(PageQuery<StudentGroupQueryDTO> pageQuery);

    /**
     * 获取学员组详情
     *
     * @param id 学员组ID
     * @return 学员组详情
     */
    StudentGroupDetailRespVO getDetail(Long id);

    /**
     * 新增学员组
     *
     * @param req 新增请求
     * @return 学员组ID
     */
    Long create(StudentGroupCreateReqDTO req);

    /**
     * 编辑学员组
     *
     * @param req 编辑请求
     */
    void update(StudentGroupUpdateReqDTO req);

    /**
     * 删除学员组
     *
     * @param id 学员组ID
     */
    void delete(Long id);

    /**
     * 切换学员组状态
     *
     * @param id     学员组ID
     * @param status 状态（1=启用 0=禁用）
     */
    void changeStatus(Long id, Integer status);

    /**
     * 设置课程（批量添加）
     *
     * @param id       学员组ID
     * @param courses  课程列表
     */
    void setCourses(Long id, List<GroupCourseReqDTO> courses);

    /**
     * 移除课程（批量）
     *
     * @param id        学员组ID
     * @param courseIds 课程ID列表
     */
    void removeCourses(Long id, List<Long> courseIds);

    /**
     * 学员组课程排序
     *
     * @param id        学员组ID
     * @param courseIds 按新顺序排列的课程ID列表
     */
    void sortCourses(Long id, List<Long> courseIds);

    /**
     * 添加学员（批量）
     *
     * @param id       学员组ID
     * @param members  学员列表
     */
    void addMembers(Long id, List<GroupMemberReqDTO> members);

    /**
     * 移除学员（批量）
     *
     * @param id         学员组ID
     * @param studentIds 学员ID列表
     */
    void removeMembers(Long id, List<Long> studentIds);
}
