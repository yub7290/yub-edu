package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存清理结果 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 缓存清理结果
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheClearVO {

    /** 被清理的键数 */
    private Long cleared;

    /** 清理的前缀 */
    private String prefix;
}
