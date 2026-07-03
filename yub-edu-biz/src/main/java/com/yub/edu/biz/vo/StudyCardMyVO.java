package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 我的学习卡响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: APP端我的学习卡列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyCardMyVO {

    /** 实例ID */
    private Long id;

    /** 学习卡标题 */
    private String title;

    /** 卡号 */
    private String cardNo;

    /** 金额 */
    private BigDecimal amount;

    /** 状态（0:未使用 1:已使用 2:已回滚 3:已禁用） */
    private Integer status;

    /** 有效开始日期 */
    private LocalDate validStartDate;

    /** 有效结束日期 */
    private LocalDate validEndDate;

    /** 学习时长 */
    private Integer studyDuration;

    /** 学习时长单位（月/天） */
    private String studyDurationUnit;

    /** 关联课程名称列表 */
    private List<String> courseNames;

    /** 使用时间 */
    private LocalDateTime useTime;
}
