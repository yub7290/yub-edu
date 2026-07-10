package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 通知查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 通知查询参数
 * @Version: 1.0.0
 */
@Data
public class NoticeQueryDTO {

    /** 课程ID */
    private Long courseId;

    /** 标题（模糊搜索） */
    private String title;

    /** 状态 0:草稿 1:已发布 */
    private Integer status;

    /** 类型 */
    private Integer type;

    /** 教师ID（教师端查询时由后端自动注入，仅返回该教师归属的数据） */
    private Long teacherId;
}
