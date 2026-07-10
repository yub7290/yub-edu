package com.yub.edu.biz.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教师角色关联 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 教师角色关联数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduTeacherRoleMapper {

    /**
     * 根据教师ID查询角色ID列表
     *
     * @param teacherId 教师ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByTeacherId(@Param("teacherId") Long teacherId);

    /**
     * 根据教师ID删除角色关联
     *
     * @param teacherId 教师ID
     */
    void deleteByTeacherId(@Param("teacherId") Long teacherId);

    /**
     * 批量插入教师角色关联
     *
     * @param teacherId 教师ID
     * @param roleIds   角色ID列表
     * @param createBy  操作人ID
     */
    void batchInsert(@Param("teacherId") Long teacherId, @Param("roleIds") List<Long> roleIds, @Param("createBy") Long createBy);
}
