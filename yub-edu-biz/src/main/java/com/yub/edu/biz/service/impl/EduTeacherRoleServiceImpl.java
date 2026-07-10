package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.mapper.EduTeacherRoleMapper;
import com.yub.edu.biz.service.EduTeacherRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 教师角色关联 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 教师角色关联业务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduTeacherRoleServiceImpl implements EduTeacherRoleService {

    private final EduTeacherRoleMapper eduTeacherRoleMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getTeacherRoleIds(Long teacherId) {
        return eduTeacherRoleMapper.selectRoleIdsByTeacherId(teacherId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long teacherId, List<Long> roleIds, Long operatorId) {
        eduTeacherRoleMapper.deleteByTeacherId(teacherId);
        if (!CollectionUtils.isEmpty(roleIds)) {
            eduTeacherRoleMapper.batchInsert(teacherId, roleIds, operatorId);
        }
        log.info("教师 {} 角色已更新，roleIds={}，操作者={}", teacherId, roleIds, operatorId);
    }
}
