package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学员组实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组实体
 * @Version: 1.0.0
 */
@Data
public class EduStudentGroup {
    /**
     * 主键
     */
    private Long id;
    /**
     * 学员组名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 排序
     */
    private Integer sortOrder;
    /**
     * 状态（1=启用 0=禁用）
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 逻辑删除（0=正常 1=已删除）
     */
    private Integer deleted;

    /**
     * 成员数（非持久化，分页查询填充）
     */
    private Integer memberCount;

    /**
     * 课程数（非持久化，分页查询填充）
     */
    private Integer courseCount;
}
