package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduKnowledgeCategory;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduKnowledgeCategoryMapper;
import com.yub.edu.biz.service.EduKnowledgeCategoryService;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 知识点分类服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点分类管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduKnowledgeCategoryServiceImpl implements EduKnowledgeCategoryService {

    private final EduKnowledgeCategoryMapper mapper;

    @Override
    public List<EduKnowledgeCategory> selectTree() {
        List<EduKnowledgeCategory> all = mapper.selectTree();
        return buildTree(all);
    }

    @Override
    public EduKnowledgeCategory getDetail(Long id) {
        EduKnowledgeCategory cat = mapper.selectById(id);
        if (cat == null) throw new EduException(EduErrorCode.KNOWLEDGE_POINT_NOT_FOUND);
        return cat;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(String name, String description, Long parentId, Integer sort) {
        checkNameUnique(name, parentId, null);
        EduKnowledgeCategory cat = new EduKnowledgeCategory();
        cat.setParentId(parentId != null ? parentId : 0L);
        cat.setName(name);
        cat.setDescription(description);
        cat.setSort(sort != null ? sort : 0);
        Long uid = SecurityUtils.getCurrentUserId();
        cat.setCreateBy(uid);
        cat.setUpdateBy(uid);
        mapper.insert(cat);
        return cat.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, String name, String description, Long parentId, Integer sort) {
        EduKnowledgeCategory cat = mapper.selectById(id);
        if (cat == null) throw new EduException(EduErrorCode.KNOWLEDGE_POINT_NOT_FOUND);
        checkNameUnique(name, parentId != null ? parentId : 0L, id);
        cat.setParentId(parentId != null ? parentId : 0L);
        cat.setName(name);
        cat.setDescription(description);
        cat.setSort(sort != null ? sort : 0);
        cat.setUpdateBy(SecurityUtils.getCurrentUserId());
        mapper.updateById(cat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduKnowledgeCategory cat = mapper.selectById(id);
        if (cat == null) throw new EduException(EduErrorCode.KNOWLEDGE_POINT_NOT_FOUND);
        int childCount = mapper.countByParentId(id);
        if (childCount > 0) throw new EduException(EduErrorCode.MAJOR_HAS_CHILDREN);
        mapper.deleteById(id);
    }

    @Override
    public List<EduKnowledgeCategory> selectBatchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return mapper.selectBatchByIds(ids);
    }

    private void checkNameUnique(String name, Long parentId, Long excludeId) {
        EduKnowledgeCategory exist = mapper.selectByNameAndParentId(name, parentId);
        if (exist != null && (excludeId == null || !exist.getId().equals(excludeId))) {
            throw new EduException(EduErrorCode.MAJOR_NAME_EXISTS);
        }
    }

    private List<EduKnowledgeCategory> buildTree(List<EduKnowledgeCategory> list) {
        Map<Long, EduKnowledgeCategory> map = list.stream()
                .collect(Collectors.toMap(EduKnowledgeCategory::getId, c -> c, (a, b) -> a));
        List<EduKnowledgeCategory> roots = new ArrayList<>();
        for (EduKnowledgeCategory c : list) {
            if (c.getParentId() == null || c.getParentId() == 0L) {
                roots.add(c);
            } else {
                EduKnowledgeCategory p = map.get(c.getParentId());
                if (p != null) {
                    if (p.getChildren() == null) p.setChildren(new ArrayList<>());
                    p.getChildren().add(c);
                }
            }
        }
        roots.sort(Comparator.comparing(EduKnowledgeCategory::getSort, Comparator.nullsLast(Comparator.naturalOrder())));
        return roots;
    }
}
