package com.yub.edu.biz.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 用户资金账户实体
 * @Version 1.0
 */
@Data
public class EduFundAccount {
    /** 主键ID */
    private Long id;
    /** 用户ID */
    private Long userId;
    /** 可用余额 */
    private BigDecimal balance;
    /** 累计充值 */
    private BigDecimal totalRecharge;
    /** 累计消费 */
    private BigDecimal totalConsumption;
    /** 乐观锁版本号 */
    private Integer version;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 逻辑删除 0=正常 1=已删除 */
    private Integer deleted;
}
