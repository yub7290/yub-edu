package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增通知请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新增通知请求参数
 * @Version: 1.0.0
 */
@Data
public class NoticeCreateReqDTO {

    /** 关联课程ID（通知仅发给绑定该课程的学员） */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /** 通知标题 */
    @NotBlank(message = "通知标题不能为空")
    @Size(max = 200, message = "通知标题长度不能超过200个字符")
    private String title;

    /** 通知内容 */
    @Size(max = 65535, message = "通知内容长度不能超过65535个字符")
    private String content;

    /** 类型 1:系统通知 2:课程相关 3:考试相关 4:活动 */
    private Integer type = 1;

    /** 状态 0:草稿 1:已发布 */
    private Integer status = 1;
}
