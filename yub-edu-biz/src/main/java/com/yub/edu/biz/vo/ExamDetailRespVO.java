package com.yub.edu.biz.vo;

import com.yub.edu.biz.entity.EduExamChapterQuestionConfig;
import com.yub.edu.biz.entity.EduExamQuestionTypeConfig;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷详情响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试卷详情响应
 * @Version: 1.0.0
 */
@Data
public class ExamDetailRespVO {

    /** 主键 */
    private Long id;

    /** 试卷标题 */
    private String title;

    /** 副标题 */
    private String subtitle;

    /** 所属专业ID */
    private Long majorId;

    /** 所属课程ID */
    private Long courseId;

    /** 专业名称 */
    private String majorName;

    /** 课程名称 */
    private String courseName;

    /** 是否结课考试 1:是 0:否 */
    private Integer isFinalExam;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 推荐 1:是 0:否 */
    private Integer recommended;

    /** 难度 1-5 */
    private Integer difficulty;

    /** 考试时长（分钟） */
    private Integer duration;

    /** 满分 */
    private Integer totalScore;

    /** 及格分 */
    private Integer passScore;

    /** 简介（富文本） */
    private String introduction;

    /** 注意事项（富文本） */
    private String notes;

    /** 出卷人 */
    private String examiner;

    /** 试卷logo */
    private String logo;

    /** 出题范围 0:当前课程所有试题 1:按章节出题 */
    private Integer questionRangeType;

    /** 最大参考次数（0=不限） */
    private Integer maxAttempts;

    /** 章节完成率准入门槛（%，0=不校验） */
    private Integer chapterPassRate;

    /** 试题类型配置列表 */
    private List<EduExamQuestionTypeConfig> typeConfigs;

    /** 章节出题配置列表 */
    private List<EduExamChapterQuestionConfig> chapterConfigs;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
