package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分记录实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分记录实体
 * @Version: 1.0
 */
@Data
public class EduPointsRecord {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 变动积分（正数为获得，负数为消耗）
     */
    private Integer points;
    /**
     * 变动类型: 1 获得, 2 消耗
     */
    private Integer changeType;
    /**
     * 变动描述
     */
    private String description;
    /**
     * 关联业务ID
     */
    private String bizId;
    /**
     * 关联业务类型
     */
    private String bizType;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
