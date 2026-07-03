package com.yub.edu.api.vo.app;

import lombok.Data;

/**
 * 积分账户信息
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分账户信息
 * @Version: 1.0
 */
@Data
public class PointsAccountVO {
    /** 当前可用积分 */
    private Integer availablePoints;
    /** 累计获得积分 */
    private Integer totalPoints;
}
