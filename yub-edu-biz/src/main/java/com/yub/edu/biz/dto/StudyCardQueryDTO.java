package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 学习卡查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡分页查询参数
 * @Version: 1.0.0
 */
@Data
public class StudyCardQueryDTO {

    /** 学习卡标题（模糊搜索） */
    private String title;

    /** 状态（1=启用 0=禁用） */
    private Integer status;
}
