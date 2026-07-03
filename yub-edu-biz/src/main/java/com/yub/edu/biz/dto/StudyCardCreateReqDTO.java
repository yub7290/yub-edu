package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 新增学习卡请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 新增学习卡请求参数
 * @Version: 1.0.0
 */
@Data
public class StudyCardCreateReqDTO {

    /** 学习卡标题 */
    @NotBlank(message = "学习卡标题不能为空")
    private String title;

    /** 金额 */
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    /** 是否可抵扣优惠券（1:是 0:否） */
    private Integer couponDeductible;

    /** 发行数量 */
    @NotNull(message = "发行数量不能为空")
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
}
