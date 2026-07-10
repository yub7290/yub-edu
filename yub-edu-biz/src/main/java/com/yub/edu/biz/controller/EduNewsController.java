package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.NewsCreateReqDTO;
import com.yub.edu.biz.dto.NewsQueryDTO;
import com.yub.edu.biz.dto.NewsUpdateReqDTO;
import com.yub.edu.biz.service.EduNewsService;
import com.yub.edu.biz.vo.NewsVO;
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

/**
 * 新闻资讯管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/news")
@RequiredArgsConstructor
public class EduNewsController {

    private final EduNewsService eduNewsService;

    @PostMapping("/page")
    public Response<PageResult<NewsVO>> page(@RequestBody PageQuery<NewsQueryDTO> pageQuery) {
        return Response.success(eduNewsService.page(pageQuery));
    }

    @GetMapping("/{id}")
    public Response<NewsVO> getDetail(@PathVariable("id") Long id) {
        return Response.success(eduNewsService.getDetail(id));
    }

    @Log(value = "新增资讯", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody NewsCreateReqDTO dto) {
        return Response.success(eduNewsService.create(dto));
    }

    @Log(value = "编辑资讯", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody NewsUpdateReqDTO dto) {
        eduNewsService.update(dto);
        return Response.success();
    }

    @Log(value = "删除资讯", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        eduNewsService.delete(id);
        return Response.success();
    }
}
