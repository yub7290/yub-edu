package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.StudentCreateReqDTO;
import com.yub.edu.biz.dto.StudentQueryDTO;
import com.yub.edu.biz.dto.StudentUpdateReqDTO;
import com.yub.edu.biz.vo.StudentDetailRespVO;
import com.yub.edu.biz.vo.StudentPageRespVO;

import java.util.List;

/**
 * 学员 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 学员业务接口
 * @Version: 1.0.0
 */
public interface StudentService {

    /**
     * 分页查询学员列表
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<StudentPageRespVO> page(PageQuery<StudentQueryDTO> pageQuery);

    /**
     * 获取学员详情
     *
     * @param id 学员ID
     * @return 学员详情
     */
    StudentDetailRespVO getDetail(Long id);

    /**
     * 新增学员
     *
     * @param req 新增请求
     * @return 学员ID
     */
    Long create(StudentCreateReqDTO req);

    /**
     * 编辑学员
     *
     * @param req 编辑请求
     */
    void update(StudentUpdateReqDTO req);

    /**
     * 删除学员
     *
     * @param id 学员ID
     */
    void delete(Long id);

    /**
     * 批量删除学员
     *
     * @param ids 学员ID列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 切换学员状态
     *
     * @param id     学员ID
     * @param status 状态（1=启用 0=禁用）
     */
    void changeStatus(Long id, Integer status);

    /**
     * 批量禁用学员
     *
     * @param ids 学员ID列表
     */
    void batchDisable(List<Long> ids);

    /**
     * 重置密码
     *
     * @param id 学员ID
     */
    void resetPassword(Long id);
}
