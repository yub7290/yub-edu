package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.StudyCardInstanceQueryDTO;
import com.yub.edu.biz.entity.EduStudyCardInstance;
import com.yub.edu.biz.vo.StudyCardInstancePageRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习卡实例 Mapper 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡实例数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduStudyCardInstanceMapper {

    /**
     * 分页查询学习卡实例
     *
     * @param cardId 卡模板ID
     * @param query  查询参数
     * @return 学习卡实例分页列表
     */
    List<StudyCardInstancePageRespVO> selectPage(@Param("cardId") Long cardId,
                                                 @Param("query") StudyCardInstanceQueryDTO query);

    /**
     * 根据卡号查询学习卡实例
     *
     * @param cardNo 卡号
     * @return 学习卡实例
     */
    EduStudyCardInstance selectByCardNo(@Param("cardNo") String cardNo);

    /**
     * 根据用户ID查询学习卡实例列表
     *
     * @param userId 用户ID
     * @return 学习卡实例列表
     */
    List<EduStudyCardInstance> selectByUserId(@Param("userId") Long userId);

    /**
     * 新增学习卡实例
     *
     * @param entity 学习卡实例
     * @return 影响行数
     */
    int insert(EduStudyCardInstance entity);

    /**
     * 批量新增学习卡实例
     *
     * @param list 学习卡实例列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<EduStudyCardInstance> list);

    /**
     * 更新学习卡实例状态
     *
     * @param id     实例ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 使用学习卡
     *
     * @param id          实例ID
     * @param userId      用户ID
     * @param userAccount 用户账号
     * @return 影响行数
     */
    int useCard(@Param("id") Long id, @Param("userId") Long userId,
                @Param("userAccount") String userAccount);

    /**
     * 回滚学习卡实例
     *
     * @param id 实例ID
     * @return 影响行数
     */
    int rollbackById(@Param("id") Long id);

    /**
     * 绑定用户到学习卡实例（暂存功能，不改变使用状态）
     *
     * @param id     实例ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int bindUser(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 统计已使用的学习卡数量
     *
     * @param cardId 卡模板ID
     * @return 已使用数量
     */
    int countUsedByCardId(@Param("cardId") Long cardId);

    /**
     * 统计未使用的学习卡数量（可用于积分商店库存显示）
     *
     * @param cardId 卡模板ID
     * @return 未使用数量
     */
    int countAvailableByCardId(@Param("cardId") Long cardId);

    /**
     * 统计用户的学习卡总数
     *
     * @param userId 用户ID
     * @return 总数
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 统计用户已使用的学习卡数量
     *
     * @param userId 用户ID
     * @return 已使用数量
     */
    long countUsedByUserId(@Param("userId") Long userId);

    /**
     * 根据ID查询学习卡实例
     *
     * @param id 主键
     * @return 学习卡实例
     */
    EduStudyCardInstance selectById(@Param("id") Long id);

    /**
     * 更新学习卡实例
     *
     * @param entity 学习卡实例
     * @return 影响行数
     */
    int updateById(EduStudyCardInstance entity);

    /**
     * 根据卡模板ID逻辑删除所有实例
     *
     * @param cardId 卡模板ID
     * @return 影响行数
     */
    int deleteByCardId(@Param("cardId") Long cardId);

    /**
     * 为用户分配一张未使用的学习卡（status=0 → 1）
     *
     * @param cardId 卡模板ID
     * @param userId 用户ID
     * @return 影响行数（0表示无可用卡）
     */
    int bindUserByCardId(@Param("cardId") Long cardId, @Param("userId") Long userId);
}
