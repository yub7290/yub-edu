package com.yub.edu.biz.vo;

import com.yub.edu.biz.entity.EduQuestionOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试题详情响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试题详情响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailRespVO {

    /** 试题ID */
    private Long id;

    /** 试题类型 0:单选 1:多选 2:判断 3:简答 4:填空 */
    private Integer questionType;

    /** 题干（富文本） */
    private String content;

    /** 难度 1-5 */
    private Integer difficulty;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 所属专业ID */
    private Long majorId;

    /** 所属专业名称 */
    private String majorName;

    /** 所属课程ID */
    private Long courseId;

    /** 所属课程名称 */
    private String courseName;

    /** 所属章节ID */
    private Long chapterId;

    /** 解析（富文本） */
    private String analysis;

    /** 知识点 */
    private String knowledgePoints;

    /** 关联知识点ID列表 */
    private List<Long> knowledgePointIds;

    /** 答案 */
    private String answer;

    /** 选项列表 */
    private List<EduQuestionOption> options;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
