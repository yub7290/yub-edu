package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 练习题目简略 VO（列表页用，不含答案）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 错题/收藏/笔记列表中的题目简略信息
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticeQuestionSimpleVO {

    /** 题目ID */
    private Long id;

    /** 试题类型 0:单选 1:多选 2:判断 3:简答 4:填空 */
    private Integer questionType;

    /** 题干 */
    private String content;

    /** 难度 1-5 */
    private Integer difficulty;

    /** 课程ID */
    private Long courseId;

    /** 错题次数（仅错题列表用） */
    private Integer wrongCount;

    /** 章节名称 */
    private String chapterName;

    /** 关联时间（收藏时间/笔记时间/最后答错时间） */
    private LocalDateTime relatedTime;
}
