package com.yub.edu.biz.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 验证码响应
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 验证码响应数据
 * @Version: 1.0.0
 */
@Data
@Builder
public class CaptchaRespVO {

    /**
     * 验证码Key
     */
    private String key;

    /**
     * 验证码图片（Base64）
     */
    private String image;
}
