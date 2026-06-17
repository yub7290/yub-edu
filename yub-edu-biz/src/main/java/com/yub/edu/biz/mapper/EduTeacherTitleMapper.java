package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.TeacherTitleQueryDTO;
import com.yub.edu.biz.entity.EduTeacherTitle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教师职称 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师职称数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduTeacherTitleMapper {

    /**
     * 分页查询教师职称列表
     *
     * @param query 查询参数
     * @return 职称列表
     */
    List<EduTeacherTitle> selectPage(@Param("query") TeacherTitleQueryDTO query);

    /**
     * 根据ID查询职称
     *
     * @param id 职称ID
     * @return 教师职称
     */
    EduTeacherTitle selectById(@Param("id") Long id);

    /**
     * 新增职称
     *
     * @param title 教师职称
     * @return 影响行数
     */
    int insert(EduTeacherTitle title);

    /**
     * 更新职称
     *
     * @param title 教师职称
     * @return 影响行数
     */
    int updateById(EduTeacherTitle title);

    /**
     * 逻辑删除职称
     *
     * @param id 职称ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新职称状态
     *
     * @param id     职称ID
     * @param status 状态（1=启用 0=禁用）
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 查询引用该职称的教师数量
     *
     * @param titleId 职称ID
     * @return 教师数量
     */
    int countTeachersByTitleId(@Param("titleId") Long titleId);

    /**
     * 查询所有启用职称（供下拉框使用）
     *
     * @return 职称列表
     */
    List<EduTeacherTitle> selectAllEnabled();
}
