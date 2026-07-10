package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 缓存监控统计 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 缓存监控统计（总键数 + 各命名空间）
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheStatsVO {

    /** 缓存总键数 */
    private Long totalKeys;

    /** 各命名空间键数 */
    private List<CacheNamespaceVO> namespaces;
}
