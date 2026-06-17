package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 试题分页响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试题分页列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPageRespVO {

    /** 试题ID */
    private Long id;

    /** 试题类型 0:单选 1:多选 2:判断 3:简答 4:填空 */
    private Integer questionType;

    /** 题干（截取） */
    private String content;

    /** 难度 1-5 */
    private Integer difficulty;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 课程名称 */
    private String courseName;

    /** 创建时间 */
    private LocalDateTime createTime;
}
