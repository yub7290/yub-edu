package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.PricePlanCreateReqDTO;
import com.yub.edu.biz.dto.PricePlanUpdateReqDTO;
import com.yub.edu.biz.entity.EduCoursePricePlan;
import com.yub.edu.biz.mapper.EduCoursePricePlanMapper;
import com.yub.framework.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程价格方案 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 课程价格方案管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/course/price-plan")
@RequiredArgsConstructor
public class EduCoursePricePlanController {

    private final EduCoursePricePlanMapper pricePlanMapper;

    /**
     * 查询课程的价格方案列表
     *
     * @param courseId 课程ID
     * @return 价格方案列表
     */
    @GetMapping("/list/{courseId}")
    public Response<List<EduCoursePricePlan>> list(@PathVariable Long courseId) {
        return Response.success(pricePlanMapper.selectByCourseId(courseId));
    }

    /**
     * 新增价格方案
     *
     * @param dto 创建请求参数
     * @return 价格方案ID
     */
    @Log(value = "新增价格方案", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody PricePlanCreateReqDTO dto) {
        EduCoursePricePlan plan = new EduCoursePricePlan();
        BeanUtils.copyProperties(dto, plan);
        Long userId = SecurityUtils.getCurrentUserId();
        plan.setCreateBy(userId);
        plan.setUpdateBy(userId);
        if (plan.getSort() == null) plan.setSort(0);
        pricePlanMapper.insert(plan);
        return Response.success(plan.getId());
    }

    /**
     * 更新价格方案
     *
     * @param dto 更新请求参数
     * @return 操作结果
     */
    @Log(value = "编辑价格方案", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody PricePlanUpdateReqDTO dto) {
        EduCoursePricePlan exist = pricePlanMapper.selectById(dto.getId());
        if (exist == null) {
            return Response.error(404, "价格方案不存在");
        }
        EduCoursePricePlan plan = new EduCoursePricePlan();
        BeanUtils.copyProperties(dto, plan);
        plan.setUpdateBy(SecurityUtils.getCurrentUserId());
        pricePlanMapper.updateById(plan);
        return Response.success();
    }

    /**
     * 删除价格方案
     *
     * @param id 价格方案ID
     * @return 操作结果
     */
    @Log(value = "删除价格方案", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        pricePlanMapper.deleteById(id);
        return Response.success();
    }
}
