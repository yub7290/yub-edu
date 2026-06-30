package com.yub.edu.biz.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI助教配置实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 每个课程可配置独立的AI助教
 * @Version: 1.0.0
 */
@Data
public class EduAiConfig {

    /** 主键ID */
    private Long id;

    /** 课程ID */
    private Long courseId;

    /** 是否启用AI助教：0-禁用，1-启用 */
    private Integer enabled;

    /** 系统提示词（AI助教角色设定） */
    private String systemPrompt;

    /** 模型名称 */
    private String model;

    /** 每日对话次数限制 */
    private Integer dailyLimit;

    /** 温度参数 */
    private BigDecimal temperature;

    /** 最大Token数 */
    private Integer maxTokens;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
