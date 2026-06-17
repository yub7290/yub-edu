package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduExamQuestionTypeConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷试题类型配置 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试卷试题类型配置数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduExamQuestionTypeConfigMapper {

    /**
     * 批量新增配置
     *
     * @param list 配置列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<EduExamQuestionTypeConfig> list);

    /**
     * 根据试卷ID删除配置
     *
     * @param examId 试卷ID
     * @return 影响行数
     */
    int deleteByExamId(@Param("examId") Long examId);

    /**
     * 根据试卷ID查询配置列表
     *
     * @param examId 试卷ID
     * @return 配置列表
     */
    List<EduExamQuestionTypeConfig> selectByExamId(@Param("examId") Long examId);
}
