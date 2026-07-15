package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 一键复查请求 DTO
 * <p>仅对批改记录中「AI 判定正确」的题目批量标记为已复查，采用 AI 判定答案
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-14
 * @Description: 一键复查请求参数
 * @Version: 1.0.0
 */
@Data
public class HomeworkAutoReviewDTO {

    /** 批改记录ID */
    @NotNull(message = "批改记录ID不能为空")
    private Long correctionId;
}
