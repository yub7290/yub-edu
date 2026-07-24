package com.yub.edu.biz.mapper;

import com.yub.edu.biz.vo.CourseAccessFlagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程访问权限 Mapper
 *
 * @Author: WorkBuddy
 * @CreateTime: 2026-07-24
 * @Description: 课程学习权限相关查询（已购 / 组绑定 / 批量可访问性）
 * @Version: 1.0.0
 */
@Mapper
public interface EduCourseAccessMapper {

    /**
     * 判断学员是否已支付购买该课程（status=1 且未删除）
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 计数（>0 表示已购）
     */
    int countPaidOrderByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 判断学员所属组是否绑定该课程
     * <p>兼容 edu_student_group_member 与 edu_student.group_id 两种归属来源
     *
     * @param studentId 学员ID
     * @param courseId  课程ID
     * @return 计数（>0 表示组已绑定）
     */
    int countGroupBoundCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /**
     * 批量判断课程对当前学员是否可访问
     * <p>免费课(is_free=1) 或 已支付订单 或 所属组绑定 任一满足即可
     *
     * @param studentId 学员ID
     * @param courseIds 课程ID集合
     * @return 课程可访问性列表
     */
    List<CourseAccessFlagVO> batchAccessible(@Param("studentId") Long studentId, @Param("courseIds") List<Long> courseIds);
}
