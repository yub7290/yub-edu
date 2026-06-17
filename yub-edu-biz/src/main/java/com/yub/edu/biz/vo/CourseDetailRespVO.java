package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程详情响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 课程详情响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailRespVO {

    /** 课程ID */
    private Long id;

    /** 课程名称 */
    private String name;

    /** 课程图片URL */
    private String imageUrl;

    /** 所属专业ID */
    private Long majorId;

    /** 所属专业名称 */
    private String majorName;

    /** 课程类型 0:学练考 1:试题库 */
    private Integer courseType;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 推荐 1:是 0:否 */
    private Integer recommended;

    /** 排序 */
    private Integer sort;

    /** 学习目标（富文本） */
    private String learningObjectives;

    /** 简介（富文本） */
    private String introduction;

    /** 需要学习月数 */
    private Integer monthsRequired;

    /** 总价格 */
    private BigDecimal totalPrice;

    /** 是否完全免费 */
    private Integer isFree;

    /** 是否限时免费 */
    private Integer isFreeLimited;

    /** 限时免费开始时间 */
    private LocalDateTime freeStartTime;

    /** 限时免费结束时间 */
    private LocalDateTime freeEndTime;

    /** 是否允许试学 */
    private Integer allowTrial;

    /** 教师 */
    private String teacher;

    /** 学员数 */
    private Integer studentCount;

    /** 浏览数 */
    private Integer viewCount;

    /** 章节数 */
    private Integer chapterCount;

    /** 试题数 */
    private Integer questionCount;

    /** 试卷数 */
    private Integer examCount;

    /** 视频数 */
    private Integer videoCount;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
