package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学员分页响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 学员分页列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentPageRespVO {

    /** 学员ID */
    private Long id;

    /** 头像URL */
    private String avatarUrl;

    /** 姓名 */
    private String name;

    /** 移动电话 */
    private String phone;

    /** 年龄（从birthDate动态计算） */
    private Integer age;

    /** 账号 */
    private String account;

    /** 学员组名称 */
    private String groupName;

    /** 身份证号 */
    private String idCard;

    /** 状态（1=启用 0=禁用） */
    private Integer status;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;
}
