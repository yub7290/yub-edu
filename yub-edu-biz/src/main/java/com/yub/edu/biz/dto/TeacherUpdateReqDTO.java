package com.yub.edu.biz.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 编辑教师请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 编辑教师请求参数
 * @Version: 1.0.0
 */
@Data
public class TeacherUpdateReqDTO {

    /** 教师ID */
    @NotNull(message = "教师ID不能为空")
    private Long id;

    // ===== 基本信息 =====
    /** 头像URL */
    private String avatarUrl;

    /** 姓名 */
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
    private String phone;

    /** 身份证号 */
    @Size(min = 18, max = 18, message = "身份证号长度为18位")
    private String idCard;

    /** 状态（1=启用 0=禁用） */
    private Integer status;

    /** 评分（1-5分） */
    @Min(value = 1, message = "评分最低为1分")
    @Max(value = 5, message = "评分最高为5分")
    private Integer rating;

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
