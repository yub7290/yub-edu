package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 练习概览响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 课程练习概览（总题量/已练习/通过率）
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticeOverviewRespVO {

    /** 总题量 */
    private Integer totalQuestionCount;

    /** 已练习次数（含重做） */
    private Integer practicedCount;

    /** 总答题次数（含重做，用于计算通过率） */
    private Integer totalAttempts;

    /** 通过率（百分比） */
    private Integer passRate;

    /** 章节练习进度列表 */
    private List<ChapterPracticeProgressVO> chapterProgressList;
}
