package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 新增课程请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 新增课程请求参数
 * @Version: 1.0.0
 */
@Data
public class CourseCreateReqDTO {

    /** 课程名称 */
    @NotBlank(message = "课程名称不能为空")
    @Size(max = 200, message = "课程名称长度不能超过200个字符")
    private String name;

    /** 课程图片URL */
    @Size(max = 200, message = "图片URL长度不能超过200个字符")
    private String imageUrl;

    /** 所属专业ID */
    private Long majorId;

    /** 课程类型 0:学练考 1:试题库 */
    private Integer courseType = 0;

    /** 状态 1:启用 0:禁用 */
    private Integer status = 1;

    /** 推荐 1:是 0:否 */
    private Integer recommended = 0;

    /** 排序 */
    private Integer sort = 0;

    /** 学习目标（富文本） */
    private String learningObjectives;

    /** 简介（富文本） */
    private String introduction;

    /** 需要学习月数 */
    private Integer monthsRequired;

    /** 总价格 */
    private BigDecimal totalPrice;

    /** 是否完全免费 1:是 0:否 */
    private Integer isFree = 0;

    /** 是否限时免费 1:是 0:否 */
    private Integer isFreeLimited = 0;

    /** 限时免费开始时间 */
    private LocalDateTime freeStartTime;

    /** 限时免费结束时间 */
    private LocalDateTime freeEndTime;

    /** 是否允许试学 1:是 0:否 */
    private Integer allowTrial = 0;

    /** 教师 */
    @Size(max = 100, message = "教师名称长度不能超过100个字符")
    private String teacher;

    /** 教师ID */
    private Long teacherId;
}
