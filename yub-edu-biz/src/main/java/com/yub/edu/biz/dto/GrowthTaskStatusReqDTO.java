package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 成长档案任务状态请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 更新成长档案任务完成状态请求参数
 * @Version: 1.0.0
 */
@Data
public class GrowthTaskStatusReqDTO {
    /**
     * 是否完成
     */
    private Boolean completed;
}

