package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分账户实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分账户实体
 * @Version: 1.0
 */
@Data
public class EduPointsAccount {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 可用积分
     */
    private Integer availablePoints;
    /**
     * 累计获得积分
     */
    private Integer totalPoints;
    /**
     * 乐观锁版本号
     */
    private Integer version;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
