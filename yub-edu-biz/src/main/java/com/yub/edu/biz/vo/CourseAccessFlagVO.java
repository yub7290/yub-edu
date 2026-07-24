package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程可访问性标志（批量查询返回）
 *
 * @Author: WorkBuddy
 * @CreateTime: 2026-07-24
 * @Description: 课程权限批量判定结果
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAccessFlagVO {

    /** 课程ID */
    private Long courseId;

    /** 当前学员是否可访问（免费课 / 已购买 / 所属组已绑定 任一满足） */
    private Boolean accessible;
}
