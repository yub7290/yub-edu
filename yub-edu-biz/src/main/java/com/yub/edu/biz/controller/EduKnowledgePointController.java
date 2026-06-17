package com.yub.edu.biz.controller;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.KnowledgePointCreateReqDTO;
import com.yub.edu.biz.dto.KnowledgePointQueryDTO;
import com.yub.edu.biz.dto.KnowledgePointUpdateReqDTO;
import com.yub.edu.biz.entity.EduKnowledgePoint;
import com.yub.edu.biz.service.EduKnowledgePointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;

/**
 * 知识点 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/knowledge/point")
@RequiredArgsConstructor
public class EduKnowledgePointController {

    private final EduKnowledgePointService service;

    @PostMapping("/page")
    public Response<PageResult<EduKnowledgePoint>> page(@RequestBody PageQuery<KnowledgePointQueryDTO> pageQuery) {
        return Response.success(service.page(pageQuery));
    }

    @GetMapping("/list-by-category/{categoryId}")
    public Response<List<EduKnowledgePoint>> listByCategory(@PathVariable Long categoryId) {
        return Response.success(service.listByCategoryId(categoryId));
    }

    @GetMapping("/{id}")
    public Response<EduKnowledgePoint> getDetail(@PathVariable Long id) {
        return Response.success(service.getDetail(id));
    }

    @PostMapping
    public Response<Long> create(@Valid @RequestBody KnowledgePointCreateReqDTO dto) {
        return Response.success(service.create(dto));
    }

    @PutMapping
    public Response<Void> update(@Valid @RequestBody KnowledgePointUpdateReqDTO dto) {
        service.update(dto);
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Response.success();
    }
}
