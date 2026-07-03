package com.yub.edu.api.vo.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: APP-个人中心-登录日志
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginLogRespVO {

    /** 登录时间 */
    private LocalDateTime createTime;

    /** 客户端IP */
    private String ip;

    /** 设备信息 */
    private String userAgent;

    /** 登录状态（1=成功 0=失败） */
    private Integer status;

    /** 失败原因 */
    private String errorMsg;

}
