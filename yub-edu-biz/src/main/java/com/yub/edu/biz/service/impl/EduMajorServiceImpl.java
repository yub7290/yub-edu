package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.MajorCreateReqDTO;
import com.yub.edu.biz.dto.MajorQueryDTO;
import com.yub.edu.biz.dto.MajorSortReqDTO;
import com.yub.edu.biz.dto.MajorUpdateReqDTO;
import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduMajorMapper;
import com.yub.edu.biz.service.EduMajorService;
import com.yub.edu.biz.vo.EduMajorCountVO;
import com.yub.edu.biz.vo.MajorDetailRespVO;
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
 * 专业服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 专业管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduMajorServiceImpl implements EduMajorService {

    private final EduMajorMapper eduMajorMapper;

    @Override
    public List<EduMajor> selectTree() {
        List<EduMajor> allMajors = eduMajorMapper.selectTree();
        return buildMajorTree(allMajors);
    }

    @Override
    public List<EduMajor> selectTreeByCondition(MajorQueryDTO query) {
        List<EduMajor> allMajors = eduMajorMapper.selectList(query);
        boolean hasFilter = (query.getName() != null && !query.getName().isEmpty())
                || query.getStatus() != null;
        if (hasFilter) {
            return buildFilteredTree(allMajors);
        }
        return buildMajorTree(allMajors);
    }

    @Override
    public MajorDetailRespVO getDetail(Long id) {
        EduMajor major = eduMajorMapper.selectById(id);
        if (major == null) {
            throw new EduException(EduErrorCode.MAJOR_NOT_FOUND);
        }

        String parentName = null;
        if (major.getParentId() != null && major.getParentId() != 0L) {
            EduMajor parent = eduMajorMapper.selectById(major.getParentId());
            if (parent != null) {
                parentName = parent.getName();
            }
        }

        return MajorDetailRespVO.builder()
                .id(major.getId())
                .parentId(major.getParentId())
                .parentName(parentName)
                .name(major.getName())
                .alias(major.getAlias())
                .status(major.getStatus())
                .recommended(major.getRecommended())
                .description(major.getDescription())
                .imageUrl(major.getImageUrl())
                .detail(major.getDetail())
                .sort(major.getSort())
                .createTime(major.getCreateTime())
                .updateTime(major.getUpdateTime())
                .createBy(major.getCreateBy())
                .updateBy(major.getUpdateBy())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(MajorCreateReqDTO dto) {
        checkNameUnique(dto.getName(), dto.getParentId(), null);

        EduMajor major = new EduMajor();
        major.setParentId(dto.getParentId());
        major.setName(dto.getName());
        major.setAlias(dto.getAlias());
        major.setStatus(dto.getStatus());
        major.setRecommended(dto.getRecommended() != null ? dto.getRecommended() : 0);
        major.setDescription(dto.getDescription());
        major.setImageUrl(dto.getImageUrl());
        major.setDetail(dto.getDetail());
        major.setSort(dto.getSort() != null ? dto.getSort() : 0);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        major.setCreateBy(currentUserId);
        major.setUpdateBy(currentUserId);
        eduMajorMapper.insert(major);
        return major.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MajorUpdateReqDTO dto) {
        EduMajor major = eduMajorMapper.selectById(dto.getId());
        if (major == null) {
            throw new EduException(EduErrorCode.MAJOR_NOT_FOUND);
        }

        checkNameUnique(dto.getName(), dto.getParentId(), dto.getId());

        major.setParentId(dto.getParentId());
        major.setName(dto.getName());
        major.setAlias(dto.getAlias());
        major.setStatus(dto.getStatus());
        major.setRecommended(dto.getRecommended() != null ? dto.getRecommended() : 0);
        major.setDescription(dto.getDescription());
        major.setImageUrl(dto.getImageUrl());
        major.setDetail(dto.getDetail());
        major.setSort(dto.getSort() != null ? dto.getSort() : 0);
        major.setUpdateBy(SecurityUtils.getCurrentUserId());
        eduMajorMapper.updateById(major);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduMajor major = eduMajorMapper.selectById(id);
        if (major == null) {
            throw new EduException(EduErrorCode.MAJOR_NOT_FOUND);
        }

        int childCount = eduMajorMapper.countByParentId(id);
        if (childCount > 0) {
            throw new EduException(EduErrorCode.MAJOR_HAS_CHILDREN);
        }

        int courseCount = eduMajorMapper.countCoursesByMajorId(id);
        if (courseCount > 0) {
            throw new EduException(EduErrorCode.MAJOR_HAS_COURSES);
        }

        eduMajorMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        EduMajor major = eduMajorMapper.selectById(id);
        if (major == null) {
            throw new EduException(EduErrorCode.MAJOR_NOT_FOUND);
        }
        eduMajorMapper.updateStatus(id, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeRecommended(Long id, Integer recommended) {
        EduMajor major = eduMajorMapper.selectById(id);
        if (major == null) {
            throw new EduException(EduErrorCode.MAJOR_NOT_FOUND);
        }
        eduMajorMapper.updateRecommended(id, recommended);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSortBatch(List<MajorSortReqDTO.MajorSortItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        // 建立本次提交的 id -> item 映射，便于向上回溯父级
        Map<Long, MajorSortReqDTO.MajorSortItem> itemMap = items.stream()
                .collect(Collectors.toMap(MajorSortReqDTO.MajorSortItem::getId, i -> i, (a, b) -> a));

        for (MajorSortReqDTO.MajorSortItem item : items) {
            EduMajor major = eduMajorMapper.selectById(item.getId());
            if (major == null) {
                throw new EduException(EduErrorCode.MAJOR_NOT_FOUND);
            }

            Long parentId = item.getParentId() == null ? 0L : item.getParentId();

            // 不能移动到自身下
            if (parentId.equals(item.getId())) {
                throw new EduException(EduErrorCode.MAJOR_CYCLE);
            }

            // 不能移动到自身的子孙节点下（避免循环引用）
            Long cursor = parentId;
            while (cursor != null && cursor != 0L) {
                if (cursor.equals(item.getId())) {
                    throw new EduException(EduErrorCode.MAJOR_CYCLE);
                }
                MajorSortReqDTO.MajorSortItem parentItem = itemMap.get(cursor);
                if (parentItem != null) {
                    cursor = parentItem.getParentId() == null ? 0L : parentItem.getParentId();
                } else {
                    // 父级不在本次提交中，向上查库校验
                    EduMajor parent = eduMajorMapper.selectById(cursor);
                    cursor = (parent != null && parent.getParentId() != null) ? parent.getParentId() : 0L;
                }
            }

            eduMajorMapper.updateParentAndSort(item.getId(), parentId, item.getSort());
        }
    }

    private void checkNameUnique(String name, Long parentId, Long excludeId) {
        EduMajor exist = eduMajorMapper.selectByNameAndParentId(name, parentId);
        if (exist != null && (excludeId == null || !exist.getId().equals(excludeId))) {
            throw new EduException(EduErrorCode.MAJOR_NAME_EXISTS);
        }
    }

    private List<EduMajor> buildMajorTree(List<EduMajor> majors) {
        Map<Long, EduMajor> majorMap = majors.stream()
                .collect(Collectors.toMap(EduMajor::getId, m -> m, (a, b) -> a));
        List<EduMajor> roots = new ArrayList<>();
        for (EduMajor major : majors) {
            if (major.getParentId() == null || major.getParentId() == 0L) {
                roots.add(major);
            } else {
                EduMajor parent = majorMap.get(major.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(major);
                }
            }
        }
        sortMajorsRecursively(roots);
        populateCountsRecursively(roots);
        return roots;
    }

    private List<EduMajor> buildFilteredTree(List<EduMajor> filteredMajors) {
        Map<Long, EduMajor> filteredMap = filteredMajors.stream()
                .collect(Collectors.toMap(EduMajor::getId, m -> m, (a, b) -> a));

        List<Long> allAncestorIds = new ArrayList<>();
        for (EduMajor major : filteredMajors) {
            Long parentId = major.getParentId();
            if (parentId != null && parentId != 0L && !filteredMap.containsKey(parentId) && !allAncestorIds.contains(parentId)) {
                allAncestorIds.add(parentId);
            }
        }

        if (!allAncestorIds.isEmpty()) {
            List<Long> ancestorIds = new ArrayList<>(allAncestorIds);
            boolean foundNew = true;
            while (foundNew) {
                foundNew = false;
                List<Long> toQuery = new ArrayList<>(ancestorIds);
                for (Long pid : toQuery) {
                    EduMajor ancestor = eduMajorMapper.selectById(pid);
                    if (ancestor != null) {
                        filteredMap.putIfAbsent(ancestor.getId(), ancestor);
                        Long upperPid = ancestor.getParentId();
                        if (upperPid != null && upperPid != 0L && !filteredMap.containsKey(upperPid) && !ancestorIds.contains(upperPid)) {
                            ancestorIds.add(upperPid);
                            foundNew = true;
                        }
                    }
                }
            }
        }

        List<EduMajor> allNodes = new ArrayList<>(filteredMap.values());
        return buildMajorTree(allNodes);
    }

    private void sortMajorsRecursively(List<EduMajor> majors) {
        majors.sort(Comparator.comparing(EduMajor::getSort, Comparator.nullsLast(Comparator.naturalOrder())));
        majors.forEach(major -> {
            if (major.getChildren() != null && !major.getChildren().isEmpty()) {
                sortMajorsRecursively(major.getChildren());
            }
        });
    }

    /**
     * 递归填充统计数量（批量查询优化，减少 N+1）
     */
    private void populateCountsRecursively(List<EduMajor> majors) {
        // 第一步：收集所有节点 ID
        List<Long> allIds = new ArrayList<>();
        collectIds(majors, allIds);
        if (allIds.isEmpty()) return;

        // 第二步：批量查询统计数据
        Map<Long, EduMajorCountVO> countMap = eduMajorMapper.selectCountsByMajorIds(allIds)
                .stream().collect(Collectors.toMap(EduMajorCountVO::getMajorId, c -> c));

        // 第三步：回填统计值
        fillCounts(majors, countMap);
    }

    private void collectIds(List<EduMajor> majors, List<Long> collector) {
        for (EduMajor major : majors) {
            collector.add(major.getId());
            if (major.getChildren() != null && !major.getChildren().isEmpty()) {
                collectIds(major.getChildren(), collector);
            }
        }
    }

    private void fillCounts(List<EduMajor> majors, Map<Long, EduMajorCountVO> countMap) {
        for (EduMajor major : majors) {
            EduMajorCountVO vo = countMap.get(major.getId());
            if (vo != null) {
                major.setCourseCount(vo.getCourseCount());
                major.setQuestionCount(vo.getQuestionCount());
                major.setExamCount(vo.getExamCount());
            }
            if (major.getChildren() != null && !major.getChildren().isEmpty()) {
                fillCounts(major.getChildren(), countMap);
            }
        }
    }
}
