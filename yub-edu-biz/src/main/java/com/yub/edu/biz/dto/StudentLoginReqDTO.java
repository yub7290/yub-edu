package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 学员登录请求
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学员登录请求参数
 * @Version: 1.0.0
 */
@Data
public class StudentLoginReqDTO {

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
}
