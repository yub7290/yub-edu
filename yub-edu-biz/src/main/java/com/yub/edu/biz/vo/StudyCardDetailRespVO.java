package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 学习卡详情响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡详情响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyCardDetailRespVO {

    /** 学习卡ID */
    private Long id;

    /** 学习卡标题 */
    private String title;

    /** 金额 */
    private BigDecimal amount;

    /** 是否可抵扣优惠券（1:是 0:否） */
    private Integer couponDeductible;

    /** 发行数量 */
    private Integer quantity;

    /** 状态（1:启用 0:禁用） */
    private Integer status;

    /** 有效开始日期 */
    private LocalDate validStartDate;

    /** 有效结束日期 */
    private LocalDate validEndDate;

    /** 学习时长 */
    private Integer studyDuration;

    /** 学习时长单位（月/天） */
    private String studyDurationUnit;

    /** 描述 */
    private String description;

    /** 卡号长度 */
    private Integer cardCodeLength;

    /** 密码长度 */
    private Integer secretCodeLength;

    /** 关联课程ID列表 */
    private List<Long> courseIds;

    /** 关联课程数 */
    private Integer courseCount;
}
