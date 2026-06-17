package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 专业查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 专业查询参数
 * @Version: 1.0.0
 */
@Data
public class MajorQueryDTO {

    /** 专业名称（模糊搜索） */
    private String name;

    /** 状态（1=启用 0=禁用） */
    private Integer status;
}
