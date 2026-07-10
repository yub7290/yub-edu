package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduHomeworkQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 作业批改题目 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业批改题目数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduHomeworkQuestionMapper {

    /**
     * 根据批改记录ID查询题目列表
     *
     * @param correctionId 批改记录ID
     * @return 题目列表
     */
    List<EduHomeworkQuestion> selectByCorrectionId(@Param("correctionId") Long correctionId);

    /**
     * 批量新增题目
     *
     * @param questions 题目列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<EduHomeworkQuestion> questions);

    /**
     * 更新题目
     *
     * @param question 题目
     * @return 影响行数
     */
    int updateById(EduHomeworkQuestion question);

    /**
     * 根据ID查询题目
     *
     * @param id 题目ID
     * @return 题目
     */
    EduHomeworkQuestion selectById(@Param("id") Long id);

    /**
     * 根据批改记录ID删除题目
     *
     * @param correctionId 批改记录ID
     * @return 影响行数
     */
    int deleteByCorrectionId(@Param("correctionId") Long correctionId);

    /**
     * 重新计算批改记录的统计数据
     *
     * @param correctionId 批改记录ID
     * @return 正确数和总数
     */
    Map<String, Object> recalculateStats(@Param("correctionId") Long correctionId);
}
