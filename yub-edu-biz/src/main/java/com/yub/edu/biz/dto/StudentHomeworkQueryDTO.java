package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 学生端作业批改记录查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-13
 * @Description: 学生端批改记录查询条件
 * @Version: 1.0.0
 */
@Data
public class StudentHomeworkQueryDTO {

    /** 课程ID（必填） */
    @NotNull(message = "请先选择课程")
    private Long courseId;
}
