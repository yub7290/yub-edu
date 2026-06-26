package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduQuestionOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试题选项 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试题选项数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduQuestionOptionMapper {

    /**
     * 根据试题ID查询选项列表
     *
     * @param questionId 试题ID
     * @return 选项列表
     */
    List<EduQuestionOption> selectByQuestionId(@Param("questionId") Long questionId);

    /**
     * 批量插入选项
     *
     * @param list 选项列表
     * @return 影响行数
     */
    int insertBatch(List<EduQuestionOption> list);

    /**
     * 根据试题ID列表批量查询选项列表
     *
     * @param questionIds 试题ID列表
     * @return 选项列表
     */
    List<EduQuestionOption> selectByQuestionIds(@Param("questionIds") List<Long> questionIds);

    /**
     * 根据试题ID删除所有选项
     *
     * @param questionId 试题ID
     * @return 影响行数
     */
    int deleteByQuestionId(@Param("questionId") Long questionId);
}
