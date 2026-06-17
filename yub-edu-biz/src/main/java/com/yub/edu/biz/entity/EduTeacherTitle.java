package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教师职称实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师职称实体
 * @Version: 1.0.0
 */
@Data
public class EduTeacherTitle {
    /**
     * 主键
     */
    private Long id;
    /**
     * 职称名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态（1=正常 0=禁用）
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 逻辑删除（0=正常 1=已删除）
     */
    private Integer deleted;
}
