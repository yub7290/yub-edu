package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.KnowledgePointCreateReqDTO;
import com.yub.edu.biz.dto.KnowledgePointQueryDTO;
import com.yub.edu.biz.dto.KnowledgePointUpdateReqDTO;
import com.yub.edu.biz.entity.EduKnowledgePoint;

import java.util.List;

/**
 * 知识点服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点管理服务
 * @Version: 1.0.0
 */
public interface EduKnowledgePointService {

    PageResult<EduKnowledgePoint> page(PageQuery<KnowledgePointQueryDTO> pageQuery);

    EduKnowledgePoint getDetail(Long id);

    List<EduKnowledgePoint> listByCategoryId(Long categoryId);

    List<EduKnowledgePoint> listByCourseId(Long courseId, Long categoryId);

    Long create(KnowledgePointCreateReqDTO dto);

    void update(KnowledgePointUpdateReqDTO dto);

    void delete(Long id);

    /**
     * 根据ID批量查询知识点
     *
     * @param ids 知识点ID列表
     * @return 知识点列表
     */
    List<EduKnowledgePoint> selectBatchByIds(List<Long> ids);
}
