package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 继续练习会话响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 继续练习返回的会话和当前题目信息
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticeSessionRespVO {

    /** 当前题目ID */
    private Long questionId;

    /** 章节ID */
    private Long chapterId;

    /** 练习模式 1章节 2错题 3收藏 4高频 5继续 */
    private Integer practiceMode;

    /** 当前题号（从0开始） */
    private Integer currentIndex;

    /** 总题数 */
    private Integer totalCount;
}
