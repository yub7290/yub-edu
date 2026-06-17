package com.yub.edu.biz.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程价格方案实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 课程价格方案实体
 * @Version: 1.0.0
 */
@Data
public class EduCoursePricePlan {
    private Long id;
    private Long courseId;
    private Integer months;
    private BigDecimal price;
    private BigDecimal voucherAmount;
    private Integer status;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createBy;
    private Long updateBy;
    private Integer deleted;
}
