package com.yub.edu.biz.controller.app;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.NewsQueryDTO;
import com.yub.edu.biz.service.EduNewsCategoryService;
import com.yub.edu.biz.service.EduNewsService;
import com.yub.edu.biz.vo.NewsCategoryVO;
import com.yub.edu.biz.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学员端新闻资讯 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 学员端新闻资讯接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/app/news")
@RequiredArgsConstructor
public class AppNewsController {

    private final EduNewsService eduNewsService;
    private final EduNewsCategoryService eduNewsCategoryService;

    /**
     * 资讯分类列表（学员端 Tab 使用）
     */
    @GetMapping("/categories")
    public Response<List<NewsCategoryVO>> categories() {
        return Response.success(eduNewsCategoryService.list());
    }

    /**
     * 资讯列表（分页，仅返回已发布，可按分类筛选）
     */
    @PostMapping("/page")
    public Response<PageResult<NewsVO>> page(@RequestBody PageQuery<NewsQueryDTO> pageQuery) {
        NewsQueryDTO queryParam = pageQuery.getQueryParam();
        Long categoryId = queryParam != null ? queryParam.getCategoryId() : null;
        return Response.success(eduNewsService.appPage(
                categoryId,
                pageQuery.getPageParam().getPageNum(),
                pageQuery.getPageParam().getPageSize()));
    }

    /**
     * 资讯详情（阅读量 +1）
     */
    @GetMapping("/{id}")
    public Response<NewsVO> getDetail(@PathVariable("id") Long id) {
        return Response.success(eduNewsService.appDetail(id));
    }
}
