package com.yub.edu.biz.dto;

import lombok.Data;

/** 当前学员的考试作答状态，不代表试卷启停状态。 */
@Data
public class ExamAttemptStatusDTO {
    private Long examId;
    private Integer attemptStatus;
}
