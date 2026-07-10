package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.ChapterCreateReqDTO;
import com.yub.edu.biz.dto.ChapterUpdateReqDTO;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.service.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Response<List<EduChapter>> tree(@PathVariable("courseId") Long courseId) {
        return Response.success(chapterService.getTree(courseId));
    }

    /**
     * 获取章节详情
     *
     * @param id 章节ID
     * @return 章节详情
     */
    @GetMapping("/{id}")
    public Response<EduChapter> getDetail(@PathVariable("id") Long id) {
        return Response.success(chapterService.getDetail(id));
    }

    /**
     * 新增章节
     *
     * @param dto 新增参数
     * @return 章节ID
     */
    @Log(value = "新增章节", type = "CREATE")
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
    @Log(value = "编辑章节", type = "UPDATE")
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
    @Log(value = "删除章节", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        chapterService.delete(id);
        return Response.success();
    }

    /**
     * 获取章节关联的知识点ID列表
     *
     * @param id 章节ID
     * @return 知识点ID列表
     */
    @GetMapping("/{id}/knowledge")
    public Response<List<Long>> getKnowledge(@PathVariable("id") Long id) {
        return Response.success(chapterService.getKnowledgePointIds(id));
    }

    /**
     * 更新章节关联的知识点
     *
     * @param id               章节ID
     * @param knowledgePointIds 知识点ID列表
     * @return 操作结果
     */
    @Log(value = "更新章节关联知识点", type = "UPDATE")
    @PutMapping("/{id}/knowledge")
    public Response<Void> updateKnowledge(@PathVariable("id") Long id, @RequestBody List<Long> knowledgePointIds) {
        chapterService.updateKnowledgePoints(id, knowledgePointIds);
        return Response.success();
    }
}
