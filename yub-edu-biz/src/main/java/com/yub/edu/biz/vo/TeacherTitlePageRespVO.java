package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 教师职称分页响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师职称分页列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTitlePageRespVO {

    /** 职称ID */
    private Long id;

    /** 职称名称 */
    private String name;

    /** 教师数量（引用该职称的教师人数） */
    private Integer teacherCount;

    /** 课程数 */
    private Integer courseCount;

    /** 备注 */
    private String remark;

    /** 状态（1=启用 0=禁用） */
    private Integer status;
}
