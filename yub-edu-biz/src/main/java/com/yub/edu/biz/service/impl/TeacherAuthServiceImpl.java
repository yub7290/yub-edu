package com.yub.edu.biz.service.impl;

import com.yub.common.constant.RedisKeyConstants;
import com.yub.common.enums.StatusEnum;
import com.yub.edu.biz.dto.TeacherLoginReqDTO;
import com.yub.edu.biz.entity.EduTeacher;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduTeacherMapper;
import com.yub.edu.biz.service.TeacherAuthService;
import com.yub.edu.biz.vo.LoginRespVO;
import com.yub.edu.biz.vo.TeacherUserInfoRespVO;
import com.yub.framework.redis.RedisUtils;
import com.yub.framework.security.JwtProvider;
import com.yub.system.entity.menu.SysMenu;
import com.yub.system.mapper.menu.SysMenuMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 教师权限服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 教师登录认证、Token 刷新、用户信息查询
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherAuthServiceImpl implements TeacherAuthService {

    private final EduTeacherMapper eduTeacherMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    // TODO: 架构治理 - 跨模块依赖: yub-edu 不应直接依赖 yub-system 的 Mapper
    private final SysMenuMapper sysMenuMapper;

    private static final int TEACHER_REFRESH_TOKEN_EXPIRE_DAYS = 7;
    private static final String TEACHER_REFRESH_TOKEN_PREFIX = "teacher_refresh_token:";

    @Override
    public LoginRespVO login(TeacherLoginReqDTO reqDTO, String ip) {
        // 验证码校验
        String captchaKey = RedisKeyConstants.CAPTCHA_PREFIX + reqDTO.getCaptchaKey();
        String storedCode = RedisUtils.getAndDelete(captchaKey).orElse("").toString();
        if (StringUtils.isBlank(storedCode) || !storedCode.equalsIgnoreCase(reqDTO.getCaptchaCode())) {
            throw new EduException(EduErrorCode.CAPTCHA_ERROR);
        }

        // 查询教师
        EduTeacher teacher = eduTeacherMapper.selectByAccount(reqDTO.getAccount());
        if (teacher == null) {
            throw new EduException(EduErrorCode.TEACHER_PASSWORD_ERROR);
        }

        // 密码校验
        if (!passwordEncoder.matches(reqDTO.getPassword(), teacher.getPassword())) {
            throw new EduException(EduErrorCode.TEACHER_PASSWORD_ERROR);
        }

        // 状态检查
        if (StatusEnum.isDisabled(teacher.getStatus())) {
            throw new EduException(EduErrorCode.TEACHER_ACCOUNT_DISABLED);
        }

        // 生成 Token
        String accessToken = jwtProvider.generateAccessToken(
                teacher.getId(), teacher.getAccount(), JwtProvider.USER_TYPE_TEACHER, new HashMap<>());
        String refreshToken = jwtProvider.generateRefreshToken(teacher.getId(), JwtProvider.USER_TYPE_TEACHER);

        // 存储 refresh_token 到 Redis
        String refreshSetKey = TEACHER_REFRESH_TOKEN_PREFIX + teacher.getId();
        RedisUtils.addToSet(refreshSetKey, refreshToken);
        RedisUtils.expireSet(refreshSetKey, Duration.ofDays(TEACHER_REFRESH_TOKEN_EXPIRE_DAYS));

        log.info("教师登录成功: account={}, ip={}", teacher.getAccount(), ip);
        return LoginRespVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginRespVO refresh(String refreshToken) {
        Claims claims = jwtProvider.parseTokenIfValid(refreshToken);
        if (claims == null) {
            throw new EduException(EduErrorCode.TOKEN_EXPIRED);
        }

        String subject = claims.getSubject();
        String teacherId = subject.contains(":") ? subject.substring(subject.indexOf(":") + 1) : subject;
        String refreshSetKey = TEACHER_REFRESH_TOKEN_PREFIX + teacherId;
        String lockKey = "lock:" + refreshSetKey;

        RedisUtils.lock(lockKey);
        try {
            if (!RedisUtils.setContains(refreshSetKey, refreshToken)) {
                throw new EduException(EduErrorCode.TOKEN_INVALID);
            }

            EduTeacher teacher = eduTeacherMapper.selectById(Long.valueOf(teacherId));
            if (teacher == null || StatusEnum.isDisabled(teacher.getStatus())) {
                throw new EduException(EduErrorCode.TEACHER_ACCOUNT_DISABLED);
            }

            String newAccessToken = jwtProvider.generateAccessToken(
                    teacher.getId(), teacher.getAccount(), JwtProvider.USER_TYPE_TEACHER, new HashMap<>());
            String newRefreshToken = jwtProvider.generateRefreshToken(teacher.getId(), JwtProvider.USER_TYPE_TEACHER);

            RedisUtils.removeFromSet(refreshSetKey, refreshToken);
            RedisUtils.addToSet(refreshSetKey, newRefreshToken);
            RedisUtils.expireSet(refreshSetKey, Duration.ofDays(TEACHER_REFRESH_TOKEN_EXPIRE_DAYS));

            return LoginRespVO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } finally {
            RedisUtils.unlock(lockKey);
        }
    }

    /**
     * 教师登出：删除 RefreshToken 集合，将 AccessToken 加入黑名单（TTL=AccessToken 剩余有效期）
     *
     * @param teacherId    教师ID
     * @param accessToken  当前 AccessToken
     */
    @Override
    public void logout(Long teacherId, String accessToken) {
        String refreshSetKey = TEACHER_REFRESH_TOKEN_PREFIX + teacherId;
        String lockKey = "lock:" + refreshSetKey;
        RedisUtils.lock(lockKey);
        try {
            RedisUtils.deleteSet(refreshSetKey);

            Claims claims = jwtProvider.parseTokenIfValid(accessToken);
            if (claims != null) {
                long remaining = claims.getExpiration().getTime() - System.currentTimeMillis();
                if (remaining > 0) {
                    String tokenHash = DigestUtils.sha256Hex(accessToken);
                    RedisUtils.set(RedisKeyConstants.TOKEN_BLACKLIST_PREFIX + tokenHash, "1", Duration.ofMillis(remaining));
                }
            }
        } finally {
            RedisUtils.unlock(lockKey);
        }
    }

    @Override
    public TeacherUserInfoRespVO getCurrentUserInfo(Long teacherId) {
        EduTeacher teacher = eduTeacherMapper.selectById(teacherId);
        if (teacher == null) {
            throw new EduException(EduErrorCode.TEACHER_NOT_FOUND);
        }

        List<String> roles = eduTeacherMapper.selectRoleCodesByTeacherId(teacherId);

        // 加载菜单树和权限标识
        List<SysMenu> allMenus = sysMenuMapper.selectByTeacherId(teacherId);
        List<Object> menuTree = buildMenuTree(allMenus);
        List<String> permissions = allMenus.stream()
                .filter(m -> m.getPermission() != null && !m.getPermission().isEmpty())
                .map(SysMenu::getPermission)
                .distinct()
                .collect(Collectors.toList());

        return TeacherUserInfoRespVO.builder()
                .id(teacher.getId())
                .account(teacher.getAccount())
                .name(teacher.getName())
                .avatarUrl(teacher.getAvatarUrl())
                .gender(teacher.getGender())
                .phone(teacher.getPhone())
                .email(teacher.getEmail())
                .roles(roles)
                .menus(menuTree)
                .permissions(permissions)
                .build();
    }

    /**
     * 构建菜单树（递归）
     */
    private List<Object> buildMenuTree(List<SysMenu> menus) {
        List<Object> tree = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                tree.add(buildMenuNode(menu, menus));
            }
        }
        return tree;
    }

    private Object buildMenuNode(SysMenu menu, List<SysMenu> allMenus) {
        var node = new java.util.LinkedHashMap<String, Object>();
        node.put("id", menu.getId());
        node.put("name", menu.getName());
        node.put("path", menu.getPath());
        node.put("component", menu.getComponent());
        node.put("icon", menu.getIcon());
        node.put("sort", menu.getSort());
        node.put("menuType", menu.getMenuType());
        node.put("permission", menu.getPermission());

        List<Object> children = new ArrayList<>();
        for (SysMenu child : allMenus) {
            if (menu.getId().equals(child.getParentId())) {
                children.add(buildMenuNode(child, allMenus));
            }
        }
        if (!children.isEmpty()) {
            node.put("children", children);
        }
        return node;
    }
}
