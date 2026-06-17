package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教师职称统计 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-17
 * @Description: 职称关联的教师数量统计
 * @Version: 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTitleCountVO {

    /** 职称 ID */
    private Long titleId;

    /** 教师数量 */
    private Integer teacherCount;
}
