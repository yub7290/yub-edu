package com.yub.edu.biz.service.impl;

import com.wf.captcha.SpecCaptcha;
import com.yub.common.constant.RedisKeyConstants;
import com.yub.common.enums.StatusEnum;
import com.yub.common.util.IdUtils;
import com.yub.edu.biz.dto.StudentLoginReqDTO;
import com.yub.edu.biz.entity.EduStudent;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduStudentMapper;
import com.yub.edu.biz.service.StudentAuthService;
import com.yub.edu.biz.vo.CaptchaRespVO;
import com.yub.edu.biz.vo.StudentInfoRespVO;
import com.yub.edu.biz.vo.StudentLoginRespVO;
import com.yub.framework.redis.RedisUtils;
import com.yub.framework.security.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;

/**
 * 学员认证服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学员认证服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentAuthServiceImpl implements StudentAuthService {

    private final EduStudentMapper eduStudentMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private static final int CAPTCHA_EXPIRE_MINUTES = 5;
    private static final int CAPTCHA_WIDTH = 130;
    private static final int CAPTCHA_HEIGHT = 48;
    private static final int CAPTCHA_LENGTH = 4;

    /** 学员RefreshToken Redis Key前缀 */
    private static final String STUDENT_REFRESH_TOKEN_PREFIX = "student_refresh_token:";

    @Override
    public CaptchaRespVO generateCaptcha() {
        SpecCaptcha captcha = new SpecCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, CAPTCHA_LENGTH);
        String text = captcha.text();
        String base64 = captcha.toBase64();

        String captchaKey = IdUtils.simpleUuid();
        RedisUtils.set(RedisKeyConstants.CAPTCHA_PREFIX + captchaKey, text, CAPTCHA_EXPIRE_MINUTES);

        return CaptchaRespVO.builder()
                .key(captchaKey)
                .image(base64)
                .build();
    }

    @Override
    public StudentLoginRespVO login(StudentLoginReqDTO reqDTO, String ip) {
        // 1. 验证码校验
        String captchaKey = RedisKeyConstants.CAPTCHA_PREFIX + reqDTO.getCaptchaKey();
        String storedCode = RedisUtils.getAndDelete(captchaKey).orElse("").toString();
        if (StringUtils.isBlank(storedCode) || !storedCode.equalsIgnoreCase(reqDTO.getCaptchaCode())) {
            throw new EduException(EduErrorCode.CAPTCHA_ERROR);
        }

        // 2. 查询学员
        EduStudent student = eduStudentMapper.selectByAccount(reqDTO.getAccount());
        if (student == null) {
            throw new EduException(EduErrorCode.STUDENT_PASSWORD_ERROR);
        }

        // 3. 密码校验（前端传SM3哈希，后端BCrypt比对）
        if (!passwordEncoder.matches(reqDTO.getPassword(), student.getPassword())) {
            throw new EduException(EduErrorCode.STUDENT_PASSWORD_ERROR);
        }

        // 4. 状态检查
        if (StatusEnum.isDisabled(student.getStatus())) {
            throw new EduException(EduErrorCode.STUDENT_ACCOUNT_DISABLED);
        }

        // 5. 生成JWT
        String accessToken = jwtProvider.generateAccessToken(student.getId(), student.getAccount(), new HashMap<>());
        String refreshToken = jwtProvider.generateRefreshToken(student.getId());

        // 6. 存储RefreshToken到Redis
        String refreshSetKey = STUDENT_REFRESH_TOKEN_PREFIX + student.getId();
        RedisUtils.addToSet(refreshSetKey, refreshToken);
        RedisUtils.expireSet(refreshSetKey, Duration.ofDays(7));

        // 7. 更新最后登录时间
        eduStudentMapper.updateLastLoginTime(student.getId());

        log.info("学员登录成功: account={}, ip={}", student.getAccount(), ip);

        return StudentLoginRespVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public StudentLoginRespVO refresh(String refreshToken) {
        Claims claims = jwtProvider.parseTokenIfValid(refreshToken);
        if (claims == null) {
            throw new EduException(EduErrorCode.TOKEN_EXPIRED);
        }

        String studentId = claims.getSubject();
        String refreshSetKey = STUDENT_REFRESH_TOKEN_PREFIX + studentId;
        String lockKey = "lock:" + refreshSetKey;
        RedisUtils.lock(lockKey);
        try {
            if (!RedisUtils.setContains(refreshSetKey, refreshToken)) {
                throw new EduException(EduErrorCode.TOKEN_INVALID);
            }

            EduStudent student = eduStudentMapper.selectById(Long.valueOf(studentId));
            if (student == null || StatusEnum.isDisabled(student.getStatus())) {
                throw new EduException(EduErrorCode.STUDENT_ACCOUNT_DISABLED);
            }

            String newAccessToken = jwtProvider.generateAccessToken(student.getId(), student.getAccount(), new HashMap<>());
            String newRefreshToken = jwtProvider.generateRefreshToken(student.getId());

            RedisUtils.removeFromSet(refreshSetKey, refreshToken);
            RedisUtils.addToSet(refreshSetKey, newRefreshToken);
            RedisUtils.expireSet(refreshSetKey, Duration.ofDays(7));

            return StudentLoginRespVO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } finally {
            RedisUtils.unlock(lockKey);
        }
    }

    @Override
    public StudentInfoRespVO getStudentInfo(Long studentId) {
        EduStudent student = eduStudentMapper.selectById(studentId);
        if (student == null) {
            throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
        }

        return StudentInfoRespVO.builder()
                .id(student.getId())
                .account(student.getAccount())
                .name(student.getName())
                .avatarUrl(student.getAvatarUrl())
                .studentNo(student.getStudentNo())
                .gender(student.getGender())
                .phone(student.getPhone())
                .email(student.getEmail())
                .build();
    }
}
