package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节更新请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 更新章节请求参数
 * @Version: 1.0.0
 */
@Data
public class ChapterUpdateReqDTO {

    /** 章节ID */
    @NotNull(message = "章节ID不能为空")
    private Long id;

    /** 父章节ID */
    private Long parentId = 0L;

    /** 章节名称 */
    @NotBlank(message = "章节名称不能为空")
    private String name;

    /** 状态 */
    private Integer status = 1;

    /** 是否完成 */
    private Integer isCompleted = 0;

    /** 是否免费 */
    private Integer isFree = 0;

    /** 内容文本 */
    private String contentText;

    /** 是否直播 */
    private Integer isLive = 0;

    /** 直播开始时间 */
    private LocalDateTime liveStartTime;

    /** 直播时长(分钟) */
    private Integer liveDuration;

    /** 排序 */
    private Integer sort = 0;
}
