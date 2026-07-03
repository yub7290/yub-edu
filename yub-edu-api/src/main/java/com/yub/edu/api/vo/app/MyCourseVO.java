package com.yub.edu.api.vo.app;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: APP-个人中心-我的课程
 * @Version: 1.0
 */
@Data
@Builder
public class MyCourseVO {

    /**
     * 课程id
     */
    private Long id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程图片
     */
    private String imageUrl;

    /**
     * 总课时
     */
    private Integer totalCourseTime;

    /**
     * 学习进度
     */
    private BigDecimal learnProgress;

    /**
     * 最近学习时间
     */
    private LocalDateTime recentLearnTime;

}
