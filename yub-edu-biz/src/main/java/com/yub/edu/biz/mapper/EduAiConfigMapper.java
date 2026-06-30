package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduAiConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * AI助教配置 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: AI助教配置数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduAiConfigMapper {

    /**
     * 根据课程ID查询配置
     *
     * @param courseId 课程ID
     * @return AI助教配置
     */
    EduAiConfig selectByCourseId(@Param("courseId") Long courseId);

    /**
     * 新增配置
     *
     * @param config 配置信息
     * @return 影响行数
     */
    int insert(EduAiConfig config);

    /**
     * 更新配置
     *
     * @param config 配置信息
     * @return 影响行数
     */
    int update(EduAiConfig config);

    /**
     * 根据课程ID删除配置
     *
     * @param courseId 课程ID
     * @return 影响行数
     */
    int deleteByCourseId(@Param("courseId") Long courseId);
}
