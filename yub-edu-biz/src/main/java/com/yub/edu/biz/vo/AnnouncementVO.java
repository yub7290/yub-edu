package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 公告响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 公告响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementVO {

    /** 主键 */
    private Long id;

    /** 所属课程ID */
    private Long courseId;

    /** 公告标题 */
    private String title;

    /** 长标题（展示详情时优先显示） */
    private String longTitle;

    /** 分类 */
    private String category;

    /** 简述 */
    private String summary;

    /** 来源 */
    private String source;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 公告内容 */
    private String content;

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

    /** 创建时间（格式化字符串） */
    private String createTimeStr;
}
