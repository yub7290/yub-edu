package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.ChapterCreateReqDTO;
import com.yub.edu.biz.dto.ChapterUpdateReqDTO;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.service.ChapterService;
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
 * 章节管理 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 章节业务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final EduChapterMapper eduChapterMapper;

    @Override
    public List<EduChapter> getTree(Long courseId) {
        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(courseId);
        return buildChapterTree(chapters);
    }

    @Override
    public EduChapter getDetail(Long id) {
        EduChapter chapter = eduChapterMapper.selectById(id);
        if (chapter == null) {
            throw new EduException(EduErrorCode.CHAPTER_NOT_FOUND);
        }
        return chapter;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(ChapterCreateReqDTO dto) {
        EduChapter chapter = new EduChapter();
        chapter.setCourseId(dto.getCourseId());
        chapter.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        chapter.setName(dto.getName());
        chapter.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        chapter.setIsCompleted(dto.getIsCompleted() != null ? dto.getIsCompleted() : 0);
        chapter.setIsFree(dto.getIsFree() != null ? dto.getIsFree() : 0);
        chapter.setContentText(dto.getContentText());
        chapter.setIsLive(dto.getIsLive() != null ? dto.getIsLive() : 0);
        chapter.setLiveStartTime(dto.getLiveStartTime());
        chapter.setLiveDuration(dto.getLiveDuration());
        chapter.setSort(dto.getSort() != null ? dto.getSort() : 0);
        Long userId = SecurityUtils.getCurrentUserId();
        chapter.setCreateBy(userId);
        chapter.setUpdateBy(userId);
        eduChapterMapper.insert(chapter);
        return chapter.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ChapterUpdateReqDTO dto) {
        EduChapter chapter = eduChapterMapper.selectById(dto.getId());
        if (chapter == null) {
            throw new EduException(EduErrorCode.CHAPTER_NOT_FOUND);
        }
        chapter.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        chapter.setName(dto.getName());
        chapter.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        chapter.setIsCompleted(dto.getIsCompleted() != null ? dto.getIsCompleted() : 0);
        chapter.setIsFree(dto.getIsFree() != null ? dto.getIsFree() : 0);
        chapter.setContentText(dto.getContentText());
        chapter.setIsLive(dto.getIsLive() != null ? dto.getIsLive() : 0);
        chapter.setLiveStartTime(dto.getLiveStartTime());
        chapter.setLiveDuration(dto.getLiveDuration());
        chapter.setSort(dto.getSort() != null ? dto.getSort() : 0);
        chapter.setUpdateBy(SecurityUtils.getCurrentUserId());
        eduChapterMapper.updateById(chapter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduChapter chapter = eduChapterMapper.selectById(id);
        if (chapter == null) {
            throw new EduException(EduErrorCode.CHAPTER_NOT_FOUND);
        }
        int childCount = eduChapterMapper.countByParentId(id);
        if (childCount > 0) {
            throw new EduException(EduErrorCode.CHAPTER_HAS_CHILDREN);
        }
        eduChapterMapper.deleteById(id);
    }

    private List<EduChapter> buildChapterTree(List<EduChapter> chapters) {
        Map<Long, EduChapter> map = chapters.stream()
                .collect(Collectors.toMap(EduChapter::getId, c -> c, (a, b) -> a));
        List<EduChapter> roots = new ArrayList<>();
        for (EduChapter ch : chapters) {
            if (ch.getParentId() == null || ch.getParentId() == 0L) {
                roots.add(ch);
            } else {
                EduChapter parent = map.get(ch.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) parent.setChildren(new ArrayList<>());
                    parent.getChildren().add(ch);
                }
            }
        }
        roots.sort(Comparator.comparing(EduChapter::getSort, Comparator.nullsLast(Comparator.naturalOrder())));
        return roots;
    }
}
