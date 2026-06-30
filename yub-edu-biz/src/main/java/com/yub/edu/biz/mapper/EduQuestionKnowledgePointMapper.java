package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduQuestionKnowledgePoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试题关联知识点 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 试题关联知识点数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduQuestionKnowledgePointMapper {

    /**
     * 批量插入试题知识点关联
     */
    int batchInsert(@Param("list") List<EduQuestionKnowledgePoint> list);

    /**
     * 根据试题ID删除所有关联
     */
    int deleteByQuestionId(@Param("questionId") Long questionId);

    /**
     * 根据试题ID查询知识点ID列表
     */
    List<Long> selectKnowledgePointIdsByQuestionId(@Param("questionId") Long questionId);
}
