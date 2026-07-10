package com.yub.edu.biz.controller.app;

import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.api.dto.app.CoursePurchaseReqDTO;
import com.yub.edu.api.vo.app.CourseOrderVO;
import com.yub.edu.biz.entity.EduCourseOrder;
import com.yub.edu.biz.service.fund.CourseOrderService;
import com.yub.framework.security.SecurityUtils;
import com.yub.framework.util.BeanUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 移动端课程订单 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 移动端课程订单管理接口（购买、我的订单、订单详情）
 * @Version: 1.0
 */
@RestController
@RequestMapping("/app/course-order")
@RequiredArgsConstructor
public class AppCourseOrderController {

    private final CourseOrderService courseOrderService;

    /**
     * 余额购买课程
     *
     * @param dto 购买参数（课程ID）
     * @return 课程订单信息
     */
    @PostMapping("/purchase")
    public Response<CourseOrderVO> purchase(@Valid @RequestBody CoursePurchaseReqDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        // TODO: 查询课程信息获取名称和价格（需注入EduCourseMapper）
        EduCourseOrder order = courseOrderService.purchaseByBalance(userId, dto.getCourseId(), "课程", BigDecimal.ZERO);
        CourseOrderVO vo = BeanUtils.copy(order, CourseOrderVO.class);
        return Response.success(vo);
    }

    /**
     * 我的课程订单（分页）
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页课程订单
     */
    @PostMapping("/my")
    public Response<PageResult<CourseOrderVO>> myOrders(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = SecurityUtils.getCurrentUserId();
        return Response.success(courseOrderService.myOrders(userId, pageNum, pageSize));
    }

    /**
     * 订单详情
     *
     * @param orderNo 订单号
     * @return 订单详情
     */
    @GetMapping("/{orderNo}")
    public Response<CourseOrderVO> detail(@PathVariable("orderNo") String orderNo) {
        EduCourseOrder order = courseOrderService.getByOrderNo(orderNo);
        CourseOrderVO vo = BeanUtils.copy(order, CourseOrderVO.class);
        return Response.success(vo);
    }
}
