package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教师分页响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师分页列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherPageRespVO {

    /** 教师ID */
    private Long id;

    /** 姓名 */
    private String name;

    /** 移动电话 */
    private String phone;

    /** 课程数 */
    private Integer courseCount;

    /** 身份证号 */
    private String idCard;

    /** 民族 */
    private String nationality;

    /** 学历 */
    private String education;

    /** 职称名称 */
    private String titleName;

    /** 年龄 */
    private Integer age;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;

    /** 账号 */
    private String account;

    /** 状态（1=启用 0=禁用） */
    private Integer status;

    /** 是否推荐（1是 0否） */
    private Integer recommended;

    /** 评分（1-5分） */
    private Integer rating;
}
