package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.dto.ChapterCreateReqDTO;
import com.yub.edu.biz.dto.ChapterUpdateReqDTO;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.service.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    private final ChapterService chapterService;

    /**
     * 获取课程的章节树
     *
     * @param courseId 课程ID
     * @return 章节树
     */
    @GetMapping("/tree/{courseId}")
    public Response<List<EduChapter>> tree(@PathVariable Long courseId) {
        return Response.success(chapterService.getTree(courseId));
    }

    /**
     * 获取章节详情
     *
     * @param id 章节ID
     * @return 章节详情
     */
    @GetMapping("/{id}")
    public Response<EduChapter> getDetail(@PathVariable Long id) {
        return Response.success(chapterService.getDetail(id));
    }

    /**
     * 新增章节
     *
     * @param dto 新增参数
     * @return 章节ID
     */
    @PostMapping
    public Response<Long> create(@Valid @RequestBody ChapterCreateReqDTO dto) {
        return Response.success(chapterService.create(dto));
    }

    /**
     * 编辑章节
     *
     * @param dto 编辑参数
     * @return 操作结果
     */
    @PutMapping
    public Response<Void> update(@Valid @RequestBody ChapterUpdateReqDTO dto) {
        chapterService.update(dto);
        return Response.success();
    }

    /**
     * 删除章节
     *
     * @param id 章节ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        chapterService.delete(id);
        return Response.success();
    }
}
