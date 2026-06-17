package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduExamChapterQuestionConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷章节出题配置 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试卷章节出题配置数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduExamChapterQuestionConfigMapper {

    /**
     * 批量新增配置
     *
     * @param list 配置列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<EduExamChapterQuestionConfig> list);

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
    List<EduExamChapterQuestionConfig> selectByExamId(@Param("examId") Long examId);
}
