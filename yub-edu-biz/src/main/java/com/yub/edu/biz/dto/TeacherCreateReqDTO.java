package com.yub.edu.biz.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 新增教师请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 新增教师请求参数
 * @Version: 1.0.0
 */
@Data
public class TeacherCreateReqDTO {

    // ===== 基本信息 =====
    /** 头像URL */
    private String avatarUrl;

    /** 账号 */
    @NotBlank(message = "账号不能为空")
    @Size(min = 1, max = 50, message = "账号长度1-50个字符")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "账号只能包含字母、数字和下划线")
    private String account;

    /** 姓名 */
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    /** 职称ID */
    private Long titleId;

    /** 拼音缩写 */
    @Size(max = 50, message = "拼音缩写长度不能超过50个字符")
    private String pinyinAbbr;

    /** 性别（1=男 2=女 0=未知） */
    private Integer gender;

    /** 移动电话 */
    @NotBlank(message = "移动电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /** 身份证号 */
    @Size(min = 18, max = 18, message = "身份证号长度为18位")
    private String idCard;

    /** 状态（1=启用 0=禁用，默认启用） */
    private Integer status = 1;

    /** 评分（1-5分，默认5分） */
    @Min(value = 1, message = "评分最低为1分")
    @Max(value = 5, message = "评分最高为5分")
    private Integer rating = 5;

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
}
