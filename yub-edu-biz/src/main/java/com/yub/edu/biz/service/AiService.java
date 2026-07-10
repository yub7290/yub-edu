package com.yub.edu.biz.service;

import dev.langchain4j.data.message.ChatMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * AI调用统一服务接口（基于LangChain4j）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 封装AI API调用，供对话和作业批改共用
 * @Version: 2.0.0
 */
public interface AiService {

    /**
     * 同步调用AI
     *
     * @param messages LangChain4j消息列表
     * @return AI回复文本
     */
    String completeSync(List<ChatMessage> messages);

    /**
     * 流式调用AI，逐token推送到SSE
     *
     * @param messages LangChain4j消息列表
     * @param emitter  SSE发射器
     * @return 完整回复文本
     */
    String completeStream(List<ChatMessage> messages, SseEmitter emitter);
}
