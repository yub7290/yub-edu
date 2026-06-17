package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduKnowledgeCategory;
import com.yub.edu.biz.service.EduKnowledgeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识点分类 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点分类管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/knowledge/category")
@RequiredArgsConstructor
public class EduKnowledgeCategoryController {

    private final EduKnowledgeCategoryService service;

    @GetMapping("/tree")
    public Response<List<EduKnowledgeCategory>> tree() {
        return Response.success(service.selectTree());
    }

    @GetMapping("/{id}")
    public Response<EduKnowledgeCategory> getDetail(@PathVariable Long id) {
        return Response.success(service.getDetail(id));
    }

    @PostMapping
    public Response<Long> create(@RequestParam String name,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(defaultValue = "0") Long parentId,
                                 @RequestParam(defaultValue = "0") Integer sort) {
        return Response.success(service.create(name, description, parentId, sort));
    }

    @PutMapping("/{id}")
    public Response<Void> update(@PathVariable Long id,
                                 @RequestParam String name,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(defaultValue = "0") Long parentId,
                                 @RequestParam(defaultValue = "0") Integer sort) {
        service.update(id, name, description, parentId, sort);
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Response.success();
    }
}
