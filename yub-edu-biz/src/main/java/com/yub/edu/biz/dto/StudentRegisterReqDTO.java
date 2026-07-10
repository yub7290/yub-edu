package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 学员注册请求
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 学员自助注册参数（含可选邀请人）
 * @Version: 1.0.0
 */
@Data
public class StudentRegisterReqDTO implements Serializable {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码（SM3加密后）
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码Key
     */
    @NotBlank(message = "验证码Key不能为空")
    private String captchaKey;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captchaCode;

    /**
     * 邀请人ID（通过邀请链接注册时携带，可选）
     */
    private Long inviterId;
}
