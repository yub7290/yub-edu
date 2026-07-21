package com.yub.edu.biz.controller;

import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduShareContent;
import com.yub.edu.biz.service.EduShareContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 分享内容管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-20
 * @Description: 分享内容管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/share-content")
@RequiredArgsConstructor
public class EduShareContentController {

    private final EduShareContentService shareContentService;

    @PostMapping
    public Response<Long> create(@RequestBody EduShareContent content) {
        Long id = shareContentService.create(content);
        return Response.success(id);
    }

    @PutMapping("/{id}")
    public Response<Void> update(@PathVariable Long id, @RequestBody EduShareContent content) {
        shareContentService.update(id, content);
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        shareContentService.delete(id);
        return Response.success();
    }

    @GetMapping("/{id}")
    public Response<EduShareContent> getById(@PathVariable Long id) {
        EduShareContent content = shareContentService.getById(id);
        return Response.success(content);
    }

    @GetMapping("/page")
    public Response<PageResult<EduShareContent>> page(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<EduShareContent> page = shareContentService.page(title, status, pageNum, pageSize);
        return Response.success(page);
    }
}