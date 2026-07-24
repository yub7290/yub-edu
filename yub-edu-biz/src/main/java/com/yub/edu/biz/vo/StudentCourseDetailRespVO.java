package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseDetailRespVO {
    private Object course;
    private List<?> chapter;
    private Object teacher;
    private Object aiAssistant;
    /** 当前学员是否可学习该课程（免费课/已购/组绑定） */
    private Boolean accessible;
}
