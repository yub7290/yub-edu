package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.StudentQueryDTO;
import com.yub.edu.biz.entity.EduStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学员 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 学员数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduStudentMapper {

    /**
     * 分页查询学员列表
     *
     * @param query 查询参数
     * @return 学员列表
     */
    List<EduStudent> selectPage(@Param("query") StudentQueryDTO query);

    /**
     * 根据ID查询学员
     *
     * @param id 学员ID
     * @return 学员
     */
    EduStudent selectById(@Param("id") Long id);

    /**
     * 根据账号查询学员（不含已删除）
     *
     * @param account 账号
     * @return 学员
     */
    EduStudent selectByAccount(@Param("account") String account);

    /**
     * 根据账号查询学员（含已删除，用于唯一性校验）
     *
     * @param account 账号
     * @return 学员
     */
    EduStudent selectByAccountIncludeDeleted(@Param("account") String account);

    /**
     * 新增学员
     *
     * @param student 学员
     * @return 影响行数
     */
    int insert(EduStudent student);

    /**
     * 更新学员
     *
     * @param student 学员
     * @return 影响行数
     */
    int updateById(EduStudent student);

    /**
     * 逻辑删除学员
     *
     * @param id 学员ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量逻辑删除学员
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int deleteBatch(@Param("ids") List<Long> ids);

    /**
     * 更新学员状态
     *
     * @param id     学员ID
     * @param status 状态（1=启用 0=禁用）
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 批量更新学员状态
     *
     * @param ids    学员ID列表
     * @param status 状态（1=启用 0=禁用）
     * @return 影响行数
     */
    int updateStatusBatch(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 更新密码
     *
     * @param id          学员ID
     * @param encodedPwd  BCrypt加密后的密码
     * @return 影响行数
     */
    int updatePassword(@Param("id") Long id, @Param("password") String encodedPwd);

    /**
     * 更新最后登录时间
     *
     * @param id 学员ID
     * @return 影响行数
     */
    int updateLastLoginTime(@Param("id") Long id);

    /**
     * 仅更新学员编号（注册时根据自增ID派生，避免覆盖其它字段）
     *
     * @param id        学员ID
     * @param studentNo 学员编号
     * @return 影响行数
     */
    int updateStudentNo(@Param("id") Long id, @Param("studentNo") String studentNo);
}
