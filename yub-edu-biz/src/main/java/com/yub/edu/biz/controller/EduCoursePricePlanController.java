package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduCoursePricePlan;
import com.yub.edu.biz.mapper.EduCoursePricePlanMapper;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程价格方案 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 */
@RestController
@RequestMapping("/edu/course/price-plan")
@RequiredArgsConstructor
public class EduCoursePricePlanController {

    private final EduCoursePricePlanMapper pricePlanMapper;

    @GetMapping("/list/{courseId}")
    public Response<List<EduCoursePricePlan>> list(@PathVariable Long courseId) {
        return Response.success(pricePlanMapper.selectByCourseId(courseId));
    }

    @PostMapping
    public Response<Long> create(@RequestBody EduCoursePricePlan plan) {
        Long userId = SecurityUtils.getCurrentUserId();
        plan.setCreateBy(userId);
        plan.setUpdateBy(userId);
        if (plan.getSort() == null) plan.setSort(0);
        pricePlanMapper.insert(plan);
        return Response.success(plan.getId());
    }

    @PutMapping
    public Response<Void> update(@RequestBody EduCoursePricePlan plan) {
        plan.setUpdateBy(SecurityUtils.getCurrentUserId());
        pricePlanMapper.updateById(plan);
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        pricePlanMapper.deleteById(id);
        return Response.success();
    }
}
