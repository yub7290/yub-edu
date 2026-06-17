package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专业统计数量 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-17
 * @Description: 专业关联的课程/试题/试卷统计数据
 * @Version: 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EduMajorCountVO {

    /** 专业 ID */
    private Long majorId;

    /** 课程数量 */
    private Integer courseCount;

    /** 试题数量 */
    private Integer questionCount;

    /** 试卷数量 */
    private Integer examCount;
}
