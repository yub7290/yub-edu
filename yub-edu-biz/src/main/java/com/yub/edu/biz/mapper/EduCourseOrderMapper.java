package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduCourseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 课程订单 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 课程订单数据访问
 * @Version: 1.0
 */
@Mapper
public interface EduCourseOrderMapper {

    /**
     * 插入课程订单
     *
     * @param order 课程订单实体
     * @return 影响行数
     */
    int insert(EduCourseOrder order);

    /**
     * 根据订单号查询
     *
     * @param orderNo 平台订单号
     * @return 课程订单实体
     */
    EduCourseOrder selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 课程订单实体
     */
    EduCourseOrder selectById(@Param("id") Long id);

    /**
     * 根据用户ID和课程ID查询订单
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 课程订单实体
     */
    EduCourseOrder selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 统计用户已支付（status=1 且未删除）的指定课程订单数
     * <p>用于课程访问权限判定；退款(status=2)等不计入
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 计数
     */
    int countPaidOrderByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 查询用户本人的课程订单
     *
     * @param userId 用户ID
     * @return 课程订单列表
     */
    List<EduCourseOrder> selectMyOrders(@Param("userId") Long userId);

    /**
     * 分页查询全部课程订单(管理员)
     *
     * @param orderNo    订单号(可选模糊查询)
     * @param userName   用户名(可选模糊查询)
     * @param courseName 课程名称(可选模糊查询)
     * @param status     状态(可选过滤)
     * @param startTime  开始时间(可选)
     * @param endTime    结束时间(可选)
     * @return 课程订单列表
     */
    List<EduCourseOrder> selectAllPage(@Param("orderNo") String orderNo,
            @Param("userName") String userName,
            @Param("courseName") String courseName,
            @Param("status") Integer status,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);

    /**
     * 更新订单状态为已退款
     *
     * @param id           主键ID
     * @param refundAmount 退款金额
     * @return 影响行数
     */
    int updateToRefunded(@Param("id") Long id, @Param("refundAmount") BigDecimal refundAmount);
}
