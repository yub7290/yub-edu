package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduLoginLog;
import com.yub.edu.biz.mapper.EduLoginLogMapper;
import com.yub.edu.biz.service.EduLoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学员登录日志服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 学员登录日志管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduLoginLogServiceImpl implements EduLoginLogService {

    private final EduLoginLogMapper mapper;

    @Override
    public List<EduLoginLog> selectByStudentId(Long studentId, int limit) {
        return mapper.selectByStudentId(studentId, limit);
    }
}
