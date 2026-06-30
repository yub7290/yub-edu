package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 编辑试卷请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 编辑试卷请求参数
 * @Version: 1.0.0
 */
@Data
public class ExamUpdateReqDTO {

    /** 试卷ID */
    @NotNull(message = "试卷ID不能为空")
    private Long id;

    /** 试卷标题 */
    @NotBlank(message = "试卷标题不能为空")
    @Size(max = 200, message = "试卷标题长度不能超过200个字符")
    private String title;

    /** 副标题 */
    private String subtitle;

    /** 所属专业ID */
    private Long majorId;

    /** 所属课程ID */
    @NotNull(message = "所属课程不能为空")
    private Long courseId;

    /** 是否结课考试 1:是 0:否 */
    private Integer isFinalExam = 0;

    /** 状态 1:启用 0:禁用 */
    private Integer status = 1;

    /** 推荐 1:是 0:否 */
    private Integer recommended = 0;

    /** 难度 1-5 */
    private Integer difficulty = 3;

    /** 考试时长（分钟） */
    private Integer duration = 60;

    /** 满分 */
    private Integer totalScore = 100;

    /** 及格分 */
    private Integer passScore = 60;

    /** 简介（富文本） */
    private String introduction;

    /** 注意事项（富文本） */
    private String notes;

    /** 出卷人 */
    private String examiner;

    /** 试卷logo */
    private String logo;

    /** 出题范围 0:当前课程所有试题 1:按章节出题 */
    private Integer questionRangeType = 0;

    /** 最大参考次数（0=不限） */
    private Integer maxAttempts = 0;

    /** 章节完成率准入门槛（%，0=不校验） */
    private Integer chapterPassRate = 0;

    /** 试题类型配置列表 */
    private List<ExamCreateReqDTO.ExamQuestionTypeConfigItem> typeConfigs;

    /** 章节出题配置列表 */
    private List<ExamCreateReqDTO.ExamChapterQuestionConfigItem> chapterConfigs;
}
