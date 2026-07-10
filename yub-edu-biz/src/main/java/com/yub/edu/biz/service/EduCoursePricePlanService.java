package com.yub.edu.biz.service;

import com.yub.edu.biz.entity.EduCoursePricePlan;

import java.util.List;

/**
 * 课程价格方案服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 课程价格方案管理服务
 * @Version: 1.0.0
 */
public interface EduCoursePricePlanService {

    /**
     * 根据课程ID查询价格方案列表
     *
     * @param courseId 课程ID
     * @return 价格方案列表
     */
    List<EduCoursePricePlan> selectByCourseId(Long courseId);

    /**
     * 根据ID查询价格方案
     *
     * @param id 价格方案ID
     * @return 价格方案
     */
    EduCoursePricePlan selectById(Long id);

    /**
     * 新增价格方案
     *
     * @param plan 价格方案
     * @return 影响行数
     */
    int insert(EduCoursePricePlan plan);

    /**
     * 更新价格方案
     *
     * @param plan 价格方案
     * @return 影响行数
     */
    int updateById(EduCoursePricePlan plan);

    /**
     * 删除价格方案
     *
     * @param id 价格方案ID
     * @return 影响行数
     */
    int deleteById(Long id);
}
