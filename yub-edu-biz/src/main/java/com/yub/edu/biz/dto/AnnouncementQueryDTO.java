package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 公告查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 公告查询参数
 * @Version: 1.0.0
 */
@Data
public class AnnouncementQueryDTO {

    /** 课程ID */
    private Long courseId;

    /** 公告标题（模糊搜索） */
    private String title;

    /** 状态（1=启用 0=禁用） */
    private Integer status;

    /** 教师ID（教师端查询时由后端自动注入，仅返回该教师归属的数据） */
    private Long teacherId;
}
