package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 新闻资讯查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯查询参数
 * @Version: 1.0.0
 */
@Data
public class NewsQueryDTO {

    /** 标题（模糊搜索） */
    private String title;

    /** 分类ID */
    private Long categoryId;

    /** 状态 0:草稿 1:已发布 */
    private Integer status;
}
