package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 编辑公告请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 编辑公告请求参数
 * @Version: 1.0.0
 */
@Data
public class AnnouncementUpdateReqDTO {

    /** 公告ID */
    @NotNull(message = "公告ID不能为空")
    private Long id;

    /** 所属课程ID */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /** 公告标题 */
    @NotBlank(message = "公告标题不能为空")
    @Size(max = 200, message = "公告标题长度不能超过200个字符")
    private String title;

    /** 长标题（展示详情时优先显示） */
    private String longTitle;

    /** 分类 */
    private String category;

    /** 简述 */
    private String summary;

    /** 来源 */
    private String source;

    /** 状态 1:启用 0:禁用 */
    private Integer status = 1;

    /** 公告内容 */
    @Size(max = 65535, message = "公告内容长度不能超过65535个字符")
    private String content;
}
