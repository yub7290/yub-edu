package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.KnowledgePointQueryDTO;
import com.yub.edu.biz.entity.EduKnowledgePoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识点 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduKnowledgePointMapper {

    List<EduKnowledgePoint> selectPage(@Param("query") KnowledgePointQueryDTO query);

    List<EduKnowledgePoint> selectByCategoryId(@Param("categoryId") Long categoryId);

    List<EduKnowledgePoint> selectByCourseId(@Param("courseId") Long courseId, @Param("categoryId") Long categoryId);

    List<EduKnowledgePoint> selectByCourseIds(@Param("courseIds") List<Long> courseIds);

    List<EduKnowledgePoint> selectBatchByIds(@Param("ids") List<Long> ids);

    EduKnowledgePoint selectById(@Param("id") Long id);

    int insert(EduKnowledgePoint point);

    int updateById(EduKnowledgePoint point);

    int deleteById(@Param("id") Long id);
}
