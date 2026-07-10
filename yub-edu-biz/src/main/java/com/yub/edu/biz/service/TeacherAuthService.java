package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.TeacherLoginReqDTO;
import com.yub.edu.biz.vo.LoginRespVO;
import com.yub.edu.biz.vo.TeacherUserInfoRespVO;

/**
 * 教师权限服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 教师登录、Token 刷新、用户信息查询
 * @Version: 1.0.0
 */
public interface TeacherAuthService {

    /**
     * 教师登录
     *
     * @param reqDTO 登录请求参数
     * @param ip      客户端IP
     * @return Token 信息
     */
    LoginRespVO login(TeacherLoginReqDTO reqDTO, String ip);

    /**
     * 刷新 Token
     *
     * @param refreshToken 刷新令牌
     * @return 新的 Token 信息
     */
    LoginRespVO refresh(String refreshToken);

    /**
     * 获取当前教师信息
     *
     * @param teacherId 教师ID
     * @return 教师信息
     */
    TeacherUserInfoRespVO getCurrentUserInfo(Long teacherId);

    /**
     * 教师登出：清除 RefreshToken 并将 AccessToken 加入黑名单
     *
     * @param teacherId   教师ID
     * @param accessToken 当前 AccessToken
     */
    void logout(Long teacherId, String accessToken);
}
