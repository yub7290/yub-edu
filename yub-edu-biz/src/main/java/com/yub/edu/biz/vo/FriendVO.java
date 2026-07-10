package com.yub.edu.biz.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 好友信息响应
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 我的好友列表项（联表学员表展示字段）
 * @Version: 1.0.0
 */
@Data
@Builder
public class FriendVO {

    /** 关系记录ID */
    private Long id;

    /** 好友学员ID */
    private Long friendId;

    /** 姓名 */
    private String name;

    /** 昵称 */
    private String nickName;

    /** 头像URL */
    private String avatarUrl;

    /** 学员编号 */
    private String studentNo;

    /** 学校 */
    private String school;

    /** 好友最后登录时间 */
    private LocalDateTime lastLoginTime;

    /** 成为好友时间 */
    private LocalDateTime createTime;
}
