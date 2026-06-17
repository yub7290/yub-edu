package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.QuestionQueryDTO;
import com.yub.edu.biz.entity.EduQuestion;
import com.yub.edu.biz.vo.QuestionPageRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试题 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试题数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduQuestionMapper {

    /**
     * 分页查询试题
     *
     * @param query 查询参数
     * @return 试题分页列表
     */
    List<QuestionPageRespVO> selectPage(@Param("query") QuestionQueryDTO query);

    /**
     * 根据ID查询试题
     *
     * @param id 试题ID
     * @return 试题
     */
    EduQuestion selectById(@Param("id") Long id);

    /**
     * 新增试题
     *
     * @param question 试题
     * @return 影响行数
     */
    int insert(EduQuestion question);

    /**
     * 更新试题
     *
     * @param question 试题
     * @return 影响行数
     */
    int updateById(EduQuestion question);

    /**
     * 逻辑删除试题
     *
     * @param id 试题ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 恢复试题
     *
     * @param id 试题ID
     * @return 影响行数
     */
    int restoreById(@Param("id") Long id);

    /**
     * 更新试题状态
     *
     * @param id     试题ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 查询回收站列表
     *
     * @return 已删除的试题列表
     */
    List<EduQuestion> selectRecycleList();

    /**
     * 物理删除试题
     *
     * @param id 试题ID
     * @return 影响行数
     */
    int deletePhysicallyById(@Param("id") Long id);
}
