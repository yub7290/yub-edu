package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学员组分页响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组分页列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroupPageRespVO {

    /** 学员组ID */
    private Long id;

    /** 学员组名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 学员人数 */
    private Integer memberCount;

    /** 课程数量 */
    private Integer courseCount;

    /** 创建时间 */
    private LocalDateTime createTime;
}
