package com.yub.edu.biz.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户第三方账号绑定实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 微信/QQ等第三方账号绑定关系
 * @Version: 1.0.0
 */
@Data
public class EduUserOAuth {

    /** 主键 */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 平台标识: wechat/qq */
    private String platform;

    /** 平台用户唯一标识 */
    private String openId;

    /** 跨应用唯一标识 */
    private String unionId;

    /** 社交账号昵称 */
    private String nickname;

    /** 社交账号头像URL */
    private String avatarUrl;

    /** 绑定时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 */
    private Integer deleted;
}
