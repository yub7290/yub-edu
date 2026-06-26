package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 考试结果 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 提交考试后返回的判分结果
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultRespVO {

    /** 考试记录ID */
    private Long recordId;

    /** 实际得分 */
    private Integer score;

    /** 试卷满分 */
    private Integer totalScore;

    /** 及格分 */
    private Integer passScore;

    /** 是否及格 1:是 0:否 */
    private Integer isPass;

    /** 答对题数 */
    private Integer correctCount;

    /** 答错题数 */
    private Integer wrongCount;

    /** 未答题数 */
    private Integer unansweredCount;

    /** 总题数 */
    private Integer totalCount;

    /** 答题用时（秒） */
    private Integer duration;

    /** 每题作答结果 */
    private List<ExamQuestionResultRespVO> questionResults;
}
