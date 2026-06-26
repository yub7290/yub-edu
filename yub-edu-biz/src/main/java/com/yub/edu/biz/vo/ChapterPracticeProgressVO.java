package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 章节练习进度 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 章节练习进度（已做/总题数 + 正确率）
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterPracticeProgressVO {

    /** 章节ID */
    private Long chapterId;

    /** 章节名称 */
    private String chapterName;

    /** 已练习不同题数 */
    private Integer practicedQuestionCount;

    /** 章节总题数 */
    private Integer totalQuestionCount;

    /** 正确率（百分比） */
    private Integer accuracyRate;

    /** 正确数（仅内部聚合用，不暴露给前端） */
    private Integer correctCount;

    /** 总答题数（仅内部聚合用，不暴露给前端） */
    private Integer totalAttempts;

    /** 子章节列表 */
    private List<ChapterPracticeProgressVO> children;
}
