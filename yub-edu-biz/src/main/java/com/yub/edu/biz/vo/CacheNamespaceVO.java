package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 缓存命名空间 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 单个缓存命名空间的键数概览
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheNamespaceVO {

    /** 命名空间前缀（如 edu:*） */
    private String prefix;

    /** 该前缀下的键数 */
    private Long keyCount;

    /** 示例键（最多 20 条） */
    private List<String> sampleKeys;
}
