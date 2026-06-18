package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教师选项 VO（下拉框使用）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 教师选项响应参数
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherOptionVO {

    /** 教师ID */
    private Long id;

    /** 教师姓名 */
    private String name;
}
