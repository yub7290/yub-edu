package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.KnowledgeRelationCreateReqDTO;
import com.yub.edu.biz.dto.KnowledgeRelationUpdateReqDTO;
import com.yub.edu.biz.entity.EduKnowledgeRelation;
import com.yub.edu.biz.service.EduKnowledgeRelationService;
import com.yub.edu.biz.vo.KnowledgeRelationVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/knowledge/relation")
@RequiredArgsConstructor
public class EduKnowledgeRelationController {

    private final EduKnowledgeRelationService service;

    @GetMapping("/list/{knowledgeId}")
    public Response<List<KnowledgeRelationVO>> listByKnowledgeId(@PathVariable("knowledgeId") Long knowledgeId) {
        return Response.success(service.getRelationsByKnowledgeId(knowledgeId));
    }

    @GetMapping("/all")
    public Response<List<KnowledgeRelationVO>> listAll() {
        return Response.success(service.getAllRelations());
    }

    @Log(value = "新增知识点关系", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody KnowledgeRelationCreateReqDTO dto) {
        return Response.success(service.create(dto));
    }

    @Log(value = "编辑知识点关系", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody KnowledgeRelationUpdateReqDTO dto) {
        service.update(dto);
        return Response.success();
    }

    @Log(value = "删除知识点关系", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return Response.success();
    }
}