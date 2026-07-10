package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 学员查询 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 学员查询参数
 * @Version: 1.0.0
 */
@Data
public class StudentQueryDTO {

    /** 姓名 */
    private String name;

    /** 移动电话 */
    private String phone;

    /** 账号 */
    private String account;

    /** 状态（1=启用 0=禁用） */
    private Integer status;

    /** 教师ID（教师端查询时由后端自动注入，仅返回该教师归属的数据） */
    private Long teacherId;
}
