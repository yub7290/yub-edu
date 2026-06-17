package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 课程查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 课程查询参数
 * @Version: 1.0.0
 */
@Data
public class CourseQueryDTO {

    /** 课程名称（模糊搜索） */
    private String name;

    /** 专业ID */
    private Long majorId;

    /** 状态（1=启用 0=禁用） */
    private Integer status;
}
