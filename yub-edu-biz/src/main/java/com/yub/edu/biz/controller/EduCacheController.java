package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.service.EduCacheService;
import com.yub.edu.biz.vo.CacheClearVO;
import com.yub.edu.biz.vo.CacheStatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 缓存管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 缓存监控与按前缀清理（基于 RedissonClient）
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/cache")
@RequiredArgsConstructor
public class EduCacheController {

    private final EduCacheService eduCacheService;

    /**
     * 缓存监控统计
     */
    @GetMapping("/stats")
    public Response<CacheStatsVO> stats() {
        return Response.success(eduCacheService.stats());
    }

    /**
     * 按前缀清理缓存
     *
     * @param prefix 缓存键前缀（需含通配符 *）
     */
    @Log(value = "清理缓存", type = "DELETE")
    @DeleteMapping("/clear")
    public Response<CacheClearVO> clearByPrefix(@RequestParam("prefix") String prefix) {
        return Response.success(eduCacheService.clearByPrefix(prefix));
    }

    /**
     * 清空全部受监控的业务缓存（不清空整个 Redis）
     */
    @Log(value = "清空业务缓存", type = "DELETE")
    @DeleteMapping("/clearAll")
    public Response<CacheClearVO> clearAll() {
        return Response.success(eduCacheService.clearAll());
    }
}
