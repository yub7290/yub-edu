package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程分页响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 课程分页列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePageRespVO {

    /** 课程ID */
    private Long id;

    /** 课程名称 */
    private String name;

    /** 课程图片URL */
    private String imageUrl;

    /** 总价格 */
    private BigDecimal totalPrice;

    /** 是否免费 */
    private Integer isFree;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 推荐 1:是 0:否 */
    private Integer recommended;

    /** 课程类型 */
    private Integer courseType;

    /** 专业名称 */
    private String majorName;

    /** 教师 */
    private String teacher;

    /** 章节数 */
    private Integer chapterCount;

    /** 试题数 */
    private Integer questionCount;

    /** 试卷数 */
    private Integer examCount;

    /** 视频数 */
    private Integer videoCount;

    /** 学员数 */
    private Integer studentCount;

    /** 浏览数 */
    private Integer viewCount;

    /** 创建时间 */
    private LocalDateTime createTime;
}
