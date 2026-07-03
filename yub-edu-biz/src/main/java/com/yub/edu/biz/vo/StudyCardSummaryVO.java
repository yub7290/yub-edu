package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学习卡汇总信息VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: APP端学习卡汇总（总数、已使用数）
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyCardSummaryVO {

    /** 学习卡总数 */
    private Long totalCount;

    /** 已使用数量 */
    private Long usedCount;
}
