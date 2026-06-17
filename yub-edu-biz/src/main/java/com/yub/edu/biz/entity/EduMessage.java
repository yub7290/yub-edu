package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 留言实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 留言实体
 * @Version: 1.0.0
 */
@Data
public class EduMessage {

    /** 主键 */
    private Long id;

    /** 所属课程ID */
    private Long courseId;

    /** 上级留言ID（回复用） */
    private Long parentId;

    /** 留言用户ID */
    private Long userId;

    /** 留言内容 */
    private String content;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
