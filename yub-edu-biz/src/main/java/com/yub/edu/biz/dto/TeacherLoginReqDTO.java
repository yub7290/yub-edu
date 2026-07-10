package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 教师登录请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 教师登录接口的请求参数
 * @Version: 1.0.0
 */
@Data
public class TeacherLoginReqDTO {

    /** 账号 */
    @NotBlank(message = "账号不能为空")
    private String account;

    /** 密码（SM3 哈希） */
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 验证码 Key */
    @NotBlank(message = "验证码Key不能为空")
    private String captchaKey;

    /** 验证码 */
    @NotBlank(message = "验证码不能为空")
    private String captchaCode;
}
