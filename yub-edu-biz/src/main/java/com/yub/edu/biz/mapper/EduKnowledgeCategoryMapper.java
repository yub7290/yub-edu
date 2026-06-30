package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduKnowledgeCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识点分类 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点分类数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduKnowledgeCategoryMapper {

    List<EduKnowledgeCategory> selectTree();

    EduKnowledgeCategory selectById(@Param("id") Long id);

    EduKnowledgeCategory selectByNameAndParentId(@Param("name") String name, @Param("parentId") Long parentId);

    /**
     * 批量查询分类（用于N+1优化）
     *
     * @param ids 分类ID集合
     * @return 分类列表
     */
    List<EduKnowledgeCategory> selectBatchByIds(@Param("ids") List<Long> ids);

    int countByParentId(@Param("parentId") Long parentId);

    int insert(EduKnowledgeCategory category);

    int updateById(EduKnowledgeCategory category);

    int deleteById(@Param("id") Long id);
}
