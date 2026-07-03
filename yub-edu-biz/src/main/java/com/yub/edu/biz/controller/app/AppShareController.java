package com.yub.edu.biz.controller.app;

import com.yub.common.model.Response;
import com.yub.edu.biz.service.PointsService;
import com.yub.framework.security.SecurityUtils;
import com.yub.system.service.param.SysParamService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP-分享 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: APP端分享相关接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/app/share")
@RequiredArgsConstructor
public class AppShareController {

    private final PointsService pointsService;
    private final SysParamService sysParamService;

    /**
     * 记录分享行为并奖励积分
     * @return 响应
     */
    @PostMapping("/record")
    public Response<Void> recordShare() {
        Long userId = SecurityUtils.getCurrentUserId();
        String pointsStr = sysParamService.getValueByCode("points_share");
        int points = pointsStr != null ? Integer.parseInt(pointsStr) : 0;
        if (points > 0) {
            pointsService.earnPoints(userId, points, 1, "分享奖励", null, "share");
        }
        return Response.success();
    }

    /**
     * 处理邀请注册（邀请人和被邀请人各获得积分）
     * @param req 邀请参数
     * @return 响应
     */
    @PostMapping("/register")
    public Response<Void> register(@RequestBody RegisterReqDTO req) {
        Long userId = SecurityUtils.getCurrentUserId();
        String pointsStr = sysParamService.getValueByCode("points_share_register");
        int points = pointsStr != null ? Integer.parseInt(pointsStr) : 0;
        if (points > 0) {
            pointsService.earnPoints(req.getInviterId(), points, 1, "邀请注册奖励", null, "share_register");
            pointsService.earnPoints(userId, points, 1, "被邀请注册奖励", null, "share_register");
        }
        return Response.success();
    }

    /**
     * 邀请注册请求DTO
     */
    @Data
    public static class RegisterReqDTO {
        /** 邀请人ID */
        private Long inviterId;
    }
}
