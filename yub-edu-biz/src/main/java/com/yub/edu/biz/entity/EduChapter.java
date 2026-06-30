package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 章节实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 章节实体
 * @Version: 1.0.0
 */
@Data
public class EduChapter {

    /** 主键 */
    private Long id;

    /** 所属课程ID */
    private Long courseId;

    /** 上级章节ID（0=顶级） */
    private Long parentId;

    /** 章节名称 */
    private String name;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 是否完结 1:是 0:否 */
    private Integer isCompleted;

    /** 是否免费 1:是 0:否 */
    private Integer isFree;

    /** 图文资料（富文本） */
    private String contentText;

    /** 是否直播课 1:是 0:否 */
    private Integer isLive;

    /** 直播开始时间 */
    private LocalDateTime liveStartTime;

    /** 直播时长（分钟） */
    private Integer liveDuration;

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

    /** 子章节列表 */
    private List<EduChapter> children;

    /** 关联知识点ID列表（非持久化） */
    private List<Long> knowledgePointIds;
}
