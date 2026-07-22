package com.yub.edu.biz.service.fund;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageResult;
import com.yub.edu.api.vo.app.CourseOrderVO;
import com.yub.edu.biz.entity.EduCourseOrder;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduCourseOrderMapper;
import com.yub.edu.biz.mapper.EduStudentGroupMapper;
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
    private final EduStudentGroupMapper studentGroupMapper;
    // TODO: 架构治理 - Service间耦合: CourseOrderService 依赖 FundService，应通过 Manager 层解耦
    private final FundService fundService;

    @Transactional(rollbackFor = Exception.class)
    public EduCourseOrder purchaseByBalance(Long userId, Long courseId, String courseName, BigDecimal coursePrice) {
        EduCourseOrder existing = courseOrderMapper.selectByUserAndCourse(userId, courseId);
        if (existing != null && existing.getStatus() == 1) throw new EduException(EduErrorCode.DUPLICATE_PAYMENT);

        // 检查学员是否在某个启用的分组中且该分组关联了该课程，若命中则免费
        Integer freeCount = studentGroupMapper.countFreeCourseByStudentId(userId, courseId);
        boolean isGroupFree = freeCount != null && freeCount > 0;

        String orderNo = "CO" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
        BigDecimal actualAmount;
        String paymentMethod;

        if (isGroupFree) {
            // 学员组关联课程，免费
            actualAmount = BigDecimal.ZERO;
            paymentMethod = "GROUP_FREE";
            log.info("学员组免费购买课程: orderNo={}, userId={}, courseId={}", orderNo, userId, courseId);
        } else {
            // 正常余额支付
            actualAmount = coursePrice;
            paymentMethod = "BALANCE";
            fundService.deduct(userId, actualAmount, orderNo, courseName);
        }

        EduCourseOrder order = new EduCourseOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setCourseId(courseId);
        order.setCourseName(courseName);
        order.setAmount(actualAmount);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(1);
        order.setPaidAt(LocalDateTime.now());
        courseOrderMapper.insert(order);
        log.info("课程购买成功: orderNo={}, userId={}, courseId={}, amount={}, method={}",
                orderNo, userId, courseId, actualAmount, paymentMethod);
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
