package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.service.EduTeacherRoleService;
import com.yub.framework.security.JwtProvider;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师角色 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 教师角色分配接口（仅管理员可调用）
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/teacher-role")
@RequiredArgsConstructor
public class EduTeacherRoleController {

    private final EduTeacherRoleService eduTeacherRoleService;

    /**
     * 查询教师的角色ID列表
     *
     * @param teacherId 教师ID
     * @return 角色ID列表
     */
    @GetMapping("/{teacherId}/roles")
    public Response<List<Long>> getRoles(@PathVariable("teacherId") Long teacherId) {
        checkAdmin();
        return Response.success(eduTeacherRoleService.getTeacherRoleIds(teacherId));
    }

    /**
     * 分配教师角色（先删后插）
     *
     * @param teacherId 教师ID
     * @param roleIds   角色ID列表
     * @return 响应
     */
    @PostMapping("/{teacherId}/roles")
    public Response<Void> assignRoles(@PathVariable("teacherId") Long teacherId,
                                      @RequestBody List<Long> roleIds) {
        checkAdmin();
        Long operatorId = SecurityUtils.getCurrentUserId();
        eduTeacherRoleService.assignRoles(teacherId, roleIds, operatorId);
        return Response.success();
    }

    /**
     * 校验当前用户是否为管理员
     */
    private void checkAdmin() {
        if (!JwtProvider.USER_TYPE_ADMIN.equals(SecurityUtils.getCurrentUserType())) {
            throw new com.yub.framework.exception.FrameworkException(
                    com.yub.framework.exception.FrameworkErrorCode.UNAUTHORIZED);
        }
    }
}
