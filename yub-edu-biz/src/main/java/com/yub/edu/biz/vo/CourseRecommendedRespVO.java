package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程推荐响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-22
 * @Description: 移动端课程推荐展示
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRecommendedRespVO {

    /** 课程ID */
    private Long id;

    /** 课程图片URL */
    private String imageUrl;

    /** 课程名称 */
    private String name;

    /** 课程类型 */
    private Integer courseType;

    /** 教师名称 */
    private String teacherName;

    /** 资费类型（免费/限免/试学） */
    private String feeType;

    /** 当前学员是否可学习该课程（免费课/已购/组绑定），不可访问时前端展示锁定态 */
    private Boolean accessible;
}
