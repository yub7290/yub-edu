package com.yub.edu.biz.vo;

import com.yub.edu.biz.entity.EduQuestionOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 练习题目响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 练习模式的题目详情（不含答案）
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticeQuestionRespVO {

    /** 题目ID */
    private Long id;

    /** 试题类型 0:单选 1:多选 2:判断 3:简答 4:填空 */
    private Integer questionType;

    /** 题干（富文本） */
    private String content;

    /** 难度 1-5 */
    private Integer difficulty;

    /** 知识点 */
    private String knowledgePoints;

    /** 选项列表（单选/多选/填空用） */
    private List<EduQuestionOption> options;

    /** 是否已收藏 */
    private Boolean favorited;

    /** 当前题号（第几题） */
    private Integer currentIndex;

    /** 总题数 */
    private Integer totalCount;

    /** 练习模式 1章节 2错题 3收藏 4高频 5继续 */
    private Integer practiceMode;

    /** 课程ID */
    private Long courseId;

    /** 章节ID */
    private Long chapterId;

    /** 解析（富文本） */
    private String analysis;
}
