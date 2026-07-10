package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.NewsCategoryCreateReqDTO;
import com.yub.edu.biz.dto.NewsCategoryUpdateReqDTO;
import com.yub.edu.biz.service.EduNewsCategoryService;
import com.yub.edu.biz.vo.NewsCategoryVO;
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
 * 新闻资讯分类管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯分类管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/news/category")
@RequiredArgsConstructor
public class EduNewsCategoryController {

    private final EduNewsCategoryService eduNewsCategoryService;

    @GetMapping("/list")
    public Response<List<NewsCategoryVO>> list() {
        return Response.success(eduNewsCategoryService.list());
    }

    @Log(value = "新增资讯分类", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody NewsCategoryCreateReqDTO dto) {
        return Response.success(eduNewsCategoryService.create(dto));
    }

    @Log(value = "编辑资讯分类", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody NewsCategoryUpdateReqDTO dto) {
        eduNewsCategoryService.update(dto);
        return Response.success();
    }

    @Log(value = "删除资讯分类", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        eduNewsCategoryService.delete(id);
        return Response.success();
    }
}
