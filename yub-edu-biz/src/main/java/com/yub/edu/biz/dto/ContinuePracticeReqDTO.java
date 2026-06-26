package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 继续练习请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 继续练习请求参数
 * @Version: 1.0.0
 */
@Data
public class ContinuePracticeReqDTO {

    /** 课程ID */
    private Long courseId;

    /** 练习模式 1章节 2错题 3收藏 4高频 5继续 */
    private Integer practiceMode;

    /** 章节ID（按章节练习时必传） */
    private Long chapterId;
}
