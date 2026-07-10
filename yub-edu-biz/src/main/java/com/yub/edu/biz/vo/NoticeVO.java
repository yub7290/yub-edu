package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通知响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 通知响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVO {

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

    /** 关联课程名称 */
    private String courseName;

    /** 是否已读 1:已读 0:未读（仅学员端列表返回） */
    private Integer readFlag;

    /** 创建时间（格式化字符串） */
    private String createTimeStr;
}
