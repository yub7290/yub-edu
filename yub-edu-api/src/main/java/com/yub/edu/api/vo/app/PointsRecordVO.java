package com.yub.edu.api.vo.app;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分记录项
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分记录项
 * @Version: 1.0
 */
@Data
public class PointsRecordVO {
    /** 记录ID */
    private Long id;
    /** 变动积分 */
    private Integer points;
    /** 变动类型: 1 获得, 2 消耗 */
    private Integer changeType;
    /** 变动描述 */
    private String description;
    /** 创建时间 */
    private LocalDateTime createTime;
}
