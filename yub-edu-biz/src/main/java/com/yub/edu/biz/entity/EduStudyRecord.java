package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学习记录实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学员学习记录实体
 * @Version: 1.0.0
 */
@Data
public class EduStudyRecord {

    /** 主键 */
    private Long id;

    /** 学员ID */
    private Long studentId;

    /** 课程ID */
    private Long courseId;

    /** 章节ID */
    private Long chapterId;

    /** 播放进度（秒） */
    private Integer playSecond;

    /** 累计学习时长（秒） */
    private Integer totalStudySecond;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
