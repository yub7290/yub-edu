package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.NewsCreateReqDTO;
import com.yub.edu.biz.dto.NewsQueryDTO;
import com.yub.edu.biz.dto.NewsUpdateReqDTO;
import com.yub.edu.biz.vo.NewsVO;

/**
 * 新闻资讯服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯服务
 * @Version: 1.0.0
 */
public interface EduNewsService {

    /**
     * 分页查询新闻（管理端）
     */
    PageResult<NewsVO> page(PageQuery<NewsQueryDTO> pageQuery);

    /**
     * 获取新闻详情（管理端）
     */
    NewsVO getDetail(Long id);

    /**
     * 新增新闻
     */
    Long create(NewsCreateReqDTO dto);

    /**
     * 编辑新闻
     */
    void update(NewsUpdateReqDTO dto);

    /**
     * 删除新闻（逻辑删除）
     */
    void delete(Long id);

    /**
     * 学员端新闻列表（仅返回已发布）
     *
     * @param categoryId 分类ID（可选）
     * @param pageNum    页码
     * @param pageSize  每页大小
     */
    PageResult<NewsVO> appPage(Long categoryId, int pageNum, int pageSize);

    /**
     * 学员端新闻详情（阅读量 +1）
     */
    NewsVO appDetail(Long id);
}
