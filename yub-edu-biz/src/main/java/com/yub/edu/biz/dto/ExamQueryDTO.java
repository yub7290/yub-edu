package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 试卷查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试卷查询参数
 * @Version: 1.0.0
 */
@Data
public class ExamQueryDTO {

    /** 试卷标题（模糊搜索） */
    private String title;

    /** 课程ID */
    private Long courseId;

    /** 状态（1=启用 0=禁用） */
    private Integer status;
}
