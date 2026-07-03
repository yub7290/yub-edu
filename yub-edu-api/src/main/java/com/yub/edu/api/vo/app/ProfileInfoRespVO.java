package com.yub.edu.api.vo.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: APP-个人中心-个人信息
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileInfoRespVO {

    /**
     * 学员ID
     */
    private Long id;

    /**
     * 昵称（展示用）
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 移动电话
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别（1=男 2=女 0=未知）
     */
    private Integer gender;

    /**
     * 出生年月
     */
    private LocalDate birthday;

    /**
     * 学校
     */
    private String schoolName;
}
