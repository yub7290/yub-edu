package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.KnowledgeRelationCreateReqDTO;
import com.yub.edu.biz.dto.KnowledgeRelationUpdateReqDTO;
import com.yub.edu.biz.entity.EduKnowledgeRelation;
import com.yub.edu.biz.vo.KnowledgeRelationVO;

import java.util.List;

public interface EduKnowledgeRelationService {

    List<KnowledgeRelationVO> getRelationsByKnowledgeId(Long knowledgeId);

    List<KnowledgeRelationVO> getAllRelations();

    List<EduKnowledgeRelation> getRelationEntitiesByKnowledgeId(Long knowledgeId);

    Long create(KnowledgeRelationCreateReqDTO dto);

    void update(KnowledgeRelationUpdateReqDTO dto);

    void delete(Long id);

    void deleteByKnowledgeId(Long knowledgeId);
}