package com.yub.edu.biz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 积分设置请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分设置保存请求参数
 * @Version: 1.0
 */
@Data
public class PointsSettingReqDTO {

    /** 注册积分 */
    @JsonProperty("points_register")
    private String pointsRegister;

    /** 登录积分 */
    @JsonProperty("points_login")
    private String pointsLogin;

    /** 登录每日上限 */
    @JsonProperty("points_login_daily_max")
    private String pointsLoginDailyMax;

    /** 分享积分 */
    @JsonProperty("points_share")
    private String pointsShare;

    /** 分享每日上限 */
    @JsonProperty("points_share_daily_max")
    private String pointsShareDailyMax;

    /** 分享注册积分 */
    @JsonProperty("points_share_register")
    private String pointsShareRegister;

    /** 分享注册每日上限 */
    @JsonProperty("points_share_register_daily_max")
    private String pointsShareRegisterDailyMax;

    /** 积分兑换比例 */
    @JsonProperty("points_exchange_rate")
    private String pointsExchangeRate;
}
