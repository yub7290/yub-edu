package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.api.vo.app.CourseOrderVO;
import com.yub.edu.biz.service.fund.CourseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 管理端课程订单 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 管理端课程订单管理接口（分页查询、退款）
 * @Version: 1.0
 */
@RestController
@RequestMapping("/edu/course-order")
@RequiredArgsConstructor
public class EduCourseOrderController {

    private final CourseOrderService courseOrderService;

    /**
     * 分页查询课程订单
     *
     * @param query 查询条件（含 orderNo、userName、courseName、status、startTime、endTime 及分页参数）
     * @return 分页课程订单
     */
    @PostMapping("/page")
    public Response<PageResult<CourseOrderVO>> page(@RequestBody PageQuery<Map<String, Object>> query) {
        Map<String, Object> params = query.getQueryParam();
        String orderNo = (String) params.get("orderNo");
        String userName = (String) params.get("userName");
        String courseName = (String) params.get("courseName");
        Integer status = params.get("status") != null ? Integer.valueOf(params.get("status").toString()) : null;
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        int pageNum = query.getPageParam().getPageNum();
        int pageSize = query.getPageParam().getPageSize();
        return Response.success(courseOrderService.allPage(orderNo, userName, courseName, status, startTime, endTime, pageNum, pageSize));
    }

    /**
     * 课程订单退款
     *
     * @param id 订单ID
     * @return 响应
     */
    @PostMapping("/{id}/refund")
    @Log(value = "课程订单退款", type = "UPDATE")
    public Response<Void> refund(@PathVariable("id") Long id) {
        courseOrderService.refund(id);
        return Response.success();
    }
}
