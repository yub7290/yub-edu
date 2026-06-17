package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 编辑学员请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 编辑学员请求参数
 * @Version: 1.0.0
 */
@Data
public class StudentUpdateReqDTO {

    /** 学员ID */
    @NotNull(message = "学员ID不能为空")
    private Long id;

    // ===== 基本信息 =====
    /** 头像URL */
    private String avatarUrl;

    /** 姓名 */
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    /** 拼音缩写 */
    private String pinyinAbbr;

    /** 学员编号 */
    private String studentNo;

    /** 学员组ID */
    private Long groupId;

    /** 性别（1=男 2=女 0=未知） */
    private Integer gender;

    /** 身份证号 */
    @Size(min = 18, max = 18, message = "身份证号长度为18位")
    private String idCard;

    // ===== 联系方式 =====
    /** 移动电话 */
    private String phone;

    /** 移动电话是否公开（1=是 0=否） */
    private Integer phonePublic;

    /** 固定电话 */
    private String fixedPhone;

    /** 固定电话是否公开（1=是 0=否） */
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

    /** 状态（1=启用 0=禁用） */
    private Integer status;
}
