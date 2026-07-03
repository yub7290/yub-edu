package com.yub.edu.biz.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 学员信息响应
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学员个人信息响应
 * @Version: 1.0.0
 */
@Data
@Builder
public class StudentInfoRespVO {

    /**
     * 学员ID
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 学员编号
     */
    private String studentNo;

    /**
     * 性别（1=男 2=女 0=未知）
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}
