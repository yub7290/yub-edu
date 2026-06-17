package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.TeacherTitleCreateReqDTO;
import com.yub.edu.biz.dto.TeacherTitleQueryDTO;
import com.yub.edu.biz.dto.TeacherTitleUpdateReqDTO;
import com.yub.edu.biz.entity.EduTeacherTitle;
import com.yub.edu.biz.vo.TeacherTitleDetailRespVO;
import com.yub.edu.biz.vo.TeacherTitlePageRespVO;

import java.util.List;

/**
 * 教师职称 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师职称业务接口
 * @Version: 1.0.0
 */
public interface TeacherTitleService {

    /**
     * 分页查询教师职称列表
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<TeacherTitlePageRespVO> page(PageQuery<TeacherTitleQueryDTO> pageQuery);

    /**
     * 获取职称详情
     *
     * @param id 职称ID
     * @return 职称详情
     */
    TeacherTitleDetailRespVO getDetail(Long id);

    /**
     * 新增职称
     *
     * @param req 新增请求
     * @return 职称ID
     */
    Long create(TeacherTitleCreateReqDTO req);

    /**
     * 编辑职称
     *
     * @param req 编辑请求
     */
    void update(TeacherTitleUpdateReqDTO req);

    /**
     * 删除职称
     *
     * @param id 职称ID
     */
    void delete(Long id);

    /**
     * 切换职称状态
     *
     * @param id     职称ID
     * @param status 状态（1=启用 0=禁用）
     */
    void changeStatus(Long id, Integer status);

    /**
     * 查询所有启用职称列表（供下拉框使用）
     *
     * @return 职称列表
     */
    List<EduTeacherTitle> selectAllEnabled();
}
