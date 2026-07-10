package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 新闻资讯分类实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯分类实体
 * @Version: 1.0.0
 */
@Data
public class EduNewsCategory {

    /** 主键 */
    private Long id;

    /** 分类名称 */
    private String name;

    /** 排序（值越小越靠前） */
    private Integer sortOrder;

    /** 状态 0:禁用 1:启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
