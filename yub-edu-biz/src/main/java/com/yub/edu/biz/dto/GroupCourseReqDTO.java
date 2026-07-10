package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 学员组课程请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组课程操作请求参数
 * @Version: 1.0.0
 */
@Data
public class GroupCourseReqDTO {

    /** 课程ID */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /** 排序 */
    private Integer sortOrder = 0;
}
