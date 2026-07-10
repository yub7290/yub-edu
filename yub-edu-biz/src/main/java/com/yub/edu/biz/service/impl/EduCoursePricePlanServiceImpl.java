package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduCoursePricePlan;
import com.yub.edu.biz.mapper.EduCoursePricePlanMapper;
import com.yub.edu.biz.service.EduCoursePricePlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程价格方案服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 课程价格方案管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduCoursePricePlanServiceImpl implements EduCoursePricePlanService {

    private final EduCoursePricePlanMapper mapper;

    @Override
    public List<EduCoursePricePlan> selectByCourseId(Long courseId) {
        return mapper.selectByCourseId(courseId);
    }

    @Override
    public EduCoursePricePlan selectById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public int insert(EduCoursePricePlan plan) {
        return mapper.insert(plan);
    }

    @Override
    public int updateById(EduCoursePricePlan plan) {
        return mapper.updateById(plan);
    }

    @Override
    public int deleteById(Long id) {
        return mapper.deleteById(id);
    }
}
