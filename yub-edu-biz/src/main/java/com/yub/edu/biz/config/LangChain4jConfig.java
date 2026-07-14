package com.yub.edu.biz.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * LangChain4j 配置
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 手动创建ChatModel和StreamingChatModel Bean
 * @Version: 3.0.0
 */
@Slf4j
@Configuration
public class LangChain4jConfig {

    @Data
    @ConfigurationProperties(prefix = "ai")
    public static class AiProperties {
        private String apiKey = "123";
        private String baseUrl = "https://token-plan-cn.xiaomimimo.com/v1";
        private Integer maxTokens = 200000;
        private Integer timeout = 60;
    }

    @Bean
    public AiProperties aiProperties() {
        return new AiProperties();
    }

    @Bean
    public ChatModel chatModel(AiProperties properties) {
        log.info("Creating ChatModel with baseUrl: {}", properties.getBaseUrl());
        return OpenAiChatModel.builder()
                .apiKey(properties.getApiKey())
                .baseUrl(properties.getBaseUrl())
                .maxTokens(properties.getMaxTokens())
                .timeout(Duration.ofSeconds(properties.getTimeout()))
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel(AiProperties properties) {
        log.info("Creating StreamingChatModel with baseUrl: {}", properties.getBaseUrl());
        return OpenAiStreamingChatModel.builder()
                .apiKey(properties.getApiKey())
                .baseUrl(properties.getBaseUrl())
                .maxTokens(properties.getMaxTokens())
                .timeout(Duration.ofSeconds(properties.getTimeout()))
                .build();
    }
}