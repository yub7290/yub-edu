package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 学员组查询 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组查询参数
 * @Version: 1.0.0
 */
@Data
public class StudentGroupQueryDTO {

    /** 学员组名称（模糊搜索） */
    private String name;

    /** 状态（1=启用 0=禁用） */
    private Integer status;
}
