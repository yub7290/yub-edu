package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.dto.StudentLoginReqDTO;
import com.yub.edu.biz.dto.StudentRegisterReqDTO;
import com.yub.edu.biz.service.OAuthService;
import com.yub.edu.biz.service.StudentAuthService;
import com.yub.edu.biz.vo.CaptchaRespVO;
import com.yub.edu.biz.vo.StudentInfoRespVO;
import com.yub.edu.biz.vo.StudentLoginRespVO;
import com.yub.framework.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 学员认证 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学员认证接口（登录、验证码、刷新Token、获取信息）
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/student/auth")
@RequiredArgsConstructor
public class StudentAuthController {

    private final StudentAuthService studentAuthService;
    private final JwtProvider jwtProvider;
    private final OAuthService oAuthService;

    @Value("${oauth.frontendBaseUrl:http://localhost:5173}")
    private String frontendBaseUrl;

    /**
     * 获取验证码
     *
     * @return 验证码
     */
    @GetMapping("/captcha")
    public Response<CaptchaRespVO> captcha() {
        return Response.success(studentAuthService.generateCaptcha());
    }

    /**
     * 学员登录
     *
     * @param reqDTO      登录请求
     * @param httpRequest HTTP请求
     * @return 登录响应
     */
    @PostMapping("/login")
    public Response<StudentLoginRespVO> login(@Valid @RequestBody StudentLoginReqDTO reqDTO,
                                               HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        return Response.success(studentAuthService.login(reqDTO, ip));
    }

    /**
     * 学员自助注册（含可选邀请人建立双向好友、发放邀请积分）
     *
     * @param reqDTO 注册请求
     * @return 响应
     */
    @PostMapping("/register")
    public Response<Void> register(@Valid @RequestBody StudentRegisterReqDTO reqDTO) {
        studentAuthService.register(reqDTO);
        return Response.success();
    }

    /**
     * 刷新Token
     *
     * @param refreshToken 刷新Token
     * @return 新的Token
     */
    @PostMapping("/refresh/{refreshToken}")
    public Response<StudentLoginRespVO> refresh(@PathVariable("refreshToken") String refreshToken) {
        return Response.success(studentAuthService.refresh(refreshToken));
    }

    /**
     * 获取当前学员信息
     *
     * @param request HTTP请求
     * @return 学员信息
     */
    @GetMapping("/getUserInfo")
    public Response<StudentInfoRespVO> getUserInfo(HttpServletRequest request) {
        String token = jwtProvider.getToken(request);
        Long studentId = Long.valueOf(jwtProvider.getUserId(token));
        return Response.success(studentAuthService.getStudentInfo(studentId));
    }

    /**
     * 获取OAuth登录授权URL
     */
    @GetMapping("/oauth/loginUrl")
    public Response<Map<String, String>> getOAuthLoginUrl(@RequestParam String platform) {
        String url = studentAuthService.getOAuthLoginUrl(platform);
        return Response.success(Map.of("url", url));
    }

    /**
     * OAuth登录回调处理
     *
     * @param code  授权码
     * @param state 签名状态
     */
    @GetMapping("/oauth/callback")
    public void handleOAuthCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws Exception {
        try {
            String redirectUrl = studentAuthService.handleOAuthLoginCallback(code, state);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            String errorUrl = frontendBaseUrl + "/#/pages/mine/oauth-callback?status=error&platform=&message="
                    + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
            response.sendRedirect(errorUrl);
        }
    }

    /**
     * 获取客户端IP
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
        // 多个代理时取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
