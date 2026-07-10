package com.yub.edu.biz.service.fund;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageResult;
import com.yub.edu.api.vo.app.CourseOrderVO;
import com.yub.edu.biz.entity.EduCourseOrder;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduCourseOrderMapper;
import com.yub.framework.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 课程订单服务
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseOrderService {
    private final EduCourseOrderMapper courseOrderMapper;
    // TODO: 架构治理 - Service间耦合: CourseOrderService 依赖 FundService，应通过 Manager 层解耦
    private final FundService fundService;

    @Transactional(rollbackFor = Exception.class)
    public EduCourseOrder purchaseByBalance(Long userId, Long courseId, String courseName, BigDecimal coursePrice) {
        EduCourseOrder existing = courseOrderMapper.selectByUserAndCourse(userId, courseId);
        if (existing != null && existing.getStatus() == 1) throw new EduException(EduErrorCode.DUPLICATE_PAYMENT);
        String orderNo = "CO" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
        fundService.deduct(userId, coursePrice, orderNo, courseName);
        EduCourseOrder order = new EduCourseOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setCourseId(courseId);
        order.setCourseName(courseName);
        order.setAmount(coursePrice);
        order.setPaymentMethod("BALANCE");
        order.setStatus(1);
        order.setPaidAt(LocalDateTime.now());
        courseOrderMapper.insert(order);
        log.info("余额购买课程成功: orderNo={}, userId={}, courseId={}", orderNo, userId, courseId);
        return order;
    }

    public PageResult<CourseOrderVO> myOrders(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduCourseOrder> list = courseOrderMapper.selectMyOrders(userId);
        PageInfo<EduCourseOrder> pageInfo = new PageInfo<>(list);
        List<CourseOrderVO> records = BeanUtils.copyList(list, CourseOrderVO.class);
        return PageResult.of(records, pageInfo.getTotal());
    }

    public PageResult<CourseOrderVO> allPage(String orderNo, String userName, String courseName, Integer status, String startTime, String endTime, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduCourseOrder> list = courseOrderMapper.selectAllPage(orderNo, userName, courseName, status, startTime, endTime);
        PageInfo<EduCourseOrder> pageInfo = new PageInfo<>(list);
        List<CourseOrderVO> records = BeanUtils.copyList(list, CourseOrderVO.class);
        return PageResult.of(records, pageInfo.getTotal());
    }

    public EduCourseOrder getByOrderNo(String orderNo) {
        EduCourseOrder order = courseOrderMapper.selectByOrderNo(orderNo);
        if (order == null) throw new EduException(EduErrorCode.COURSE_ORDER_NOT_FOUND);
        return order;
    }

    @Transactional(rollbackFor = Exception.class)
    public void refund(Long id) {
        EduCourseOrder order = courseOrderMapper.selectById(id);
        if (order == null || order.getStatus() != 1) throw new EduException(EduErrorCode.COURSE_ORDER_NOT_FOUND);
        int rows = courseOrderMapper.updateToRefunded(id, order.getAmount());
        if (rows == 0) throw new EduException(EduErrorCode.REFUND_FAILED);
        fundService.addBalance(order.getUserId(), order.getAmount(), order.getOrderNo(), order.getCourseName() + " 退款");
        log.info("退款成功: id={}, amount={}", id, order.getAmount());
    }
}
