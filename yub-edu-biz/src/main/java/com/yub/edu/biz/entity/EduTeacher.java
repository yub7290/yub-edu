package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教师信息实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师信息实体
 * @Version: 1.0.0
 */
@Data
public class EduTeacher {
    /**
     * 主键
     */
    private Long id;
    /**
     * 头像URL
     */
    private String avatarUrl;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码（BCrypt加密）
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    /**
     * 职称ID
     */
    private Long titleId;
    /**
     * 拼音缩写
     */
    private String pinyinAbbr;
    /**
     * 性别（1=男 2=女 0=未知）
     */
    private Integer gender;
    /**
     * 移动电话
     */
    private String phone;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 状态（1=正常 0=禁用）
     */
    private Integer status;
    /**
     * 固定电话
     */
    private String fixedPhone;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * QQ
     */
    private String qq;
    /**
     * 微信
     */
    private String wechat;
    /**
     * 住址
     */
    private String address;
    /**
     * 通讯地址
     */
    private String mailingAddress;
    /**
     * 邮编
     */
    private String zipCode;
    /**
     * 紧急联系人
     */
    private String emergencyContact;
    /**
     * 紧急电话
     */
    private String emergencyPhone;
    /**
     * 出生年月
     */
    private LocalDate birthDate;
    /**
     * 学历
     */
    private String education;
    /**
     * 专业
     */
    private String major;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 工作单位
     */
    private String workUnit;
    /**
     * 民族
     */
    private String nationality;
    /**
     * 签名
     */
    private String signature;
    /**
     * 个人介绍（富文本）
     */
    private String bio;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 逻辑删除（0=正常 1=已删除）
     */
    private Integer deleted;
}
