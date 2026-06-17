package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试题实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试题实体
 * @Version: 1.0.0
 */
@Data
public class EduQuestion {

    /** 主键 */
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

    /** 所属课程ID */
    private Long courseId;

    /** 所属章节ID */
    private Long chapterId;

    /** 解析（富文本） */
    private String analysis;

    /** 知识点（逗号分隔） */
    private String knowledgePoints;

    /** 答案（判断题存 true/false，简答题存文本） */
    private String answer;

    /** 选项列表（非持久化，仅用于传输） */
    private List<EduQuestionOption> options;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人 */
    private Long createBy;

    /** 更新人 */
    private Long updateBy;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
