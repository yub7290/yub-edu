package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识点分类实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点分类（树形结构）
 * @Version: 1.0.0
 */
@Data
public class EduKnowledgeCategory {

    /** 主键 */
    private Long id;

    /** 上级分类ID（0=顶级） */
    private Long parentId;

    /** 分类名称 */
    private String name;

    /** 简介 */
    private String description;

    /** 排序 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人 */
    private Long createBy;

    /** 更新人 */
    private Long updateBy;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;

    /** 子分类列表 */
    private List<EduKnowledgeCategory> children;
}
