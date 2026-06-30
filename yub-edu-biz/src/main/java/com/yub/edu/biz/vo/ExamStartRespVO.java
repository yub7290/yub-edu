package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 开考返回 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-27
 * @Description: 开始考试时返回的记录ID和题目列表
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamStartRespVO {

    /** 考试记录ID */
    private Long recordId;

    /** 题目列表 */
    private List<ExamQuestionRespVO> questions;

    /** 考试时长（分钟） */
    private Integer duration;

    /** 服务端计算的结束时间 */
    private LocalDateTime endTime;
}
