package com.yub.edu.biz.service;

import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.mapper.EduCourseAccessMapper;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduCourseOrderMapper;
import com.yub.edu.biz.vo.CourseAccessFlagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程学习权限判定服务
 *
 * @Author: WorkBuddy
 * @CreateTime: 2026-07-24
 * @Description: 统一判定学员是否可学习某课程：免费课 / 已支付订单 / 所属组绑定课程 任一满足即可
 * @Version: 1.0.0
 */
@Service
@RequiredArgsConstructor
public class CourseAccessService {

    private final EduCourseMapper eduCourseMapper;
    private final EduCourseOrderMapper eduCourseOrderMapper;
    private final EduCourseAccessMapper eduCourseAccessMapper;

    /**
     * 判断学员是否可学习某课程。
     * <p>规则（依确认口径 D2/D4）：
     * <ol>
     *   <li>课程 is_free=1（免费课）→ 全员可学；</li>
     *   <li>存在该学员对课程的已支付订单（status=1，退款 status=2 不计入）→ 已购买；</li>
     *   <li>学员所属组（edu_student_group_member UNION edu_student.group_id）绑定了该课程 → 组授权。</li>
     * </ol>
     *
     * @param studentId 学员ID（可为 null，表示未登录 → 无权限）
     * @param courseId  课程ID
     * @return 是否可访问
     */
    public boolean canAccess(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return false;
        }
        EduCourse course = eduCourseMapper.selectById(courseId);
        if (course == null) {
            return false;
        }
        if (course.getIsFree() != null && course.getIsFree() == 1) {
            return true;
        }
        if (eduCourseOrderMapper.countPaidOrderByUserAndCourse(studentId, courseId) > 0) {
            return true;
        }
        return eduCourseAccessMapper.countGroupBoundCourse(studentId, courseId) > 0;
    }

    /**
     * 批量判断课程可访问性，避免列表场景逐条 canAccess 造成 N+1。
     *
     * @param studentId 学员ID
     * @param courseIds 课程ID集合
     * @return 课程ID → 是否可访问
     */
    public Map<Long, Boolean> batchAccessible(Long studentId, List<Long> courseIds) {
        Map<Long, Boolean> result = new HashMap<>();
        if (studentId == null || courseIds == null || courseIds.isEmpty()) {
            return result;
        }
        List<CourseAccessFlagVO> flags = eduCourseAccessMapper.batchAccessible(studentId, courseIds);
        for (CourseAccessFlagVO flag : flags) {
            result.put(flag.getCourseId(), Boolean.TRUE.equals(flag.getAccessible()));
        }
        // 兜底：SQL 未返回的课程默认无权限
        for (Long id : courseIds) {
            result.putIfAbsent(id, false);
        }
        return result;
    }
}
