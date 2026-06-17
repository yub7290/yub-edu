package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 试题查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试题查询参数
 * @Version: 1.0.0
 */
@Data
public class QuestionQueryDTO {

    /** 试题类型 */
    private Integer questionType;

    /** 难度 */
    private Integer difficulty;

    /** 课程ID */
    private Long courseId;

    /** 状态 */
    private Integer status;
}
