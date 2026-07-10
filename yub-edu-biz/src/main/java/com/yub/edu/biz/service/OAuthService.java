package com.yub.edu.biz.service;

import java.util.Map;

/**
 * OAuth第三方账号绑定服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 微信/QQ OAuth授权绑定、解绑、状态查询
 * @Version: 1.0.0
 */
public interface OAuthService {

    /**
     * 获取OAuth授权URL（绑定流程）
     *
     * @param userId   当前用户ID
     * @param platform 平台标识(wechat/qq)
     * @return OAuth授权URL
     */
    String getBindAuthorizeUrl(Long userId, String platform);

    /**
     * 处理OAuth回调（绑定流程）
     *
     * @param code  授权码
     * @param state 签名状态
     * @return 跳转前端的完整URL
     */
    String handleBindCallback(String code, String state);

    /**
     * 解绑第三方账号
     *
     * @param userId   当前用户ID
     * @param platform 平台标识
     */
    void unbind(Long userId, String platform);

    /**
     * 获取用户所有平台的绑定状态
     *
     * @param userId 当前用户ID
     * @return 绑定状态Map，key为platform code
     */
    Map<String, Object> getBindStatus(Long userId);

    /**
     * 获取OAuth授权URL（登录流程）
     *
     * @param platform 平台标识
     * @return OAuth授权URL
     */
    String getLoginAuthorizeUrl(String platform);

    /**
     * 处理OAuth回调（登录流程）
     *
     * @param code  授权码
     * @param state 签名状态
     * @return 跳转前端的完整URL（含token参数）
     */
    String handleLoginCallback(String code, String state);
}
