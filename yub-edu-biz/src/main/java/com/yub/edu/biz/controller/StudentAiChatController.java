package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.dto.AiChatReqDTO;
import com.yub.edu.biz.service.AiChatService;
import com.yub.edu.biz.vo.AiChatRespVO;
import com.yub.framework.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * 学生端AI助教
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端AI助教接口
 * @Version: 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/student/ai")
@RequiredArgsConstructor
public class StudentAiChatController {

    private final AiChatService aiChatService;

    /**
     * 发送消息获取AI回复（同步）
     *
     * @param req 请求参数
     * @return AI回复消息
     */
    @PostMapping("/chat")
    public Response<AiChatRespVO> chat(@RequestBody @Valid AiChatReqDTO req) {
        Long studentId = SecurityUtils.getCurrentUserId();
        AiChatRespVO resp = aiChatService.chat(studentId, req);
        return Response.success(resp);
    }

    /**
     * 流式对话（SSE）
     *
     * @param req 请求参数
     * @return SSE 流
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(@RequestBody @Valid AiChatReqDTO req) {
        SseEmitter emitter = new SseEmitter(120000L);
        emitter.onTimeout(() -> {
            log.warn("SSE timeout for chatId={}", req.getChatId());
            emitter.complete();
        });
        Long studentId = SecurityUtils.getCurrentUserId();
        aiChatService.chatStream(studentId, req, emitter);
        return emitter;
    }

    /**
     * 获取对话历史
     *
     * @param courseId 课程ID
     * @param chatId  会话ID（可选）
     * @param limit   返回条数（可选，默认50）
     * @return 消息列表
     */
    @GetMapping("/history")
    public Response<List<AiChatRespVO>> history(
            @RequestParam Long courseId,
            @RequestParam(required = false) Long chatId,
            @RequestParam(required = false) Integer limit) {
        Long studentId = SecurityUtils.getCurrentUserId();
        List<AiChatRespVO> list = aiChatService.getHistory(studentId, courseId, chatId, limit);
        return Response.success(list);
    }

    /**
     * 上传媒体文件
     *
     * @param file      文件
     * @param mediaType 媒体类型：image-图片，video-视频
     * @return 媒体URL
     */
    @PostMapping("/upload")
    public Response<String> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("mediaType") String mediaType) {
        Long studentId = SecurityUtils.getCurrentUserId();
        String mediaUrl = aiChatService.uploadMedia(studentId, file, mediaType);
        return Response.success(mediaUrl);
    }
}
