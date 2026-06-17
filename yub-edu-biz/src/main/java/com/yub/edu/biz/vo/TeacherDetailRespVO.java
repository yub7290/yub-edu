package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教师详情响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师详情响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDetailRespVO {

    /** 教师ID */
    private Long id;

    // ===== 基本信息 =====
    /** 头像URL */
    private String avatarUrl;
    /** 账号 */
    private String account;
    /** 姓名 */
    private String name;
    /** 职称ID */
    private Long titleId;
    /** 职称名称 */
    private String titleName;
    /** 拼音缩写 */
    private String pinyinAbbr;
    /** 性别（1=男 2=女 0=未知） */
    private Integer gender;
    /** 移动电话 */
    private String phone;
    /** 身份证号 */
    private String idCard;
    /** 状态（1=启用 0=禁用） */
    private Integer status;

    // ===== 联系方式 =====
    /** 固定电话 */
    private String fixedPhone;
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
    /** 学历 */
    private String education;
    /** 专业 */
    private String major;
    /** 籍贯 */
    private String nativePlace;
    /** 工作单位 */
    private String workUnit;
    /** 民族 */
    private String nationality;
    /** 签名 */
    private String signature;
    /** 个人介绍（富文本） */
    private String bio;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
