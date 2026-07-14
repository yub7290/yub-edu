package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.KnowledgeRelationCreateReqDTO;
import com.yub.edu.biz.dto.KnowledgeRelationUpdateReqDTO;
import com.yub.edu.biz.entity.EduKnowledgePoint;
import com.yub.edu.biz.entity.EduKnowledgeRelation;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduKnowledgePointMapper;
import com.yub.edu.biz.mapper.EduKnowledgeRelationMapper;
import com.yub.edu.biz.service.EduKnowledgeRelationService;
import com.yub.edu.biz.vo.KnowledgeRelationVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EduKnowledgeRelationServiceImpl implements EduKnowledgeRelationService {

    private final EduKnowledgeRelationMapper relationMapper;
    private final EduKnowledgePointMapper pointMapper;

    private static final Map<Integer, String> RELATION_TYPE_MAP = new HashMap<>();
    static {
        RELATION_TYPE_MAP.put(1, "前置依赖");
        RELATION_TYPE_MAP.put(2, "进阶延伸");
        RELATION_TYPE_MAP.put(3, "关联参考");
        RELATION_TYPE_MAP.put(4, "对比对照");
    }

    @Override
    public List<KnowledgeRelationVO> getRelationsByKnowledgeId(Long knowledgeId) {
        List<EduKnowledgeRelation> relations = relationMapper.selectByKnowledgeId(knowledgeId);
        return buildVOList(relations);
    }

    @Override
    public List<KnowledgeRelationVO> getAllRelations() {
        List<EduKnowledgeRelation> relations = relationMapper.selectAll();
        return buildVOList(relations);
    }

    @Override
    public List<EduKnowledgeRelation> getRelationEntitiesByKnowledgeId(Long knowledgeId) {
        return relationMapper.selectByKnowledgeId(knowledgeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(KnowledgeRelationCreateReqDTO dto) {
        log.info("创建知识点关系: sourceId={}, targetId={}, relationType={}", 
                dto.getSourceId(), dto.getTargetId(), dto.getRelationType());
        
        if (dto.getSourceId().equals(dto.getTargetId())) {
            throw new EduException(EduErrorCode.KNOWLEDGE_RELATION_SELF);
        }

        int count = relationMapper.countBySourceAndTargetAndType(dto.getSourceId(), dto.getTargetId(), dto.getRelationType());
        log.info("关系已存在检查: count={}", count);
        if (count > 0) {
            throw new EduException(EduErrorCode.KNOWLEDGE_RELATION_EXISTS);
        }

        EduKnowledgeRelation relation = new EduKnowledgeRelation();
        relation.setSourceId(dto.getSourceId());
        relation.setTargetId(dto.getTargetId());
        relation.setRelationType(dto.getRelationType());
        relation.setDescription(dto.getDescription());
        relation.setSort(dto.getSort());
        relation.setStatus(1);
        
        Long uid = null;
        try {
            uid = SecurityUtils.getCurrentUserId();
        } catch (Exception e) {
            log.warn("获取当前用户ID失败: {}", e.getMessage());
        }
        log.info("当前用户ID: {}", uid);
        if (uid != null) {
            relation.setCreateBy(uid);
            relation.setUpdateBy(uid);
        }

        relationMapper.insert(relation);
        log.info("知识点关系创建成功: id={}", relation.getId());
        return relation.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KnowledgeRelationUpdateReqDTO dto) {
        EduKnowledgeRelation relation = relationMapper.selectById(dto.getId());
        if (relation == null) {
            throw new EduException(EduErrorCode.KNOWLEDGE_RELATION_NOT_FOUND);
        }

        if (dto.getSourceId() != null) {
            relation.setSourceId(dto.getSourceId());
        }
        if (dto.getTargetId() != null) {
            relation.setTargetId(dto.getTargetId());
        }
        if (dto.getRelationType() != null) {
            relation.setRelationType(dto.getRelationType());
        }
        if (dto.getDescription() != null) {
            relation.setDescription(dto.getDescription());
        }
        if (dto.getSort() != null) {
            relation.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            relation.setStatus(dto.getStatus());
        }

        Long uid = SecurityUtils.getCurrentUserId();
        relation.setUpdateBy(uid);

        relationMapper.updateById(relation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduKnowledgeRelation relation = relationMapper.selectById(id);
        if (relation == null) {
            throw new EduException(EduErrorCode.KNOWLEDGE_RELATION_NOT_FOUND);
        }
        relationMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByKnowledgeId(Long knowledgeId) {
        relationMapper.deleteByKnowledgeId(knowledgeId);
    }

    private List<KnowledgeRelationVO> buildVOList(List<EduKnowledgeRelation> relations) {
        List<KnowledgeRelationVO> result = new ArrayList<>();
        if (relations.isEmpty()) {
            return result;
        }

        Map<Long, String> pointTitleMap = new HashMap<>();
        List<Long> pointIds = new ArrayList<>();
        for (EduKnowledgeRelation r : relations) {
            pointIds.add(r.getSourceId());
            pointIds.add(r.getTargetId());
        }

        if (!pointIds.isEmpty()) {
            List<EduKnowledgePoint> points = pointMapper.selectBatchByIds(pointIds);
            for (EduKnowledgePoint p : points) {
                pointTitleMap.put(p.getId(), p.getTitle());
            }
        }

        for (EduKnowledgeRelation r : relations) {
            KnowledgeRelationVO vo = new KnowledgeRelationVO();
            vo.setId(r.getId());
            vo.setSourceId(r.getSourceId());
            vo.setSourceTitle(pointTitleMap.get(r.getSourceId()));
            vo.setTargetId(r.getTargetId());
            vo.setTargetTitle(pointTitleMap.get(r.getTargetId()));
            vo.setRelationType(r.getRelationType());
            vo.setRelationTypeName(RELATION_TYPE_MAP.get(r.getRelationType()));
            vo.setDescription(r.getDescription());
            vo.setSort(r.getSort());
            result.add(vo);
        }

        return result;
    }
}