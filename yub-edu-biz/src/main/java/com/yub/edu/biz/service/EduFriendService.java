package com.yub.edu.biz.service;

import com.yub.edu.biz.vo.FriendVO;

import java.util.List;

/**
 * 学员好友关系服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 学员好友关系业务接口
 * @Version: 1.0.0
 */
public interface EduFriendService {

    /**
     * 查询某学员的好友列表
     *
     * @param ownerId 学员ID
     * @return 好友视图列表
     */
    List<FriendVO> listMyFriends(Long ownerId);

    /**
     * 建立双向好友关系（幂等：已存在则忽略）
     *
     * @param ownerId 归属学员
     * @param friendId 好友学员
     */
    void addFriend(Long ownerId, Long friendId);

    /**
     * 移除双向好友关系（幂等：不存在也不报错）
     *
     * @param ownerId 归属学员
     * @param friendId 好友学员
     */
    void removeFriend(Long ownerId, Long friendId);
}
