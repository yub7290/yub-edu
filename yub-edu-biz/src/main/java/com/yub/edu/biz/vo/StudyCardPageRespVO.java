package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学习卡分页响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡分页列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyCardPageRespVO {

    /**
     * 学习卡ID
     */
    private Long id;

    /**
     * 学习卡标题
     */
    private String title;

    /**
     * 关联课程数
     */
    private Integer courseCount;

    /**
     * 已使用数量
     */
    private Integer usedCount;

    /**
     * 发行数量
     */
    private Integer quantity;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 有效开始日期
     */
    private LocalDate validStartDate;

    /**
     * 有效结束日期
     */
    private LocalDate validEndDate;

    /**
     * 学习时长
     */
    private Integer studyDuration;

    /**
     * 学习时长单位（月/天）
     */
    private String studyDurationUnit;

    /**
     * 状态（1:启用 0:禁用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
