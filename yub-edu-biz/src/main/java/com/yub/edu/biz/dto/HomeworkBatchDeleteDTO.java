package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量删除作业题目请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-14
 * @Description: 批量删除冗余/误识别题目
 * @Version: 1.0.0
 */
@Data
public class HomeworkBatchDeleteDTO {

    /** 待删除的题目ID列表 */
    @NotEmpty(message = "题目ID列表不能为空")
    private List<Long> ids;
}
