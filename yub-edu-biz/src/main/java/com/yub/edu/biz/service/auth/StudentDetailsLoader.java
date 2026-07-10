package com.yub.edu.biz.service.auth;

import com.yub.edu.biz.entity.EduStudent;
import com.yub.edu.biz.mapper.EduStudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学员 UserDetailsService 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 从 edu_student 表加载学员 UserDetails，供 Spring Security 鉴权使用
 * @Version: 1.0.0
 */
@Slf4j
@Service("studentUserDetailsService")
@RequiredArgsConstructor
public class StudentDetailsLoader implements UserDetailsService {

    private final EduStudentMapper eduStudentMapper;

    /**
     * 根据用户名加载学员 UserDetails
     * <p>用户名为纯数字 ID（Filter 已剥离 TYPE: 前缀）</p>
     *
     * @param username 用户名（纯数字学员ID）
     * @return UserDetails
     * @throws UsernameNotFoundException 学员不存在时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long studentId = Long.parseLong(username);

        EduStudent student = eduStudentMapper.selectById(studentId);
        if (student == null) {
            log.warn("学员用户不存在: {}", username);
            throw new UsernameNotFoundException("学员用户不存在: " + username);
        }

        // 学员固定角色: ROLE_STUDENT
        List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities =
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_STUDENT"));

        // 密码不需要，登录时单独校验; accountNonLocked 根据状态判断
        return new User(
                username,
                "",
                student.getStatus() != 0,
                true, true, true,
                authorities
        );
    }
}
