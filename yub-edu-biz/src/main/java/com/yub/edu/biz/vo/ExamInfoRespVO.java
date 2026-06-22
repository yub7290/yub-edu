package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamInfoRespVO {
    private Long id;
    private String name;
    private Integer duration;
    private Integer totalScore;
    private String startTime;
    private String endTime;
}
