package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 教师用户信息响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 返回当前登录教师的基本信息、角色、菜单和权限
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUserInfoRespVO {

    /** 教师ID */
    private Long id;

    /** 账号 */
    private String account;

    /** 姓名 */
    private String name;

    /** 头像地址 */
    private String avatarUrl;

    /** 性别（1=男 2=女 0=未知） */
    private Integer gender;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 角色编码列表 */
    private List<String> roles;

    /** 菜单树 */
    private List<Object> menus;

    /** 权限标识列表 */
    private List<String> permissions;
}
