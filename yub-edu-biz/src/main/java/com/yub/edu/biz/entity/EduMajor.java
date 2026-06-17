package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 专业实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 专业实体
 * @Version: 1.0.0
 */
@Data
public class EduMajor {

    /** 主键 */
    private Long id;

    /** 上级专业ID（0=顶级） */
    private Long parentId;

    /** 专业名称 */
    private String name;

    /** 别名 */
    private String alias;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 推荐 1:是 0:否 */
    private Integer recommended;

    /** 说明 */
    private String description;

    /** 展示图片URL */
    private String imageUrl;

    /** 详情（富文本） */
    private String detail;

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

    /** 子专业列表 */
    private List<EduMajor> children;

    /** 课程数（非持久化） */
    private Integer courseCount;

    /** 试题数（非持久化） */
    private Integer questionCount;

    /** 试卷数（非持久化） */
    private Integer examCount;
}
