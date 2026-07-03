package com.yub.edu.biz.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学习卡模板实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡模板实体
 * @Version: 1.0.0
 */
@Data
public class EduStudyCard {

    /** 主键 */
    private Long id;

    /** 主题 */
    private String title;

    /** 面额(元) */
    private BigDecimal amount;

    /** 可抵扣卡券 1:是 0:否 */
    private Integer couponDeductible;

    /** 生成数量 */
    private Integer quantity;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 有效期开始日期 */
    private LocalDate validStartDate;

    /** 有效期结束日期 */
    private LocalDate validEndDate;

    /** 学习时长数值 */
    private Integer studyDuration;

    /** 学习时长单位(月/天) */
    private String studyDurationUnit;

    /** 说明 */
    private String description;

    /** 学习码长度 */
    private Integer cardCodeLength;

    /** 密钥长度 */
    private Integer secretCodeLength;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人 */
    private Long createBy;

    /** 更新人 */
    private Long updateBy;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
