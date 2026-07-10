package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduFriend;
import com.yub.edu.biz.mapper.EduFriendMapper;
import com.yub.edu.biz.service.EduFriendService;
import com.yub.edu.biz.vo.FriendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 学员好友关系服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 学员好友关系业务实现（双向存储）
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduFriendServiceImpl implements EduFriendService {

    private final EduFriendMapper friendMapper;

    @Override
    public List<FriendVO> listMyFriends(Long ownerId) {
        return friendMapper.selectMyFriends(ownerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFriend(Long ownerId, Long friendId) {
        if (ownerId == null || friendId == null || ownerId.equals(friendId)) {
            return;
        }
        // 已存在则忽略，保证幂等
        if (friendMapper.countByPair(ownerId, friendId) > 0) {
            return;
        }
        EduFriend forward = new EduFriend();
        forward.setStudentId(ownerId);
        forward.setFriendId(friendId);
        friendMapper.insert(forward);

        EduFriend backward = new EduFriend();
        backward.setStudentId(friendId);
        backward.setFriendId(ownerId);
        friendMapper.insert(backward);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFriend(Long ownerId, Long friendId) {
        if (ownerId == null || friendId == null) {
            return;
        }
        friendMapper.deletePair(ownerId, friendId);
    }
}
