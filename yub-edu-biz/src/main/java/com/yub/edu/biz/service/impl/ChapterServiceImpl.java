package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.ChapterCreateReqDTO;
import com.yub.edu.biz.dto.ChapterUpdateReqDTO;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduChapterAttachment;
import com.yub.edu.biz.entity.EduChapterKnowledgePoint;
import com.yub.edu.biz.entity.EduChapterVideo;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduChapterAttachmentMapper;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.mapper.EduChapterKnowledgePointMapper;
import com.yub.edu.biz.mapper.EduChapterVideoMapper;
import com.yub.edu.biz.service.ChapterService;
import com.yub.edu.biz.service.VideoTranscodeService;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final EduChapterKnowledgePointMapper eduChapterKnowledgePointMapper;
    private final EduChapterVideoMapper eduChapterVideoMapper;
    private final EduChapterAttachmentMapper eduChapterAttachmentMapper;
    private final VideoTranscodeService videoTranscodeService;

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
        // 加载关联知识点ID列表
        chapter.setKnowledgePointIds(eduChapterKnowledgePointMapper.selectKnowledgePointIdsByChapterId(id));
        List<EduChapterVideo> videos = eduChapterVideoMapper.selectByChapterId(id);
        videos.forEach(video -> video.setPlaybackType(
                video.getVideoUrl() != null && video.getVideoUrl().toLowerCase().contains(".m3u8") ? "hls" : "file"));
        chapter.setVideoList(videos);
        chapter.setAttachmentList(eduChapterAttachmentMapper.selectByChapterId(id));
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

        // 保存章节-知识点关联
        if (dto.getKnowledgePointIds() != null && !dto.getKnowledgePointIds().isEmpty()) {
            saveChapterKnowledgePoints(chapter.getId(), dto.getKnowledgePointIds(), userId);
        }
        saveChapterVideos(chapter.getId(), dto.getVideoList(), userId, Collections.emptyList());
        saveChapterAttachments(chapter.getId(), dto.getAttachmentList(), userId);

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

        // 全量覆盖章节-知识点关联
        if (dto.getKnowledgePointIds() != null) {
            eduChapterKnowledgePointMapper.deleteByChapterId(dto.getId());
            if (!dto.getKnowledgePointIds().isEmpty()) {
                saveChapterKnowledgePoints(dto.getId(), dto.getKnowledgePointIds(), chapter.getUpdateBy());
            }
        }
        if (dto.getVideoList() != null) {
            List<EduChapterVideo> existingVideos = eduChapterVideoMapper.selectByChapterId(dto.getId());
            Set<Long> retainedAssetIds = dto.getVideoList().stream()
                    .filter(video -> video != null && video.getAssetId() != null)
                    .map(EduChapterVideo::getAssetId)
                    .collect(Collectors.toSet());
            Set<Long> removedAssetIds = existingVideos.stream()
                    .map(EduChapterVideo::getAssetId)
                    .filter(assetId -> assetId != null && !retainedAssetIds.contains(assetId))
                    .collect(Collectors.toCollection(HashSet::new));
            eduChapterVideoMapper.deleteByChapterId(dto.getId());
            saveChapterVideos(dto.getId(), dto.getVideoList(), chapter.getUpdateBy(), existingVideos);
            removedAssetIds.forEach(videoTranscodeService::deleteIfOrphaned);
        }
        if (dto.getAttachmentList() != null) {
            eduChapterAttachmentMapper.deleteByChapterId(dto.getId());
            saveChapterAttachments(dto.getId(), dto.getAttachmentList(), chapter.getUpdateBy());
        }
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
        List<Long> assetIds = eduChapterVideoMapper.selectByChapterId(id).stream()
                .map(EduChapterVideo::getAssetId)
                .filter(assetId -> assetId != null)
                .distinct()
                .collect(Collectors.toList());
        eduChapterMapper.deleteById(id);
        // 删除章节-知识点关联
        eduChapterKnowledgePointMapper.deleteByChapterId(id);
        eduChapterVideoMapper.deleteByChapterId(id);
        eduChapterAttachmentMapper.deleteByChapterId(id);
        assetIds.forEach(videoTranscodeService::deleteIfOrphaned);
    }

    @Override
    public List<Long> getKnowledgePointIds(Long chapterId) {
        return eduChapterKnowledgePointMapper.selectKnowledgePointIdsByChapterId(chapterId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateKnowledgePoints(Long chapterId, List<Long> knowledgePointIds) {
        eduChapterKnowledgePointMapper.deleteByChapterId(chapterId);
        if (knowledgePointIds != null && !knowledgePointIds.isEmpty()) {
            Long userId = SecurityUtils.getCurrentUserId();
            saveChapterKnowledgePoints(chapterId, knowledgePointIds, userId);
        }
    }

    /**
     * 批量保存章节-知识点关联
     */
    private void saveChapterKnowledgePoints(Long chapterId, List<Long> knowledgePointIds, Long userId) {
        List<EduChapterKnowledgePoint> list = new ArrayList<>();
        for (int i = 0; i < knowledgePointIds.size(); i++) {
            EduChapterKnowledgePoint item = new EduChapterKnowledgePoint();
            item.setChapterId(chapterId);
            item.setKnowledgePointId(knowledgePointIds.get(i));
            item.setSort(i);
            item.setCreateBy(userId);
            list.add(item);
        }
        eduChapterKnowledgePointMapper.batchInsert(list);
    }

    /**
     * 批量保存章节视频
     */
    private void saveChapterVideos(Long chapterId, List<EduChapterVideo> videoList, Long userId,
                                   List<EduChapterVideo> existingVideos) {
        if (videoList == null || videoList.isEmpty()) {
            return;
        }
        Map<Long, EduChapterVideo> existingById = existingVideos.stream()
                .filter(video -> video.getId() != null)
                .collect(Collectors.toMap(EduChapterVideo::getId, video -> video, (a, b) -> a));
        List<EduChapterVideo> list = new ArrayList<>();
        for (int i = 0; i < videoList.size(); i++) {
            EduChapterVideo source = videoList.get(i);
            if (source == null) continue;
            EduChapterVideo item = new EduChapterVideo();
            item.setChapterId(chapterId);
            if (source.getAssetId() != null) {
                var asset = videoTranscodeService.getReadyAsset(source.getAssetId(), userId);
                item.setAssetId(asset.getId());
                item.setVideoUrl(asset.getHlsUrl());
                item.setFileSize(asset.getFileSize());
                item.setVideoName(source.getVideoName() != null && !source.getVideoName().isBlank()
                        ? source.getVideoName() : asset.getOriginalName());
            } else {
                // 只允许原样保留本章已有的存量直链，禁止浏览器提交任意播放URL。
                EduChapterVideo legacy = source.getId() == null ? null : existingById.get(source.getId());
                if (legacy == null || source.getVideoUrl() == null
                        || !source.getVideoUrl().equals(legacy.getVideoUrl())) {
                    throw new com.yub.common.exception.BaseException(EduErrorCode.VIDEO_TRANSCODE_NOT_READY);
                }
                item.setVideoUrl(legacy.getVideoUrl());
                item.setFileSize(legacy.getFileSize());
                item.setVideoName(source.getVideoName() != null && !source.getVideoName().isBlank()
                        ? source.getVideoName() : legacy.getVideoName());
            }
            item.setSort(i);
            item.setCreateBy(userId);
            list.add(item);
        }
        if (!list.isEmpty()) {
            eduChapterVideoMapper.batchInsert(list);
        }
    }

    /**
     * 批量保存章节附件
     */
    private void saveChapterAttachments(Long chapterId, List<EduChapterAttachment> attachmentList, Long userId) {
        if (attachmentList == null || attachmentList.isEmpty()) {
            return;
        }
        List<EduChapterAttachment> list = new ArrayList<>();
        for (int i = 0; i < attachmentList.size(); i++) {
            EduChapterAttachment source = attachmentList.get(i);
            if (source == null || source.getFileUrl() == null || source.getFileUrl().isBlank()) {
                continue;
            }
            EduChapterAttachment item = new EduChapterAttachment();
            item.setChapterId(chapterId);
            item.setFileName(source.getFileName() != null && !source.getFileName().isBlank()
                    ? source.getFileName()
                    : "附件" + (i + 1));
            item.setFileUrl(source.getFileUrl());
            item.setFileSize(source.getFileSize());
            item.setFileType(source.getFileType());
            item.setSort(i);
            item.setCreateBy(userId);
            list.add(item);
        }
        if (!list.isEmpty()) {
            eduChapterAttachmentMapper.batchInsert(list);
        }
    }

    @Override
    public List<EduChapterKnowledgePoint> selectByChapterIds(List<Long> chapterIds) {
        if (chapterIds == null || chapterIds.isEmpty()) {
            return new ArrayList<>();
        }
        return eduChapterKnowledgePointMapper.selectByChapterIds(chapterIds);
    }

    @Override
    public List<EduChapter> selectFlatByCourseId(Long courseId) {
        return eduChapterMapper.selectTreeByCourseId(courseId);
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
        normalizeChildren(roots);
        return roots;
    }

    /**
     * 兜底：确保叶子节点的 children 为空列表而非 null（避免前端 el-tree 无法区分"无子节点"和"未加载"）
     */
    private void normalizeChildren(List<EduChapter> nodes) {
        for (EduChapter node : nodes) {
            if (node.getChildren() == null) {
                node.setChildren(Collections.emptyList());
            } else {
                normalizeChildren(node.getChildren());
            }
        }
    }
}
