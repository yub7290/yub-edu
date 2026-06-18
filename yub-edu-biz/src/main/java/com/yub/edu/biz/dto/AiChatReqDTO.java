package com.yub.edu.biz.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * AI助教对话请求DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: AI助教对话请求参数
 * @Version: 1.0.0
 */
@Data
public class AiChatReqDTO {

    /** 课程ID */
    private Long courseId;

    /** 会话ID（可选，为空则创建新会话） */
    private Long chatId;

    /** 消息内容 */
    private String message;

    /** 媒体类型：image-图片，video-视频 */
    private String mediaType;

    /** 媒体文件URL */
    private String mediaUrl;

    /** 媒体文件Base64编码 */
    private String mediaBase64;
}
