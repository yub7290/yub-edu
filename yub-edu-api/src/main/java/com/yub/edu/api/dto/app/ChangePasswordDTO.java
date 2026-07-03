package com.yub.edu.api.dto.app;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: APP-个人中心-修改密码
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

    /** 原密码（已 SM3 哈希） */
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    /** 新密码（已 SM3 哈希） */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 64, max = 64, message = "密码格式错误")
    private String newPassword;

}
