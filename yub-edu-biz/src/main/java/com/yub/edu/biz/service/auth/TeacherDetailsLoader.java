package com.yub.edu.biz.service.auth;

import com.yub.edu.biz.entity.EduTeacher;
import com.yub.edu.biz.mapper.EduTeacherMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 教师 UserDetailsService 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 从 edu_teacher 表加载教师 UserDetails，供 Spring Security 鉴权使用
 * @Version: 1.0.0
 */
@Slf4j
@Service("teacherUserDetailsService")
@RequiredArgsConstructor
public class TeacherDetailsLoader implements UserDetailsService {

    private final EduTeacherMapper eduTeacherMapper;

    /**
     * 根据用户名加载教师 UserDetails
     * <p>用户名格式: TEACHER:{teacherId}，与 JwtProvider subject 格式一致</p>
     *
     * @param username 用户名（格式: TEACHER:{teacherId}）
     * @return UserDetails
     * @throws UsernameNotFoundException 教师不存在时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ponytail: Filter 已剥离 TYPE: 前缀，username 为纯数字 ID
        Long teacherId = Long.parseLong(username);

        EduTeacher teacher = eduTeacherMapper.selectById(teacherId);
        if (teacher == null) {
            log.warn("教师用户不存在: {}", username);
            throw new UsernameNotFoundException("教师用户不存在: " + username);
        }

        List<String> roleCodes = eduTeacherMapper.selectRoleCodesByTeacherId(teacherId);
        List<SimpleGrantedAuthority> authorities = roleCodes.stream()
                .map(code -> new SimpleGrantedAuthority("ROLE_" + code))
                .toList();

        // ponytail: 密码不需要，登录时单独校验
        return new User(
                username,
                "",
                teacher.getStatus() != 0,
                true, true, true,
                authorities
        );
    }
}
