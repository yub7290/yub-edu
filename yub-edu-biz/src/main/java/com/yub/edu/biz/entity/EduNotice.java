package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程通知实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 课程通知实体（与课程关联，仅发给绑定了该课程的学员）
 * @Version: 1.0.0
 */
@Data
public class EduNotice {

    /** 主键 */
    private Long id;

    /** 关联课程ID */
    private Long courseId;

    /** 通知标题 */
    private String title;

    /** 通知内容 */
    private String content;

    /** 类型 1:系统通知 2:课程相关 3:考试相关 4:活动 */
    private Integer type;

    /** 状态 0:草稿 1:已发布 */
    private Integer status;

    /** 发布时间 */
    private LocalDateTime publishTime;

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
