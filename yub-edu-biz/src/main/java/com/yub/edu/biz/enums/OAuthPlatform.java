package com.yub.edu.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 第三方OAuth平台枚举
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 微信/QQ等第三方OAuth平台标识
 * @Version: 1.0.0
 */
@Getter
@AllArgsConstructor
public enum OAuthPlatform {

    /** 微信 */
    WECHAT("wechat", "微信"),

    /** QQ */
    QQ("qq", "QQ");

    /** 平台标识 */
    private final String code;

    /** 平台名称 */
    private final String name;

    /**
     * 根据code获取枚举
     *
     * @param code 平台标识
     * @return OAuthPlatform枚举
     */
    public static OAuthPlatform fromCode(String code) {
        for (OAuthPlatform platform : values()) {
            if (platform.code.equals(code)) {
                return platform;
            }
        }
        throw new IllegalArgumentException("不支持的OAuth平台: " + code);
    }
}
