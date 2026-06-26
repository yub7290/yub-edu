package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 考试列表响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端考试列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamListRespVO {

    /** 考试列表 */
    private List<ExamItemVO> list;

    /** 总记录数 */
    private Long total;

    /**
     * 考试列表单项 VO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExamItemVO {

        /** 试卷ID */
        private Long id;

        /** 试卷标题 */
        private String title;

        /** 副标题 */
        private String subtitle;

        /** 课程ID */
        private Long courseId;

        /** 课程名称 */
        private String courseName;

        /** 考试时长（分钟） */
        private Integer duration;

        /** 满分 */
        private Integer totalScore;

        /** 及格分 */
        private Integer passScore;

        /** 难度 1-5 */
        private Integer difficulty;

        /** 是否结课考试 1:是 0:否 */
        private Integer isFinalExam;
    }
}
