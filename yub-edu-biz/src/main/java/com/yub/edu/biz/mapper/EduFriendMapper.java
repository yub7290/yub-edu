package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduFriend;
import com.yub.edu.biz.vo.FriendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学员好友关系 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 学员好友关系数据访问
 * @Version: 1.0.0
 */
@Mapper
public interface EduFriendMapper {

    /**
     * 新增好友关系（单条，双向需插入两条）
     *
     * @param friend 好友关系实体
     * @return 影响行数
     */
    int insert(EduFriend friend);

    /**
     * 查询某学员的好友列表（联表学员表取展示字段）
     *
     * @param ownerId 学员ID
     * @return 好友视图列表（按成为好友时间倒序）
     */
    List<FriendVO> selectMyFriends(@Param("ownerId") Long ownerId);

    /**
     * 判断某关系是否已存在（未删除）
     *
     * @param studentId 归属学员
     * @param friendId  好友学员
     * @return 是否存在
     */
    int countByPair(@Param("studentId") Long studentId, @Param("friendId") Long friendId);

    /**
     * 删除双向好友关系（A→B 与 B→A 两条）
     *
     * @param a 一方学员ID
     * @param b 另一方学员ID
     * @return 影响行数
     */
    int deletePair(@Param("a") Long a, @Param("b") Long b);
}
