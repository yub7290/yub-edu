package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.AiChatReqDTO;
import com.yub.edu.biz.entity.EduAiConversation;
import com.yub.edu.biz.entity.EduAiMessage;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduAiConversationMapper;
import com.yub.edu.biz.mapper.EduAiMessageMapper;
import com.yub.edu.biz.service.AiChatService;
import com.yub.edu.biz.vo.AiChatRespVO;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * AI助教服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: AI助教业务逻辑实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final EduAiConversationMapper conversationMapper;
    private final EduAiMessageMapper messageMapper;
    private final ChatLanguageModel chatModel;
    private final RestTemplate restTemplate;

    /** 线程池用于异步流式处理 */
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /** 消息历史缓存：conversationId -> messages */
    private final ConcurrentHashMap<Long, List<ChatMessage>> chatMemory = new ConcurrentHashMap<>();

    /** 历史消息默认返回条数 */
    private static final int DEFAULT_HISTORY_LIMIT = 50;
    /** 上下文窗口大小 */
    private static final int MAX_CONTEXT_MESSAGES = 20;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiChatRespVO chat(Long studentId, AiChatReqDTO req) {
        Long courseId = req.getCourseId();

        // 1. 获取或创建会话
        Long conversationId = req.getChatId();
        if (conversationId == null) {
            EduAiConversation conversation = new EduAiConversation();
            conversation.setStudentId(studentId);
            conversation.setCourseId(courseId);
            conversation.setTitle(req.getMessage().substring(0, Math.min(req.getMessage().length(), 20)));
            conversationMapper.insert(conversation);
            conversationId = conversation.getId();
        }

        // 2. 保存学生消息
        EduAiMessage userMessage = new EduAiMessage();
        userMessage.setConversationId(conversationId);
        userMessage.setStudentId(studentId);
        userMessage.setCourseId(courseId);
        userMessage.setRole("student");
        userMessage.setContent(req.getMessage());
        userMessage.setMediaType(req.getMediaType());
        userMessage.setMediaUrl(req.getMediaUrl());
        userMessage.setTokenCount(0);
        userMessage.setStatus(1);
        messageMapper.insert(userMessage);

        // 3. 获取上下文消息
        Long finalConversationId = conversationId;
        List<ChatMessage> contextMessages = getContextMessages(conversationId);

        // 4. 构建当前用户消息并添加到上下文
        UserMessage currentUserMessage = buildUserMessage(req);
        contextMessages.add(currentUserMessage);

        // 5. 调用同步AI模型
        String aiReply;
        try {
            log.info("调用AI模型，消息数量: {}", contextMessages.size());

            // 直接使用 RestTemplate 调用 API
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.setBearerAuth("tp-c2ajmdwscpit7ihqkxcyaqiwkrjpmxfnal2yob2ygvtztxxf");

            java.util.Map<String, Object> requestBody = new java.util.HashMap<>();
            requestBody.put("model", "mimo-v2.5");
            requestBody.put("messages", contextMessages.stream().map(msg -> {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("role", msg.type().name().toLowerCase());
                if (msg instanceof UserMessage) {
                    UserMessage userMsg = (UserMessage) msg;
                    boolean hasImage = userMsg.contents().stream()
                            .anyMatch(c -> c instanceof ImageContent);
                    
                    if (hasImage) {
                        // 多模态消息：包含文本和图片
                        java.util.List<java.util.Map<String, Object>> contentArray = new java.util.ArrayList<>();
                        
                        // 添加文本内容
                        String textContent = userMsg.contents().stream()
                                .filter(c -> c instanceof TextContent)
                                .map(c -> ((TextContent) c).text())
                                .collect(java.util.stream.Collectors.joining(" "));
                        if (!textContent.isEmpty()) {
                            java.util.Map<String, Object> textPart = new java.util.HashMap<>();
                            textPart.put("type", "text");
                            textPart.put("text", textContent);
                            contentArray.add(textPart);
                        }
                        
                        // 添加图片内容
                        userMsg.contents().stream()
                                .filter(c -> c instanceof ImageContent)
                                .forEach(c -> {
                                    ImageContent imgContent = (ImageContent) c;
                                    java.util.Map<String, Object> imgPart = new java.util.HashMap<>();
                                    imgPart.put("type", "image_url");
                                    java.util.Map<String, String> imgUrl = new java.util.HashMap<>();
                                    imgUrl.put("url", imgContent.image().url().toString());
                                    imgPart.put("image_url", imgUrl);
                                    contentArray.add(imgPart);
                                });
                        
                        m.put("content", contentArray);
                    } else {
                        // 纯文本消息
                        String textContent = userMsg.contents().stream()
                                .filter(c -> c instanceof TextContent)
                                .map(c -> ((TextContent) c).text())
                                .collect(java.util.stream.Collectors.joining(" "));
                        m.put("content", textContent.isEmpty() ? "" : textContent);
                    }
                } else if (msg instanceof AiMessage) {
                    m.put("content", ((AiMessage) msg).text());
                } else if (msg instanceof SystemMessage) {
                    m.put("content", ((SystemMessage) msg).text());
                }
                return m;
            }).collect(java.util.stream.Collectors.toList()));
            requestBody.put("max_tokens", 2000);

            org.springframework.http.HttpEntity<java.util.Map<String, Object>> entity =
                    new org.springframework.http.HttpEntity<>(requestBody, headers);

            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://token-plan-cn.xiaomimimo.com/v1/chat/completions",
                    entity,
                    String.class);

            log.info("AI API 响应: {}", response.getBody());

            // 解析响应
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(response.getBody());
            aiReply = root.path("choices").path(0).path("message").path("content").asText();

            log.info("AI回复内容: {}", aiReply);
        } catch (Exception e) {
            log.error("AI API调用失败: {}", e.getMessage(), e);
            throw new EduException(EduErrorCode.AI_SERVICE_ERROR);
        }

        // 6. 更新上下文记忆
        contextMessages.add(AiMessage.from(aiReply));
        chatMemory.put(finalConversationId, contextMessages);

        // 7. 保存AI回复
        AiChatRespVO respVO = AiChatRespVO.builder()
                .conversationId(finalConversationId)
                .role("assistant")
                .content(aiReply)
                .createTime(java.time.LocalDateTime.now())
                .build();

        try {
            EduAiMessage aiMessage = new EduAiMessage();
            aiMessage.setConversationId(finalConversationId);
            aiMessage.setStudentId(studentId);
            aiMessage.setCourseId(courseId);
            aiMessage.setRole("assistant");
            aiMessage.setContent(aiReply);
            aiMessage.setTokenCount(0);
            aiMessage.setStatus(1);
            messageMapper.insert(aiMessage);
            respVO.setId(aiMessage.getId());
            respVO.setCreateTime(aiMessage.getCreateTime());
        } catch (Exception e) {
            log.warn("AI回复保存失败: {}", e.getMessage());
        }

        return respVO;
    }

    @Override
    public List<AiChatRespVO> getHistory(Long studentId, Long courseId, Long chatId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = DEFAULT_HISTORY_LIMIT;
        }

        List<EduAiMessage> messages;
        if (chatId != null) {
            messages = messageMapper.selectByConversationId(chatId, limit);
        } else {
            messages = messageMapper.selectRecentByStudentAndCourse(studentId, courseId, limit);
        }

        return messages.stream()
                .map(msg -> AiChatRespVO.builder()
                        .id(msg.getId())
                        .conversationId(msg.getConversationId())
                        .role(msg.getRole())
                        .content(msg.getContent())
                        .mediaType(msg.getMediaType())
                        .mediaUrl(msg.getMediaUrl())
                        .createTime(msg.getCreateTime())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public String uploadMedia(Long studentId, MultipartFile file, String mediaType) {
        try {
            byte[] fileBytes = file.getBytes();
            return java.util.Base64.getEncoder().encodeToString(fileBytes);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public void chatStream(Long studentId, AiChatReqDTO req, SseEmitter emitter) {
        Long courseId = req.getCourseId();

        // 1. 获取或创建会话
        Long conversationId = req.getChatId();
        if (conversationId == null) {
            EduAiConversation conversation = new EduAiConversation();
            conversation.setStudentId(studentId);
            conversation.setCourseId(courseId);
            conversation.setTitle(req.getMessage().substring(0, Math.min(req.getMessage().length(), 20)));
            conversationMapper.insert(conversation);
            conversationId = conversation.getId();
        }

        // 2. 保存学生消息
        EduAiMessage userMessage = new EduAiMessage();
        userMessage.setConversationId(conversationId);
        userMessage.setStudentId(studentId);
        userMessage.setCourseId(courseId);
        userMessage.setRole("student");
        userMessage.setContent(req.getMessage());
        userMessage.setMediaType(req.getMediaType());
        userMessage.setMediaUrl(req.getMediaUrl());
        userMessage.setTokenCount(0);
        userMessage.setStatus(1);
        messageMapper.insert(userMessage);

        // 3. 获取上下文消息
        Long finalConversationId = conversationId;
        List<ChatMessage> contextMessages = getContextMessages(conversationId);

        // 4. 构建当前用户消息并添加到上下文
        UserMessage currentUserMessage = buildUserMessage(req);
        contextMessages.add(currentUserMessage);

        // 5. 异步执行流式调用（使用 RestTemplate 直接调用）
        List<ChatMessage> finalContextMessages = contextMessages;
        executorService.submit(() -> {
            StringBuilder fullResponse = new StringBuilder();
            try {
                // 发送会话ID
                emitter.send(SseEmitter.event()
                        .name("conversationId")
                        .data(finalConversationId.toString()));

                log.info("开始流式调用AI，消息数量: {}", finalContextMessages.size());

                // 构建请求体
                org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
                headers.setBearerAuth("tp-c2ajmdwscpit7ihqkxcyaqiwkrjpmxfnal2yob2ygvtztxxf");

                java.util.Map<String, Object> requestBody = new java.util.HashMap<>();
                requestBody.put("model", "mimo-v2.5");
                requestBody.put("stream", true);
                requestBody.put("messages", finalContextMessages.stream().map(msg -> {
                    java.util.Map<String, Object> m = new java.util.HashMap<>();
                    m.put("role", msg.type().name().toLowerCase());
                    if (msg instanceof UserMessage) {
                        UserMessage userMsg = (UserMessage) msg;
                        boolean hasImage = userMsg.contents().stream()
                                .anyMatch(c -> c instanceof ImageContent);
                        
                        if (hasImage) {
                            // 多模态消息：包含文本和图片
                            java.util.List<java.util.Map<String, Object>> contentArray = new java.util.ArrayList<>();
                            
                            // 添加文本内容
                            String textContent = userMsg.contents().stream()
                                    .filter(c -> c instanceof TextContent)
                                    .map(c -> ((TextContent) c).text())
                                    .collect(java.util.stream.Collectors.joining(" "));
                            if (!textContent.isEmpty()) {
                                java.util.Map<String, Object> textPart = new java.util.HashMap<>();
                                textPart.put("type", "text");
                                textPart.put("text", textContent);
                                contentArray.add(textPart);
                            }
                            
                            // 添加图片内容
                            userMsg.contents().stream()
                                    .filter(c -> c instanceof ImageContent)
                                    .forEach(c -> {
                                        ImageContent imgContent = (ImageContent) c;
                                        java.util.Map<String, Object> imgPart = new java.util.HashMap<>();
                                        imgPart.put("type", "image_url");
                                        java.util.Map<String, String> imgUrl = new java.util.HashMap<>();
                                        imgUrl.put("url", imgContent.image().url().toString());
                                        imgPart.put("image_url", imgUrl);
                                        contentArray.add(imgPart);
                                    });
                            
                            m.put("content", contentArray);
                        } else {
                            // 纯文本消息
                            String textContent = userMsg.contents().stream()
                                    .filter(c -> c instanceof TextContent)
                                    .map(c -> ((TextContent) c).text())
                                    .collect(java.util.stream.Collectors.joining(" "));
                            m.put("content", textContent.isEmpty() ? "" : textContent);
                        }
                    } else if (msg instanceof AiMessage) {
                        m.put("content", ((AiMessage) msg).text());
                    } else if (msg instanceof SystemMessage) {
                        m.put("content", ((SystemMessage) msg).text());
                    }
                    return m;
                }).collect(java.util.stream.Collectors.toList()));
                requestBody.put("max_tokens", 2000);

                org.springframework.http.HttpEntity<java.util.Map<String, Object>> entity =
                        new org.springframework.http.HttpEntity<>(requestBody, headers);

                // 使用 RestTemplate 执行流式请求
                org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> response =
                        restTemplate.exchange(
                                "https://token-plan-cn.xiaomimimo.com/v1/chat/completions",
                                org.springframework.http.HttpMethod.POST,
                                entity,
                                org.springframework.core.io.Resource.class);

                // 读取 SSE 流
                try (java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(response.getBody().getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {
                            String data = line.substring(6).trim();
                            if ("[DONE]".equals(data)) {
                                break;
                            }
                            try {
                                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                                com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(data);
                                String content = root.path("choices").path(0).path("delta").path("content").asText("");
                                if (!content.isEmpty()) {
                                    fullResponse.append(content);
                                    emitter.send(SseEmitter.event()
                                            .name("token")
                                            .data(content));
                                }
                            } catch (Exception e) {
                                log.warn("解析SSE数据失败: {}", data, e);
                            }
                        }
                    }
                }

                log.info("AI流式调用完成，回复长度: {}", fullResponse.length());

                // 发送完成标记
                emitter.send(SseEmitter.event()
                        .name("done")
                        .data("[DONE]"));

                // 更新上下文记忆
                finalContextMessages.add(AiMessage.from(fullResponse.toString()));
                chatMemory.put(finalConversationId, finalContextMessages);

                // 保存AI回复
                try {
                    EduAiMessage aiMessage = new EduAiMessage();
                    aiMessage.setConversationId(finalConversationId);
                    aiMessage.setStudentId(studentId);
                    aiMessage.setCourseId(courseId);
                    aiMessage.setRole("assistant");
                    aiMessage.setContent(fullResponse.toString());
                    aiMessage.setTokenCount(0);
                    aiMessage.setStatus(1);
                    messageMapper.insert(aiMessage);
                } catch (Exception e) {
                    log.warn("AI回复保存失败: {}", e.getMessage());
                }

                emitter.complete();

            } catch (Exception e) {
                log.error("Chat stream failed: {}", e.getMessage(), e);
                try {
                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data("AI服务暂时不可用: " + e.getMessage()));
                    emitter.completeWithError(e);
                } catch (Exception ex) {
                    log.warn("SSE error handling failed: {}", ex.getMessage());
                }
            }
        });
    }

    /**
     * 获取上下文消息（带记忆）
     */
    private List<ChatMessage> getContextMessages(Long conversationId) {
        return chatMemory.computeIfAbsent(conversationId, id -> {
            List<ChatMessage> messages = new ArrayList<>();
            // 添加系统提示
            messages.add(SystemMessage.from(
                    "你是一个专业的课程AI助教，专门帮助学生解答关于课程的问题。" +
                    "请用简洁、专业的语言回答学生的问题。"));

            // 从数据库加载历史消息
            List<EduAiMessage> history = messageMapper.selectByConversationId(id, MAX_CONTEXT_MESSAGES);
            Collections.reverse(history);
            for (EduAiMessage msg : history) {
                if ("student".equals(msg.getRole())) {
                    messages.add(UserMessage.from(msg.getContent()));
                } else if ("assistant".equals(msg.getRole())) {
                    messages.add(AiMessage.from(msg.getContent()));
                }
            }
            return messages;
        });
    }

    /**
     * 构建用户消息（支持多模态）
     */
    private UserMessage buildUserMessage(AiChatReqDTO req) {
        if (req.getMediaType() != null && req.getMediaBase64() != null) {
            List<Content> contents = new ArrayList<>();
            if ("image".equals(req.getMediaType())) {
                contents.add(ImageContent.from("data:image/jpeg;base64," + req.getMediaBase64()));
            }
            if (req.getMessage() != null && !req.getMessage().isEmpty()) {
                contents.add(TextContent.from(req.getMessage()));
            }
            return UserMessage.from(contents.toArray(new Content[0]));
        }
        return UserMessage.from(req.getMessage());
    }
}
