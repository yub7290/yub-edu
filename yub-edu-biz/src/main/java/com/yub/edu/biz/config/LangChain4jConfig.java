package com.yub.edu.biz.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * LangChain4j 配置
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 使用spring-boot-starter自动配置，无需手动定义Bean
 * @Version: 2.0.0
 */
@Slf4j
@Configuration
public class LangChain4jConfig {
    // langchain4j-open-ai-spring-boot-starter 自动创建 ChatModel 和 StreamingChatModel Bean
    // 配置项通过 application.yml 的 langchain4j.open-ai.* 读取
}
