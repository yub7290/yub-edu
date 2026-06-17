package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.framework.security.SecurityUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 章节管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 章节管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/chapter")
@RequiredArgsConstructor
public class EduChapterController {

    private final EduChapterMapper eduChapterMapper;

    /**
     * 获取课程的章节树
     *
     * @param courseId 课程ID
     * @return 章节树
     */
    @GetMapping("/tree/{courseId}")
    public Response<List<EduChapter>> tree(@PathVariable Long courseId) {
        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(courseId);
        return Response.success(buildChapterTree(chapters));
    }

    /**
     * 获取章节详情
     *
     * @param id 章节ID
     * @return 章节详情
     */
    @GetMapping("/{id}")
    public Response<EduChapter> getDetail(@PathVariable Long id) {
        EduChapter chapter = eduChapterMapper.selectById(id);
        if (chapter == null) {
            throw new EduException(EduErrorCode.CHAPTER_NOT_FOUND);
        }
        return Response.success(chapter);
    }

    /**
     * 新增章节
     *
     * @param dto 新增参数
     * @return 章节ID
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Response<Long> create(@RequestBody @NotNull ChapterCreateReqDTO dto) {
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
        return Response.success(chapter.getId());
    }

    /**
     * 编辑章节
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> update(@RequestBody @NotNull ChapterUpdateReqDTO dto) {
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
        return Response.success();
    }

    /**
     * 删除章节
     *
     * @param id 章节ID
     * @return 响应
     */
    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> delete(@PathVariable Long id) {
        EduChapter chapter = eduChapterMapper.selectById(id);
        if (chapter == null) {
            throw new EduException(EduErrorCode.CHAPTER_NOT_FOUND);
        }
        int childCount = eduChapterMapper.countByParentId(id);
        if (childCount > 0) {
            throw new EduException(EduErrorCode.MAJOR_HAS_CHILDREN);
        }
        eduChapterMapper.deleteById(id);
        return Response.success();
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

    @Data
    public static class ChapterCreateReqDTO {
        @NotNull(message = "课程ID不能为空")
        private Long courseId;
        private Long parentId = 0L;
        @NotBlank(message = "章节名称不能为空")
        private String name;
        private Integer status = 1;
        private Integer isCompleted = 0;
        private Integer isFree = 0;
        private String contentText;
        private Integer isLive = 0;
        private LocalDateTime liveStartTime;
        private Integer liveDuration;
        private Integer sort = 0;
    }

    @Data
    public static class ChapterUpdateReqDTO {
        @NotNull(message = "章节ID不能为空")
        private Long id;
        private Long parentId = 0L;
        @NotBlank(message = "章节名称不能为空")
        private String name;
        private Integer status = 1;
        private Integer isCompleted = 0;
        private Integer isFree = 0;
        private String contentText;
        private Integer isLive = 0;
        private LocalDateTime liveStartTime;
        private Integer liveDuration;
        private Integer sort = 0;
    }
}
