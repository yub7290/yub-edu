package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduKnowledgeRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EduKnowledgeRelationMapper {

    int insert(EduKnowledgeRelation entity);

    int updateById(EduKnowledgeRelation entity);

    int deleteById(Long id);

    int deleteByKnowledgeId(Long knowledgeId);

    EduKnowledgeRelation selectById(Long id);

    List<EduKnowledgeRelation> selectBySourceId(Long sourceId);

    List<EduKnowledgeRelation> selectByTargetId(Long targetId);

    List<EduKnowledgeRelation> selectByKnowledgeId(Long knowledgeId);

    List<EduKnowledgeRelation> selectAll();

    int countBySourceAndTarget(@Param("sourceId") Long sourceId, @Param("targetId") Long targetId);

    int countBySourceAndTargetAndType(@Param("sourceId") Long sourceId, @Param("targetId") Long targetId, @Param("relationType") Integer relationType);
}