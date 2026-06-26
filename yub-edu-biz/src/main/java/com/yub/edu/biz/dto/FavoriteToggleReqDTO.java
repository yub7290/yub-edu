package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 切换收藏请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 切换收藏请求参数
 * @Version: 1.0.0
 */
@Data
public class FavoriteToggleReqDTO {

    /** 课程ID */
    private Long courseId;

    /** 题目ID */
    private Long questionId;
}
