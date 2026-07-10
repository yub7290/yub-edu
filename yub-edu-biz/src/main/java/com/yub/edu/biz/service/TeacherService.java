package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.TeacherCreateReqDTO;
import com.yub.edu.biz.dto.TeacherQueryDTO;
import com.yub.edu.biz.dto.TeacherUpdateReqDTO;
import com.yub.edu.biz.entity.EduTeacher;
import com.yub.edu.biz.vo.TeacherDetailRespVO;
import com.yub.edu.biz.vo.TeacherPageRespVO;

import java.util.List;

/**
 * 教师 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师业务接口
 * @Version: 1.0.0
 */
public interface TeacherService {

    /**
     * 分页查询教师列表
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<TeacherPageRespVO> page(PageQuery<TeacherQueryDTO> pageQuery);

    /**
     * 获取教师详情
     *
     * @param id 教师ID
     * @return 教师详情
     */
    TeacherDetailRespVO getDetail(Long id);

    /**
     * 新增教师
     *
     * @param req 新增请求
     * @return 教师ID
     */
    Long create(TeacherCreateReqDTO req);

    /**
     * 编辑教师
     *
     * @param req 编辑请求
     */
    void update(TeacherUpdateReqDTO req);

    /**
     * 删除教师
     *
     * @param id 教师ID
     */
    void delete(Long id);

    /**
     * 批量删除教师
     *
     * @param ids 教师ID列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 重置密码
     *
     * @param id 教师ID
     */
    void resetPassword(Long id);

    /**
     * 切换教师状态
     *
     * @param id     教师ID
     * @param status 状态（1=启用 0=禁用）
     */
    void changeStatus(Long id, Integer status);

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
     * 查询所有启用教师列表（学生端）
     *
     * @return 教师列表
     */
    List<EduTeacher> selectStudentList();

    /**
     * 设置教师推荐状态
     *
     * @param id          教师ID
     * @param recommended 推荐状态（1=推荐 0=不推荐）
     */
    void setRecommended(Long id, Integer recommended);

    /**
     * 根据教师姓名查询
     *
     * @param name 教师姓名
     * @return 教师信息
     */
    EduTeacher selectByName(String name);
}
