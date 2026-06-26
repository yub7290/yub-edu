package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏切换响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 收藏状态切换结果
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteToggleRespVO {

    /** 是否已收藏（true:已收藏 false:已取消） */
    private Boolean favorited;
}
