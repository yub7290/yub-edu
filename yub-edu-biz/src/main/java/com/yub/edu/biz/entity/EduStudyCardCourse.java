package com.yub.edu.biz.entity;

import lombok.Data;

/**
 * 学习卡关联课程实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡关联课程实体
 * @Version: 1.0.0
 */
@Data
public class EduStudyCardCourse {

    /** 主键 */
    private Long id;

    /** 学习卡模板ID */
    private Long cardId;

    /** 课程ID */
    private Long courseId;
}
