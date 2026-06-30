package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 编辑试题请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 编辑试题请求参数
 * @Version: 1.0.0
 */
@Data
public class QuestionUpdateReqDTO {

    /** 试题ID */
    @NotNull(message = "试题ID不能为空")
    private Long id;

    /** 试题类型 0:单选 1:多选 2:判断 3:简答 4:填空 */
    @NotNull(message = "试题类型不能为空")
    private Integer questionType;

    /** 题干（富文本） */
    @NotBlank(message = "题干不能为空")
    private String content;

    /** 难度 1-5 */
    @NotNull(message = "难度不能为空")
    private Integer difficulty = 3;

    /** 状态 1:启用 0:禁用 */
    @NotNull(message = "状态不能为空")
    private Integer status = 1;

    /** 所属专业ID */
    private Long majorId;

    /** 所属课程ID */
    private Long courseId;

    /** 所属章节ID */
    private Long chapterId;

    /** 解析（富文本） */
    private String analysis;

    /** 知识点 */
    @Size(max = 500, message = "知识点长度不能超过500个字符")
    @Deprecated
    private String knowledgePoints;

    /** 关联知识点ID列表 */
    private List<Long> knowledgePointIds;

    /** 答案（判断题存 true/false，简答题存文本） */
    private String answer;

    /** 选项列表（单选/多选/填空用） */
    private List<QuestionCreateReqDTO.QuestionOptionDTO> options;
}
