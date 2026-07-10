package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.service.AiService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * AI调用统一服务实现（基于LangChain4j 1.3.x）
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 封装AI API调用，供对话和作业批改共用
 * @Version: 3.0.0
 */
@Slf4j
@Service
public class AiServiceImpl implements AiService {

    private final ChatModel chatModel;
    private final StreamingChatModel streamingChatModel;

    public AiServiceImpl(ChatModel chatModel, StreamingChatModel streamingChatModel) {
        this.chatModel = chatModel;
        this.streamingChatModel = streamingChatModel;
    }

    @Override
    public String completeSync(List<ChatMessage> messages) {
        try {
            log.info("LangChain4j同步调用，消息数量: {}", messages.size());
            ChatResponse response = chatModel.chat(messages);
            String text = response.aiMessage().text();
            log.info("AI回复长度: {}", text.length());
            return text;
        } catch (Exception e) {
            log.error("AI同步调用失败: {}", e.getMessage(), e);
            throw new EduException(EduErrorCode.AI_SERVICE_ERROR);
        }
    }

    @Override
    public String completeStream(List<ChatMessage> messages, SseEmitter emitter) {
        StringBuilder fullResponse = new StringBuilder();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> errorRef = new AtomicReference<>();

        log.info("LangChain4j流式调用，消息数量: {}", messages.size());

        streamingChatModel.chat(messages, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String token) {
                fullResponse.append(token);
                try {
                    emitter.send(SseEmitter.event().name("token").data(token));
                } catch (Exception e) {
                    log.warn("SSE token发送失败: {}", e.getMessage());
                }
            }

            @Override
            public void onCompleteResponse(ChatResponse response) {
                log.info("AI流式调用完成，回复长度: {}", fullResponse.length());
                try {
                    emitter.send(SseEmitter.event().name("done").data("[DONE]"));
                } catch (Exception e) {
                    log.warn("SSE done发送失败: {}", e.getMessage());
                }
                latch.countDown();
            }

            @Override
            public void onError(Throwable error) {
                log.error("AI流式调用错误: {}", error.getMessage(), error);
                errorRef.set(error);
                try {
                    emitter.send(SseEmitter.event().name("error").data("AI服务错误: " + error.getMessage()));
                } catch (Exception e) {
                    log.warn("SSE error发送失败: {}", e.getMessage());
                }
                latch.countDown();
            }
        });

        try {
            latch.await(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EduException(EduErrorCode.AI_SERVICE_ERROR);
        }

        if (errorRef.get() != null) {
            throw new EduException(EduErrorCode.AI_SERVICE_ERROR);
        }

        return fullResponse.toString();
    }
}
