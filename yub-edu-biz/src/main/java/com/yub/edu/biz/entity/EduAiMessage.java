package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI助教消息实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: AI助教消息实体
 * @Version: 1.0.0
 */
@Data
public class EduAiMessage {

    /** 消息ID */
    private Long id;

    /** 会话ID */
    private Long conversationId;

    /** 学生ID */
    private Long studentId;

    /** 课程ID */
    private Long courseId;

    /** 角色：user-学生，assistant-AI助教 */
    private String role;

    /** 消息内容 */
    private String content;

    /** 媒体类型：image-图片，video-视频 */
    private String mediaType;

    /** 媒体文件URL */
    private String mediaUrl;

    /** 媒体文件Base64编码 */
    private String mediaBase64;

    /** Token消耗量 */
    private Integer tokenCount;

    /** 状态：1-正常，0-失败 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;
}
