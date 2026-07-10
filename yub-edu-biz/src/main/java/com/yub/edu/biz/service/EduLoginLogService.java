package com.yub.edu.biz.service;

import com.yub.edu.biz.entity.EduLoginLog;

import java.util.List;

/**
 * 学员登录日志服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 学员登录日志管理服务
 * @Version: 1.0.0
 */
public interface EduLoginLogService {

    /**
     * 查询学员最近登录记录
     *
     * @param studentId 学员ID
     * @param limit     限制数量
     * @return 登录日志列表
     */
    List<EduLoginLog> selectByStudentId(Long studentId, int limit);
}
