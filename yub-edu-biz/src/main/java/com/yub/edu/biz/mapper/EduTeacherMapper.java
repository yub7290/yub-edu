package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.TeacherQueryDTO;
import com.yub.edu.biz.entity.EduTeacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教师 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduTeacherMapper {

    /**
     * 分页查询教师列表
     *
     * @param query 查询参数
     * @return 教师列表
     */
    List<EduTeacher> selectPage(@Param("query") TeacherQueryDTO query);

    /**
     * 根据ID查询教师
     *
     * @param id 教师ID
     * @return 教师
     */
    EduTeacher selectById(@Param("id") Long id);

    /**
     * 根据账号查询教师（排除已删除）
     *
     * @param account 账号
     * @return 教师
     */
    EduTeacher selectByAccount(@Param("account") String account);

    /**
     * 根据账号查询教师（含已删除，用于唯一性校验）
     *
     * @param account 账号
     * @return 教师
     */
    EduTeacher selectByAccountIncludeDeleted(@Param("account") String account);

    /**
     * 新增教师
     *
     * @param teacher 教师
     * @return 影响行数
     */
    int insert(EduTeacher teacher);

    /**
     * 更新教师
     *
     * @param teacher 教师
     * @return 影响行数
     */
    int updateById(EduTeacher teacher);

    /**
     * 逻辑删除教师
     *
     * @param id 教师ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量逻辑删除教师
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int deleteBatch(@Param("ids") List<Long> ids);

    /**
     * 更新教师状态
     *
     * @param id     教师ID
     * @param status 状态（1=启用 0=禁用）
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新密码
     *
     * @param id          教师ID
     * @param encodedPwd  BCrypt加密后的密码
     * @return 影响行数
     */
    int updatePassword(@Param("id") Long id, @Param("password") String encodedPwd);

    /**
     * 查询所有启用教师（供下拉框使用）
     *
     * @return 教师列表
     */
    List<EduTeacher> selectAllEnabled();

    /**
     * 查询推荐教师列表
     *
     * @return 推荐教师列表
     */
    List<EduTeacher> selectRecommended();

    /**
     * 查询所有启用教师（学生端列表，包含头像/签名等）
     *
     * @return 教师列表
     */
    List<EduTeacher> selectStudentList();

    /**
     * 根据姓名查询教师（学生端详情用）
     *
     * @param name 教师姓名
     * @return 教师
     */
    EduTeacher selectByName(@Param("name") String name);

    /**
     * 更新教师推荐状态
     *
     * @param id          教师ID
     * @param recommended 推荐状态（1=推荐 0=不推荐）
     * @return 影响行数
     */
    int updateRecommended(@Param("id") Long id, @Param("recommended") Integer recommended);

    /**
     * 查询教师的角色编码列表
     *
     * @param teacherId 教师ID
     * @return 角色编码列表
     */
    List<String> selectRoleCodesByTeacherId(@Param("teacherId") Long teacherId);
}
