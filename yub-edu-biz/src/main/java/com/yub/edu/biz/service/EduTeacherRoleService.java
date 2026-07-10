package com.yub.edu.biz.service;

import java.util.List;

/**
 * 教师角色关联 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 教师角色关联业务接口
 * @Version: 1.0.0
 */
public interface EduTeacherRoleService {

    /**
     * 查询教师的角色ID列表
     *
     * @param teacherId 教师ID
     * @return 角色ID列表
     */
    List<Long> getTeacherRoleIds(Long teacherId);

    /**
     * 分配教师角色（先删后插，事务性）
     *
     * @param teacherId  教师ID
     * @param roleIds    角色ID列表
     * @param operatorId 操作人ID
     */
    void assignRoles(Long teacherId, List<Long> roleIds, Long operatorId);
}
