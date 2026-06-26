package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 获取练习题目请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 获取练习题目请求参数
 * @Version: 1.0.0
 */
@Data
public class PracticeQuestionsReqDTO {

    /** 课程ID */
    private Long courseId;

    /** 章节ID */
    private Long chapterId;

    /** 练习模式 1章节 2错题 3收藏 4高频 5继续 */
    private Integer practiceMode;
}
