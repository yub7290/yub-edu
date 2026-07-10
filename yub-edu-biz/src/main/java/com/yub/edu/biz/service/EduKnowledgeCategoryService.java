package com.yub.edu.biz.service;

import com.yub.edu.biz.entity.EduKnowledgeCategory;

import java.util.List;

/**
 * 知识点分类服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点分类管理服务
 * @Version: 1.0.0
 */
public interface EduKnowledgeCategoryService {

    List<EduKnowledgeCategory> selectTree();

    EduKnowledgeCategory getDetail(Long id);

    Long create(String name, String description, Long parentId, Integer sort);

    void update(Long id, String name, String description, Long parentId, Integer sort);

    void delete(Long id);

    /**
     * 根据ID批量查询分类
     *
     * @param ids 分类ID列表
     * @return 分类列表
     */
    List<EduKnowledgeCategory> selectBatchByIds(List<Long> ids);
}
