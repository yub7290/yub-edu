package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 教师职称查询 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师职称查询参数
 * @Version: 1.0.0
 */
@Data
public class TeacherTitleQueryDTO {

    /** 职称名称 */
    private String name;

    /** 状态（1=启用 0=禁用） */
    private Integer status;
}
