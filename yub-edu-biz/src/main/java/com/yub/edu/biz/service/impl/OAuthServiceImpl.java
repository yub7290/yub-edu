package com.yub.edu.biz.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yub.common.util.IdUtils;
import com.yub.edu.biz.entity.EduStudent;
import com.yub.edu.biz.entity.EduUserOAuth;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduStudentMapper;
import com.yub.edu.biz.mapper.EduUserOAuthMapper;
import com.yub.edu.biz.service.OAuthService;
import com.yub.framework.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OAuth第三方账号绑定服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 微信/QQ OAuth授权绑定、解绑、状态查询实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    private final EduUserOAuthMapper eduUserOAuthMapper;
    private final EduStudentMapper eduStudentMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${oauth.wechat.appId:}")
    private String wechatAppId;
    @Value("${oauth.wechat.appSecret:}")
    private String wechatAppSecret;
    @Value("${oauth.wechat.redirectUri:}")
    private String wechatRedirectUri;
    @Value("${oauth.qq.appId:}")
    private String qqAppId;
    @Value("${oauth.qq.appSecret:}")
    private String qqAppSecret;
    @Value("${oauth.qq.redirectUri:}")
    private String qqRedirectUri;
    @Value("${oauth.frontendBaseUrl:}")
    private String frontendBaseUrl;
    @Value("${jwt.secret}")
    private String stateSecret;

    private static final long STATE_EXPIRY_MS = 10 * 60 * 1000L;
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String TYPE_BIND = "bind";
    private static final String TYPE_LOGIN = "login";
    private static final String PLATFORM_WECHAT = "wechat";
    private static final String PLATFORM_QQ = "qq";
    private static final String FRONTEND_CALLBACK = "/#/pages/mine/oauth-callback";

    @Override
    public String getBindAuthorizeUrl(Long userId, String platform) {
        String state = buildState(userId, TYPE_BIND, platform);
        return buildAuthorizeUrl(platform, state);
    }

    @Override
    public String getLoginAuthorizeUrl(String platform) {
        String state = buildState(0L, TYPE_LOGIN, platform);
        return buildAuthorizeUrl(platform, state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleBindCallback(String code, String state) {
        // 1. 校验state签名与时效
        StatePayload sp = verifyState(state, TYPE_BIND);
        Long currentUserId = sp.userId;
        String platform = sp.platform;

        // 2. 换取accessToken和openId
        OAuthToken token = exchangeCode(platform, code);

        // 3. 获取第三方用户信息
        OAuthUserInfo userInfo = fetchUserInfo(platform, token);

        // 4. 检查该openId是否已被其他用户绑定
        EduUserOAuth bound = eduUserOAuthMapper.selectByPlatformAndOpenId(platform, userInfo.openId);
        if (bound != null && !bound.getUserId().equals(currentUserId)) {
            throw new EduException(EduErrorCode.OAUTH_ALREADY_BOUND_OTHER);
        }

        // 5. 检查当前用户是否已绑定该平台（重复绑定）
        EduUserOAuth myBinding = eduUserOAuthMapper.selectByUserIdAndPlatform(currentUserId, platform);
        if (myBinding != null) {
            throw new EduException(EduErrorCode.OAUTH_PLATFORM_ALREADY_BOUND);
        }

        // 6. 插入绑定记录
        EduUserOAuth oauth = new EduUserOAuth();
        oauth.setUserId(currentUserId);
        oauth.setPlatform(platform);
        oauth.setOpenId(userInfo.openId);
        oauth.setUnionId(userInfo.unionId);
        oauth.setNickname(userInfo.nickname);
        oauth.setAvatarUrl(userInfo.avatarUrl);
        oauth.setCreateTime(LocalDateTime.now());
        eduUserOAuthMapper.insert(oauth);

        log.info("OAuth绑定成功: userId={}, platform={}, openId={}", currentUserId, platform, userInfo.openId);
        return frontendBaseUrl + FRONTEND_CALLBACK + "?status=success&platform=" + platform;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleLoginCallback(String code, String state) {
        // 1. 校验state
        StatePayload sp = verifyState(state, TYPE_LOGIN);
        String platform = sp.platform;

        // 2. 换取accessToken和openId
        OAuthToken token = exchangeCode(platform, code);

        // 3. 获取第三方用户信息
        OAuthUserInfo userInfo = fetchUserInfo(platform, token);

        // 4. 查找已有绑定，决定登录或注册
        EduUserOAuth binding = eduUserOAuthMapper.selectByPlatformAndOpenId(platform, userInfo.openId);
        EduStudent student;
        if (binding != null) {
            // 已绑定 → 直接登录，更新昵称/头像
            student = eduStudentMapper.selectById(binding.getUserId());
            if (student == null) {
                throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
            }
            EduUserOAuth update = new EduUserOAuth();
            update.setId(binding.getId());
            update.setNickname(userInfo.nickname);
            update.setAvatarUrl(userInfo.avatarUrl);
            update.setUpdateTime(LocalDateTime.now());
            eduUserOAuthMapper.updateById(update);
        } else {
            // 未绑定 → 自动注册
            student = new EduStudent();
            student.setAccount("oauth_" + platform + "_" + userInfo.openId);
            student.setPassword(passwordEncoder.encode(IdUtils.simpleUuid()));
            student.setName(userInfo.nickname != null ? userInfo.nickname : platform + "_user");
            student.setNickName(userInfo.nickname);
            student.setAvatarUrl(userInfo.avatarUrl);
            student.setStatus(1);
            eduStudentMapper.insert(student);

            EduUserOAuth oauth = new EduUserOAuth();
            oauth.setUserId(student.getId());
            oauth.setPlatform(platform);
            oauth.setOpenId(userInfo.openId);
            oauth.setUnionId(userInfo.unionId);
            oauth.setNickname(userInfo.nickname);
            oauth.setAvatarUrl(userInfo.avatarUrl);
            oauth.setCreateTime(LocalDateTime.now());
            eduUserOAuthMapper.insert(oauth);
        }

        // 5. 生成JWT令牌
        String accessToken = jwtProvider.generateAccessToken(student.getId(), student.getAccount(), new HashMap<>());
        String refreshToken = jwtProvider.generateRefreshToken(student.getId());

        log.info("OAuth登录成功: studentId={}, platform={}, openId={}", student.getId(), platform, userInfo.openId);
        return frontendBaseUrl + FRONTEND_CALLBACK
                + "?status=loginSuccess&platform=" + platform
                + "&accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbind(Long userId, String platform) {
        EduUserOAuth binding = eduUserOAuthMapper.selectByUserIdAndPlatform(userId, platform);
        if (binding == null) {
            throw new EduException(EduErrorCode.OAUTH_NOT_BOUND);
        }
        eduUserOAuthMapper.deleteByUserIdAndPlatform(userId, platform);
        log.info("OAuth解绑成功: userId={}, platform={}", userId, platform);
    }

    @Override
    public Map<String, Object> getBindStatus(Long userId) {
        Map<String, Object> result = new HashMap<>();

        EduUserOAuth wechat = eduUserOAuthMapper.selectByUserIdAndPlatform(userId, PLATFORM_WECHAT);
        result.put(PLATFORM_WECHAT, buildStatusItem(wechat));

        EduUserOAuth qq = eduUserOAuthMapper.selectByUserIdAndPlatform(userId, PLATFORM_QQ);
        result.put(PLATFORM_QQ, buildStatusItem(qq));

        return result;
    }

    private Map<String, Object> buildStatusItem(EduUserOAuth oauth) {
        Map<String, Object> item = new HashMap<>();
        if (oauth == null) {
            item.put("bound", false);
        } else {
            item.put("bound", true);
            item.put("nickname", oauth.getNickname());
            item.put("avatarUrl", oauth.getAvatarUrl());
            item.put("bindTime", oauth.getCreateTime() != null ? oauth.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : null);
        }
        return item;
    }

    // ==================== 构建授权URL ====================

    private String buildAuthorizeUrl(String platform, String state) {
        if (PLATFORM_WECHAT.equals(platform)) {
            if (isEmpty(wechatAppId) || isEmpty(wechatRedirectUri)) {
                throw configMissing(PLATFORM_WECHAT);
            }
            return "https://open.weixin.qq.com/connect/qrconnect?appid=" + wechatAppId
                    + "&redirect_uri=" + urlEncode(wechatRedirectUri)
                    + "&response_type=code&scope=snsapi_login&state=" + state + "#wechat_redirect";
        } else if (PLATFORM_QQ.equals(platform)) {
            if (isEmpty(qqAppId) || isEmpty(qqRedirectUri)) {
                throw configMissing(PLATFORM_QQ);
            }
            return "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + qqAppId
                    + "&redirect_uri=" + urlEncode(qqRedirectUri)
                    + "&state=" + state;
        }
        throw new EduException(EduErrorCode.OAUTH_PLATFORM_NOT_SUPPORTED);
    }

    // ==================== State签名/校验 ====================

    private String buildState(Long userId, String type, String platform) {
        String nonce = IdUtils.simpleUuid();
        long expiry = System.currentTimeMillis() + STATE_EXPIRY_MS;
        String payload = userId + ":" + type + ":" + platform + ":" + nonce + ":" + expiry;
        String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return encodedPayload + "." + hmacSha256(encodedPayload);
    }

    private StatePayload verifyState(String state, String expectedType) {
        String[] parts = state.split("\\.");
        if (parts.length != 2) {
            throw new EduException(EduErrorCode.OAUTH_STATE_INVALID);
        }
        String encodedPayload = parts[0];
        String signature = parts[1];

        if (!hmacSha256(encodedPayload).equals(signature)) {
            throw new EduException(EduErrorCode.OAUTH_STATE_INVALID);
        }

        String payload;
        try {
            payload = new String(Base64.getUrlDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EduException(EduErrorCode.OAUTH_STATE_INVALID);
        }

        String[] fields = payload.split(":");
        if (fields.length != 5) {
            throw new EduException(EduErrorCode.OAUTH_STATE_INVALID);
        }

        long userId = Long.parseLong(fields[0]);
        String type = fields[1];
        String platform = fields[2];
        long expiry = Long.parseLong(fields[4]);

        if (!expectedType.equals(type)) {
            throw new EduException(EduErrorCode.OAUTH_STATE_INVALID);
        }
        if (System.currentTimeMillis() > expiry) {
            throw new EduException(EduErrorCode.OAUTH_STATE_INVALID);
        }

        StatePayload result = new StatePayload();
        result.userId = userId;
        result.platform = platform;
        return result;
    }

    private String hmacSha256(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(
                    stateSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            mac.init(keySpec);
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256签名失败", e);
        }
    }

    // ==================== 授权码换Token ====================

    private OAuthToken exchangeCode(String platform, String code) {
        try {
            if (PLATFORM_WECHAT.equals(platform)) {
                return exchangeWeChatCode(code);
            } else if (PLATFORM_QQ.equals(platform)) {
                return exchangeQQCode(code);
            }
            throw new EduException(EduErrorCode.OAUTH_PLATFORM_NOT_SUPPORTED);
        } catch (EduException e) {
            throw e;
        } catch (Exception e) {
            log.error("OAuth授权码交换失败: platform={}", platform, e);
            throw new EduException(EduErrorCode.OAUTH_CODE_INVALID);
        }
    }

    private OAuthToken exchangeWeChatCode(String code) throws Exception {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + wechatAppId
                + "&secret=" + wechatAppSecret + "&code=" + code + "&grant_type=authorization_code";
        String response = restTemplate.postForObject(url, null, String.class);
        JsonNode json = objectMapper.readTree(response);
        if (json.has("errcode") && json.get("errcode").asInt() != 0) {
            log.error("微信授权码交换失败: {}", response);
            throw new EduException(EduErrorCode.OAUTH_CODE_INVALID);
        }
        OAuthToken token = new OAuthToken();
        token.accessToken = json.get("access_token").asText();
        token.openId = json.get("openid").asText();
        token.unionId = json.has("unionid") ? json.get("unionid").asText() : null;
        return token;
    }

    private OAuthToken exchangeQQCode(String code) throws Exception {
        String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=" + qqAppId
                + "&client_secret=" + qqAppSecret + "&code=" + code
                + "&redirect_uri=" + urlEncode(qqRedirectUri);
        String response = restTemplate.getForObject(url, String.class);
        Map<String, String> params = parseUrlEncoded(response);
        String accessToken = params.get("access_token");
        if (accessToken == null) {
            log.error("QQ授权码交换失败: {}", response);
            throw new EduException(EduErrorCode.OAUTH_CODE_INVALID);
        }
        JsonNode qqIdentity = getQQOpenId(accessToken);
        OAuthToken token = new OAuthToken();
        token.accessToken = accessToken;
        token.openId = qqIdentity.get("openid").asText();
        token.unionId = qqIdentity.has("unionid") ? qqIdentity.get("unionid").asText() : null;
        return token;
    }

    /** 从JSONP回调中提取QQ openid和unionid */
    private JsonNode getQQOpenId(String accessToken) throws Exception {
        String url = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken + "&unionid=1";
        String response = restTemplate.getForObject(url, String.class);
        Pattern p = Pattern.compile("\\(\\s*(\\{.*\\})\\s*\\)\\s*;?\\s*$");
        Matcher m = p.matcher(response);
        if (!m.find()) {
            log.error("QQ openId解析失败: {}", response);
            throw new EduException(EduErrorCode.OAUTH_USERINFO_FAILED);
        }
        return objectMapper.readTree(m.group(1));
    }

    // ==================== 获取用户信息 ====================

    private OAuthUserInfo fetchUserInfo(String platform, OAuthToken token) {
        try {
            if (PLATFORM_WECHAT.equals(platform)) {
                return fetchWeChatUserInfo(token);
            } else if (PLATFORM_QQ.equals(platform)) {
                return fetchQQUserInfo(token);
            }
            throw new EduException(EduErrorCode.OAUTH_PLATFORM_NOT_SUPPORTED);
        } catch (EduException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取第三方用户信息失败: platform={}", platform, e);
            throw new EduException(EduErrorCode.OAUTH_USERINFO_FAILED);
        }
    }

    private OAuthUserInfo fetchWeChatUserInfo(OAuthToken token) throws Exception {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token.accessToken
                + "&openid=" + token.openId + "&lang=zh_CN";
        String response = restTemplate.getForObject(url, String.class);
        JsonNode json = objectMapper.readTree(response);
        if (json.has("errcode") && json.get("errcode").asInt() != 0) {
            log.error("获取微信用户信息失败: {}", response);
            throw new EduException(EduErrorCode.OAUTH_USERINFO_FAILED);
        }
        OAuthUserInfo info = new OAuthUserInfo();
        info.openId = token.openId;
        info.unionId = token.unionId;
        info.nickname = json.has("nickname") ? json.get("nickname").asText() : null;
        info.avatarUrl = json.has("headimgurl") ? json.get("headimgurl").asText() : null;
        return info;
    }

    private OAuthUserInfo fetchQQUserInfo(OAuthToken token) throws Exception {
        String url = "https://graph.qq.com/user/get_user_info?access_token=" + token.accessToken
                + "&oauth_consumer_key=" + qqAppId + "&openid=" + token.openId;
        String response = restTemplate.getForObject(url, String.class);
        JsonNode json = objectMapper.readTree(response);
        if (json.has("ret") && json.get("ret").asInt() != 0) {
            log.error("获取QQ用户信息失败: {}", response);
            throw new EduException(EduErrorCode.OAUTH_USERINFO_FAILED);
        }
        OAuthUserInfo info = new OAuthUserInfo();
        info.openId = token.openId;
        info.nickname = json.has("nickname") ? json.get("nickname").asText() : null;
        info.avatarUrl = json.has("figureurl_qq") ? json.get("figureurl_qq").asText() : null;
        return info;
    }

    // ==================== 工具方法 ====================

    private static Map<String, String> parseUrlEncoded(String response) {
        Map<String, String> result = new HashMap<>();
        if (response == null) return result;
        for (String pair : response.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                result.put(kv[0], kv[1]);
            }
        }
        return result;
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private static EduException configMissing(String platform) {
        log.error("OAuth配置缺失: platform={}", platform);
        return new EduException(EduErrorCode.OAUTH_CONFIG_MISSING);
    }

    // ==================== 内部数据结构 ====================

    /** 解析后的state载荷 */
    private static class StatePayload {
        Long userId;
        String platform;
    }

    /** OAuth令牌响应 */
    private static class OAuthToken {
        String accessToken;
        String openId;
        String unionId;
    }

    /** OAuth用户信息 */
    private static class OAuthUserInfo {
        String openId;
        String unionId;
        String nickname;
        String avatarUrl;
    }
}
