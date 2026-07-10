package com.yub.edu.biz.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 登录响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 登录成功后返回的 Token 信息
 * @Version: 1.0.0
 */
@Data
@Builder
public class LoginRespVO {

    /** 访问令牌 */
    private String accessToken;

    /** 刷新令牌 */
    private String refreshToken;
}
