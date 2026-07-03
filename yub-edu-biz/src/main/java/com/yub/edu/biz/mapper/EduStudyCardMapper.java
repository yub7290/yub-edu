package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.StudyCardQueryDTO;
import com.yub.edu.biz.entity.EduStudyCard;
import com.yub.edu.biz.vo.StudyCardPageRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习卡模板 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡模板数据访问
 * @Version: 1.0.0
 */
@Mapper
public interface EduStudyCardMapper {

    /**
     * 分页查询学习卡
     *
     * @param query 查询参数
     * @return 分页数据
     */
    List<StudyCardPageRespVO> selectPage(@Param("query") StudyCardQueryDTO query);

    /**
     * 根据ID查询学习卡
     *
     * @param id 主键
     * @return 学习卡实体
     */
    EduStudyCard selectById(@Param("id") Long id);

    /**
     * 插入学习卡
     *
     * @param entity 学习卡实体
     * @return 影响行数
     */
    int insert(EduStudyCard entity);

    /**
     * 更新学习卡
     *
     * @param entity 学习卡实体
     * @return 影响行数
     */
    int updateById(EduStudyCard entity);

    /**
     * 逻辑删除学习卡
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新学习卡状态
     *
     * @param id     主键
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 查询学习卡简单列表（仅 id + title，用于下拉选择）
     *
     * @return 学习卡列表
     */
    List<EduStudyCard> selectSimpleList();
}
