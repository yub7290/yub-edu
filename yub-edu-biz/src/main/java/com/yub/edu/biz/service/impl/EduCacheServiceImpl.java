package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.service.EduCacheService;
import com.yub.edu.biz.vo.CacheClearVO;
import com.yub.edu.biz.vo.CacheNamespaceVO;
import com.yub.edu.biz.vo.CacheStatsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存管理服务实现（基于 RedissonClient）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 缓存监控与按前缀清理（不做 flushdb，避免误清全局）
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduCacheServiceImpl implements EduCacheService {

    /** 受监控/可清理的缓存命名空间前缀 */
    private static final List<String> MONITORED_PREFIXES = List.of(
            "login:*", "captcha:*", "edu:*", "sys:*",
            "student:*", "app:*", "ai:*", "course:*",
            "points:*", "fund:*", "study:*"
    );

    private static final int SAMPLE_LIMIT = 20;

    private final RedissonClient redissonClient;

    @Override
    public CacheStatsVO stats() {
        long total = 0;
        try {
            for (@SuppressWarnings("unused") String ignored : redissonClient.getKeys().getKeys()) {
                total++;
            }
        } catch (Exception e) {
            log.warn("统计缓存总键数失败", e);
        }

        List<CacheNamespaceVO> namespaces = new ArrayList<>();
        for (String prefix : MONITORED_PREFIXES) {
            namespaces.add(buildNamespace(prefix));
        }
        return CacheStatsVO.builder().totalKeys(total).namespaces(namespaces).build();
    }

    @Override
    public CacheClearVO clearByPrefix(String prefix) {
        validatePrefix(prefix);
        long cleared = safeDelete(prefix);
        return CacheClearVO.builder().cleared(cleared).prefix(prefix).build();
    }

    @Override
    public CacheClearVO clearAll() {
        long total = 0;
        for (String prefix : MONITORED_PREFIXES) {
            total += safeDelete(prefix);
        }
        return CacheClearVO.builder().cleared(total).prefix("*（受监控命名空间）").build();
    }

    private CacheNamespaceVO buildNamespace(String prefix) {
        long count = 0;
        List<String> samples = new ArrayList<>();
        try {
            for (String key : redissonClient.getKeys().getKeysByPattern(prefix)) {
                count++;
                if (samples.size() < SAMPLE_LIMIT) {
                    samples.add(key);
                }
            }
        } catch (Exception e) {
            log.warn("统计命名空间 {} 失败", prefix, e);
        }
        return CacheNamespaceVO.builder().prefix(prefix).keyCount(count).sampleKeys(samples).build();
    }

    private long safeDelete(String pattern) {
        try {
            return redissonClient.getKeys().deleteByPattern(pattern);
        } catch (Exception e) {
            log.warn("清理缓存 {} 失败", pattern, e);
            return 0L;
        }
    }

    private void validatePrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new EduException(EduErrorCode.PARAM_INVALID);
        }
        // 禁止直接清库：必须带通配符且不是裸 "*"
        if (!prefix.contains("*") || "*".equals(prefix.trim())) {
            throw new EduException(EduErrorCode.PARAM_INVALID);
        }
    }
}
