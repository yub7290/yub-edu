package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识点实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点
 * @Version: 1.0.0
 */
@Data
public class EduKnowledgePoint {

    /** 主键 */
    private Long id;

    /** 所属分类ID */
    private Long categoryId;

    /** 所属课程ID */
    private Long courseId;

    /** 标题 */
    private String title;

    /** 内容（富文本） */
    private String content;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

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
}
