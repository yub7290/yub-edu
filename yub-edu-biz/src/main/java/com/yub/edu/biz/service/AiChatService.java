package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.AiChatReqDTO;
import com.yub.edu.biz.vo.AiChatRespVO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * AI助教服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: AI助教业务逻辑
 * @Version: 1.0.0
 */
public interface AiChatService {

    /**
     * 发送消息获取AI回复
     *
     * @param studentId 学生ID
     * @param req       请求参数
     * @return AI回复消息
     */
    AiChatRespVO chat(Long studentId, AiChatReqDTO req);

    /**
     * 上传媒体文件
     *
     * @param studentId 学生ID
     * @param file      文件
     * @param mediaType 媒体类型
     * @return 媒体URL
     */
    String uploadMedia(Long studentId, MultipartFile file, String mediaType);

    /**
     * 获取对话历史
     *
     * @param studentId 学生ID
     * @param courseId  课程ID
     * @param chatId    会话ID（可选）
     * @param limit     返回条数
     * @return 消息列表
     */
    List<AiChatRespVO> getHistory(Long studentId, Long courseId, Long chatId, Integer limit);

    /**
     * 流式对话（SSE）
     *
     * @param studentId 学生ID
     * @param req       请求参数
     * @param emitter   SSE 发射器
     */
    void chatStream(Long studentId, AiChatReqDTO req, SseEmitter emitter);
}
