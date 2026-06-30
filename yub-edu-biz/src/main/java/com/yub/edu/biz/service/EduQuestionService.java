package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.QuestionCreateReqDTO;
import com.yub.edu.biz.dto.QuestionQueryDTO;
import com.yub.edu.biz.dto.QuestionUpdateReqDTO;
import com.yub.edu.biz.vo.QuestionDetailRespVO;
import com.yub.edu.biz.vo.QuestionPageRespVO;

import java.util.List;

/**
 * 试题服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试题管理服务
 * @Version: 1.0.0
 */
public interface EduQuestionService {

    /**
     * 分页查询试题
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<QuestionPageRespVO> page(PageQuery<QuestionQueryDTO> pageQuery);

    /**
     * 获取试题详情
     *
     * @param id 试题ID
     * @return 试题详情
     */
    QuestionDetailRespVO getDetail(Long id);

    /**
     * 新增试题
     *
     * @param dto 新增参数
     * @return 试题ID
     */
    Long create(QuestionCreateReqDTO dto);

    /**
     * 编辑试题
     *
     * @param dto 编辑参数
     */
    void update(QuestionUpdateReqDTO dto);

    /**
     * 删除试题
     *
     * @param id 试题ID
     */
    void delete(Long id);

    /**
     * 切换试题状态
     *
     * @param id     试题ID
     * @param status 状态
     */
    void changeStatus(Long id, Integer status);

    /**
     * 获取试题关联的知识点ID列表
     *
     * @param questionId 试题ID
     * @return 知识点ID列表
     */
    List<Long> getKnowledgePointIds(Long questionId);

    /**
     * 更新试题关联的知识点
     *
     * @param questionId        试题ID
     * @param knowledgePointIds 知识点ID列表
     */
    void updateKnowledgePoints(Long questionId, List<Long> knowledgePointIds);
}
