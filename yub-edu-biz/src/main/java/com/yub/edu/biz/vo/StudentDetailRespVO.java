package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学员详情响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 学员详情响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailRespVO {

    /** 学员ID */
    private Long id;

    // ===== 基本信息 =====
    /** 头像URL */
    private String avatarUrl;
    /** 账号 */
    private String account;
    /** 姓名 */
    private String name;
    /** 拼音缩写 */
    private String pinyinAbbr;
    /** 学员编号 */
    private String studentNo;
    /** 学员组ID */
    private Long groupId;
    /** 学员组名称 */
    private String groupName;
    /** 性别（1=男 2=女 0=未知） */
    private Integer gender;
    /** 身份证号 */
    private String idCard;
    /** 状态（1=启用 0=禁用） */
    private Integer status;

    // ===== 联系方式 =====
    /** 移动电话 */
    private String phone;
    /** 移动电话是否公开 */
    private Integer phonePublic;
    /** 固定电话 */
    private String fixedPhone;
    /** 固定电话是否公开 */
    private Integer fixedPhonePublic;
    /** 电子邮箱 */
    private String email;
    /** QQ */
    private String qq;
    /** 微信 */
    private String wechat;
    /** 住址 */
    private String address;
    /** 通讯地址 */
    private String mailingAddress;
    /** 邮编 */
    private String zipCode;
    /** 紧急联系人 */
    private String emergencyContact;
    /** 紧急电话 */
    private String emergencyPhone;

    // ===== 详细信息 =====
    /** 出生年月 */
    private LocalDate birthDate;
    /** 年龄（从birthDate动态计算） */
    private Integer age;
    /** 学历 */
    private String education;
    /** 专业 */
    private String major;
    /** 籍贯 */
    private String nativePlace;
    /** 学校 */
    private String school;
    /** 民族 */
    private String nationality;
    /** 签名 */
    private String signature;
    /** 个人介绍（富文本） */
    private String bio;

    // ===== 系统字段 =====
    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
