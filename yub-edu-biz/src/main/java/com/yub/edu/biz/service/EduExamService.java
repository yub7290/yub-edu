package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.ExamCreateReqDTO;
import com.yub.edu.biz.dto.ExamQueryDTO;
import com.yub.edu.biz.dto.ExamUpdateReqDTO;
import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.vo.ExamDetailRespVO;
import com.yub.edu.biz.vo.ExamPageRespVO;

/**
 * 试卷服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试卷管理服务
 * @Version: 1.0.0
 */
public interface EduExamService {

    /**
     * 分页查询试卷
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<ExamPageRespVO> page(PageQuery<ExamQueryDTO> pageQuery);

    /**
     * 获取试卷详情
     *
     * @param id 试卷ID
     * @return 试卷详情
     */
    ExamDetailRespVO getDetail(Long id);

    /**
     * 新增试卷
     *
     * @param dto 新增参数
     * @return 试卷ID
     */
    Long create(ExamCreateReqDTO dto);

    /**
     * 编辑试卷
     *
     * @param dto 编辑参数
     */
    void update(ExamUpdateReqDTO dto);

    /**
     * 删除试卷
     *
     * @param id 试卷ID
     */
    void delete(Long id);

    /**
     * 切换试卷状态
     *
     * @param id     试卷ID
     * @param status 状态
     */
    void changeStatus(Long id, Integer status);

    /**
     * 根据ID查询试卷
     *
     * @param id 试卷ID
     * @return 试卷
     */
    EduExam selectById(Long id);
}
