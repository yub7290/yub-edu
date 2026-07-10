package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.StudentLoginReqDTO;
import com.yub.edu.biz.dto.StudentRegisterReqDTO;
import com.yub.edu.biz.vo.CaptchaRespVO;
import com.yub.edu.biz.vo.StudentInfoRespVO;
import com.yub.edu.biz.vo.StudentLoginRespVO;

/**
 * 学员认证服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学员认证业务接口
 * @Version: 1.0.0
 */
public interface StudentAuthService {

    /**
     * 获取验证码
     *
     * @return 验证码
     */
    CaptchaRespVO generateCaptcha();

    /**
     * 学员登录
     *
     * @param reqDTO 登录请求
     * @param ip     客户端IP
     * @return 登录响应（AccessToken + RefreshToken）
     */
    StudentLoginRespVO login(StudentLoginReqDTO reqDTO, String ip);

    /**
     * 学员自助注册（含可选邀请人建立双向好友、发放邀请积分）
     *
     * @param reqDTO 注册请求
     */
    void register(StudentRegisterReqDTO reqDTO);

    /**
     * 刷新Token
     *
     * @param refreshToken 刷新Token
     * @return 新的登录响应
     */
    StudentLoginRespVO refresh(String refreshToken);

    /**
     * 获取学员信息
     *
     * @param studentId 学员ID
     * @return 学员信息
     */
    StudentInfoRespVO getStudentInfo(Long studentId);

    /**
     * 获取OAuth登录授权URL
     *
     * @param platform 平台标识(wechat/qq)
     * @return OAuth授权URL
     */
    String getOAuthLoginUrl(String platform);

    /**
     * 处理OAuth登录回调（含自动注册）
     *
     * @param code  授权码
     * @param state 签名状态
     * @return 重定向到前端的URL（含token参数）
     */
    String handleOAuthLoginCallback(String code, String state);
}
