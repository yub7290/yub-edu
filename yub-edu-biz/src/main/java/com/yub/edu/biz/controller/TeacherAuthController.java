package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.dto.TeacherLoginReqDTO;
import com.yub.edu.biz.service.TeacherAuthService;
import com.yub.edu.biz.vo.LoginRespVO;
import com.yub.edu.biz.vo.TeacherUserInfoRespVO;
import com.yub.framework.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 教师认证控制器
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 教师登录、Token 刷新、获取用户信息
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/teacher/auth")
@RequiredArgsConstructor
public class TeacherAuthController {

    private final TeacherAuthService teacherAuthService;
    private final JwtProvider jwtProvider;

    /**
     * 教师登录
     *
     * @param reqDTO      登录请求参数
     * @param httpRequest HTTP 请求（用于获取客户端 IP）
     * @return Token 信息
     */
    @PostMapping("/login")
    public Response<LoginRespVO> login(@Valid @RequestBody TeacherLoginReqDTO reqDTO,
                                       HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        return Response.success(teacherAuthService.login(reqDTO, ip));
    }

    /**
     * 获取当前教师信息
     *
     * @param request HTTP 请求（用于解析 JWT Token）
     * @return 教师信息
     */
    @GetMapping("/getUserInfo")
    public Response<TeacherUserInfoRespVO> getUserInfo(HttpServletRequest request) {
        String token = jwtProvider.getToken(request);
        Long teacherId = Long.valueOf(jwtProvider.getUserId(token));
        return Response.success(teacherAuthService.getCurrentUserInfo(teacherId));
    }

    /**
     * 教师登出：清除 RefreshToken 并将 AccessToken 加入黑名单
     *
     * @param request HTTP 请求（用于提取 AccessToken）
     * @return 登出响应
     */
    @PostMapping("/logout")
    public Response<Void> logout(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        Long teacherId = Long.valueOf(jwtProvider.getUserId(accessToken));
        teacherAuthService.logout(teacherId, accessToken);
        return Response.success();
    }

    /**
     * 刷新 Token
     *
     * @param refreshToken 刷新令牌
     * @return 新的 Token 信息
     */
    @PostMapping("/refresh/{refreshToken}")
    public Response<LoginRespVO> refresh(@PathVariable("refreshToken") String refreshToken) {
        return Response.success(teacherAuthService.refresh(refreshToken));
    }

    /**
     * 获取客户端真实 IP
     *
     * @param request HTTP 请求
     * @return 客户端 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
