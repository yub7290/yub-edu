package com.yub.edu.biz.vo;

import lombok.Data;

import java.util.List;

/**
 * 课程知识库 VO（学员端）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 课程知识点按章节分组返回
 * @Version: 1.0.0
 */
@Data
public class CourseKnowledgeRespVO {

    /** 课程ID */
    private Long courseId;

    /** 课程名称 */
    private String courseName;

    /** 按章节分组的知识点列表 */
    private List<KnowledgeGroupVO> groups;

    /** 未分配到章节的知识点列表 */
    private List<KnowledgeItemVO> unassigned;
}
