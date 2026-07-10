package com.yub.edu.biz.controller.app;

import com.yub.common.model.Response;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.service.EduFriendService;
import com.yub.edu.biz.vo.FriendVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * APP端好友关系 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: APP端学员好友关系管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/app/friend")
@RequiredArgsConstructor
public class AppFriendController {

    private final EduFriendService friendService;

    /**
     * 获取我的好友列表（按成为好友时间倒序）
     *
     * @return 好友视图列表
     */
    @GetMapping("/my")
    public Response<List<FriendVO>> myFriends() {
        Long userId = SecurityUtils.getCurrentUserId();
        return Response.success(friendService.listMyFriends(userId));
    }

    /**
     * 移除好友（删除双向关系）
     *
     * @param friendId 好友学员ID
     * @return 响应
     */
    @DeleteMapping("/{friendId}")
    public Response<Void> removeFriend(@PathVariable("friendId") Long friendId) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId.equals(friendId)) {
            throw new EduException(EduErrorCode.FRIEND_SELF);
        }
        friendService.removeFriend(userId, friendId);
        return Response.success();
    }
}
