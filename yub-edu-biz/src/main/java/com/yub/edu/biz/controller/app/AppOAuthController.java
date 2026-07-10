package com.yub.edu.biz.controller.app;

import com.yub.common.model.Response;
import com.yub.edu.biz.service.OAuthService;
import com.yub.framework.security.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
/**
 * �û�OAuth�󶨹���������
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: ΢��/QQ�󶨡����״̬��ѯ
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/app/oauth")
@RequiredArgsConstructor
public class AppOAuthController {

    private final OAuthService oAuthService;

    @Value("${oauth.frontendBaseUrl:http://localhost:5173}")
    private String frontendBaseUrl;

    /**
     * ��ȡOAuth����ȨURL
     *
     * @param platform ƽ̨��ʶ(wechat/qq)
     * @return ��ȨURL
     */
    @GetMapping("/bindUrl")
    public Response<Map<String, String>> getBindUrl(@RequestParam String platform) {
        Long userId = SecurityUtils.getCurrentUserId();
        String url = oAuthService.getBindAuthorizeUrl(userId, platform);
        return Response.success(Map.of("url", url));
    }
    /**
     * OAuth�ص������������̣�
     *
     * @param code  ��Ȩ��
     * @param state ǩ��״̬
     */
    @GetMapping("/callback")
    public void handleCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {
        try {
            String redirectUrl = oAuthService.handleBindCallback(code, state);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            String errorUrl = frontendBaseUrl + "/#/pages/mine/oauth-callback?status=error&platform=&message="
                    + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
            response.sendRedirect(errorUrl);
        }
    }
    /**
     * ���������˺�
     *
     * @param platform ƽ̨��ʶ
     * @return �������
     */
    @DeleteMapping("/unbind")
    public Response<Void> unbind(@RequestParam String platform) {
        Long userId = SecurityUtils.getCurrentUserId();
        oAuthService.unbind(userId, platform);
        return Response.success();
    }
    /**
     * ��ȡ��ǰ�û�����ƽ̨�İ�״̬
     *
     * @return ��״̬Map
     */
    @GetMapping("/status")
    public Response<Map<String, Object>> getStatus() {
        Long userId = SecurityUtils.getCurrentUserId();
        return Response.success(oAuthService.getBindStatus(userId));
    }
}