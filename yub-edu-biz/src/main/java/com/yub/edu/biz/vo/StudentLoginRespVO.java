package com.yub.edu.biz.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 学员登录响应
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学员登录响应数据
 * @Version: 1.0.0
 */
@Data
@Builder
public class StudentLoginRespVO {

    /**
     * AccessToken
     */
    private String accessToken;

    /**
     * RefreshToken
     */
    private String refreshToken;
}
