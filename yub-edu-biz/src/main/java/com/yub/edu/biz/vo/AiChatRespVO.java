package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI助教消息响应VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: AI助教消息响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiChatRespVO {

    /** 消息ID */
    private Long id;

    /** 会话ID */
    private Long conversationId;

    /** 角色：user-学生，assistant-AI助教 */
    private String role;

    /** 消息内容 */
    private String content;

    /** 媒体类型：image-图片，video-视频 */
    private String mediaType;

    /** 媒体文件URL */
    private String mediaUrl;

    /** 创建时间 */
    private LocalDateTime createTime;
}
