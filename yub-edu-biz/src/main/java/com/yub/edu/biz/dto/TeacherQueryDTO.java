package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 教师查询 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师查询参数
 * @Version: 1.0.0
 */
@Data
public class TeacherQueryDTO {

    /** 账号 */
    private String account;

    /** 姓名 */
    private String name;

    /** 移动电话 */
    private String phone;

    /** 职称ID */
    private Long titleId;

    /** 学历 */
    private String education;

    /** 状态（1=启用 0=禁用） */
    private Integer status;
}
