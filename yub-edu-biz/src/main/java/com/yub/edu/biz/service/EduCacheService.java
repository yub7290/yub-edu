package com.yub.edu.biz.service;

import com.yub.edu.biz.vo.CacheClearVO;
import com.yub.edu.biz.vo.CacheNamespaceVO;
import com.yub.edu.biz.vo.CacheStatsVO;

import java.util.List;

/**
 * 缓存管理服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 基于 RedissonClient 的缓存监控与清理
 * @Version: 1.0.0
 */
public interface EduCacheService {

    /**
     * 缓存监控统计（总键数 + 各命名空间概览）
     */
    CacheStatsVO stats();

    /**
     * 按前缀清理缓存（删除匹配该前缀的所有键）
     *
     * @param prefix 缓存键前缀（需含通配符 *）
     * @return 清理结果
     */
    CacheClearVO clearByPrefix(String prefix);

    /**
     * 清空全部受监控的业务缓存（不清空整个 Redis，仅清理已知命名空间）
     */
    CacheClearVO clearAll();
}
