package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.StudentGroupQueryDTO;
import com.yub.edu.biz.entity.EduStudentGroup;
import com.yub.edu.biz.vo.StudentGroupDetailRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学员组 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduStudentGroupMapper {

    /**
     * 分页查询学员组列表
     *
     * @param query 查询参数
     * @return 学员组列表
     */
    List<EduStudentGroup> selectPage(@Param("query") StudentGroupQueryDTO query);

    /**
     * 根据ID查询学员组
     *
     * @param id 学员组ID
     * @return 学员组
     */
    EduStudentGroup selectById(@Param("id") Long id);

    /**
     * 根据名称查询学员组
     *
     * @param name 学员组名称
     * @return 学员组
     */
    EduStudentGroup selectByName(@Param("name") String name);

    /**
     * 查询学员组成员数
     *
     * @param groupId 学员组ID
     * @return 成员数
     */
    Integer selectMemberCount(@Param("groupId") Long groupId);

    /**
     * 查询学员组课程数
     *
     * @param groupId 学员组ID
     * @return 课程数
     */
    Integer selectCourseCount(@Param("groupId") Long groupId);

    /**
     * 查询学员组成员列表
     *
     * @param groupId 学员组ID
     * @return 成员列表
     */
    List<StudentGroupDetailRespVO.MemberItem> selectMembers(@Param("groupId") Long groupId);

    /**
     * 查询学员组课程列表
     *
     * @param groupId 学员组ID
     * @return 课程列表
     */
    List<StudentGroupDetailRespVO.CourseItem> selectCourses(@Param("groupId") Long groupId);

    /**
     * 检查学员组是否有关联课程
     *
     * @param groupId 学员组ID
     * @return 课程数量
     */
    Integer selectCoursesByGroupIdForCheck(@Param("groupId") Long groupId);

    /**
     * 检查学员组是否有关联学员
     *
     * @param groupId 学员组ID
     * @return 学员数量
     */
    Integer selectMembersByGroupIdForCheck(@Param("groupId") Long groupId);

    /**
     * 新增学员组
     *
     * @param group 学员组
     * @return 影响行数
     */
    int insert(EduStudentGroup group);

    /**
     * 更新学员组
     *
     * @param group 学员组
     * @return 影响行数
     */
    int updateById(EduStudentGroup group);

    /**
     * 逻辑删除学员组
     *
     * @param id 学员组ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新学员组状态
     *
     * @param id     学员组ID
     * @param status 状态（1=启用 0=禁用）
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 批量添加学员组课程
     *
     * @param groupId   学员组ID
     * @param courseIds 课程ID列表
     * @return 影响行数
     */
    int insertCourses(@Param("groupId") Long groupId, @Param("courseIds") List<Long> courseIds,
                       @Param("createBy") Long createBy);

    /**
     * 移除学员组课程
     *
     * @param groupId   学员组ID
     * @param courseIds 课程ID列表
     * @return 影响行数
     */
    int deleteCourses(@Param("groupId") Long groupId, @Param("courseIds") List<Long> courseIds);

    /**
     * 批量添加学员组成员
     *
     * @param groupId    学员组ID
     * @param studentIds 学员ID列表
     * @return 影响行数
     */
    int insertMembers(@Param("groupId") Long groupId, @Param("studentIds") List<Long> studentIds,
                       @Param("createBy") Long createBy);

    /**
     * 移除学员组成员
     *
     * @param groupId    学员组ID
     * @param studentIds 学员ID列表
     * @return 影响行数
     */
    int deleteMembers(@Param("groupId") Long groupId, @Param("studentIds") List<Long> studentIds);

    /**
     * 更新课程排序
     *
     * @param groupId  学员组ID
     * @param courseId 课程ID
     * @param sortOrder 排序值
     * @return 影响行数
     */
    int updateCourseSortOrder(@Param("groupId") Long groupId, @Param("courseId") Long courseId,
                              @Param("sortOrder") Integer sortOrder);
}
