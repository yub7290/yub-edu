package com.yub.edu.biz.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * LangChain4j 配置
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: LangChain4j 聊天模型配置
 * @Version: 1.0.0
 */
@Slf4j
@Configuration
public class LangChain4jConfig {

    @Value("${ai.api-key}")
    private String apiKey;

    @Value("${ai.base-url}")
    private String baseUrl;

    @Value("${ai.model}")
    private String model;

    @Value("${ai.max-tokens:2000}")
    private Integer maxTokens;

    @Value("${ai.timeout:60}")
    private Integer timeout;

    /**
     * 同步聊天模型 Bean（用于普通请求）
     */
    @Bean
    public ChatLanguageModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(model)
                .maxTokens(maxTokens)
                .timeout(Duration.ofSeconds(timeout))
                .build();
    }

    /**
     * 流式聊天模型 Bean（用于 SSE 流式请求）
     */
    @Bean
    public StreamingChatLanguageModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(model)
                .maxTokens(maxTokens)
                .timeout(Duration.ofSeconds(timeout))
                .build();
    }
}
