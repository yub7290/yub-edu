package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduCoursePricePlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程价格方案 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 */
@Mapper
public interface EduCoursePricePlanMapper {

    List<EduCoursePricePlan> selectByCourseId(@Param("courseId") Long courseId);

    EduCoursePricePlan selectById(@Param("id") Long id);

    int insert(EduCoursePricePlan plan);

    int updateById(EduCoursePricePlan plan);

    int deleteById(@Param("id") Long id);

    int deleteByCourseId(@Param("courseId") Long courseId);
}
