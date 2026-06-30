package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.AiChatReqDTO;
import com.yub.edu.biz.entity.EduAiConfig;
import com.yub.edu.biz.entity.EduAiConversation;
import com.yub.edu.biz.entity.EduAiMessage;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduTeacher;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduAiConfigMapper;
import com.yub.edu.biz.mapper.EduAiConversationMapper;
import com.yub.edu.biz.mapper.EduAiMessageMapper;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduTeacherMapper;
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
import org.springframework.beans.factory.annotation.Value;
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
 * @Version: 2.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final EduAiConversationMapper conversationMapper;
    private final EduAiMessageMapper messageMapper;
    private final EduAiConfigMapper aiConfigMapper;
    private final EduCourseMapper courseMapper;
    private final EduChapterMapper chapterMapper;
    private final EduTeacherMapper teacherMapper;
    private final ChatLanguageModel chatModel;
    private final RestTemplate restTemplate;

    @Value("${ai.api-key}")
    private String defaultApiKey;

    @Value("${ai.base-url}")
    private String defaultBaseUrl;

    @Value("${ai.model}")
    private String defaultModel;

    /** 线程池用于异步流式处理 */
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /** 消息历史缓存：conversationId -> messages */
    private final ConcurrentHashMap<Long, List<ChatMessage>> chatMemory = new ConcurrentHashMap<>();

    /** 历史消息默认返回条数 */
    private static final int DEFAULT_HISTORY_LIMIT = 50;
    /** 上下文窗口大小 */
    private static final int MAX_CONTEXT_MESSAGES = 20;

    /**
     * 检查每日对话次数限制
     */
    private void checkDailyLimit(Long studentId, Long courseId) {
        // 读取课程AI配置
        EduAiConfig config = aiConfigMapper.selectByCourseId(courseId);
        int dailyLimit = (config != null && config.getDailyLimit() != null) ? config.getDailyLimit() : 100;

        if (config == null || config.getEnabled() == null || config.getEnabled() != 1) {
            // AI助教未启用 - 但继续允许使用默认限制
            log.warn("课程 {} 未配置AI助教或未启用，使用默认限制", courseId);
        }

        int todayCount = messageMapper.countTodayMessages(studentId, courseId);
        if (todayCount >= dailyLimit) {
            log.warn("学生 {} 今日对话次数已达上限: {}/{}", studentId, todayCount, dailyLimit);
            throw new EduException(EduErrorCode.AI_DAILY_LIMIT_EXCEEDED);
        }
        log.info("学生 {} 今日对话次数: {}/{}", studentId, todayCount, dailyLimit);
    }

    /**
     * 获取课程的AI配置（含默认值）
     */
    private EduAiConfig getCourseAiConfig(Long courseId) {
        EduAiConfig config = aiConfigMapper.selectByCourseId(courseId);
        if (config == null) {
            config = new EduAiConfig();
            config.setCourseId(courseId);
            config.setEnabled(1);
            config.setModel(defaultModel);
            config.setDailyLimit(100);
            config.setSystemPrompt("你是一个专业的课程AI助教，专门帮助学生解答关于课程的问题。请用简洁、专业的语言回答学生的问题。");
        }
        return config;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiChatRespVO chat(Long studentId, AiChatReqDTO req) {
        Long courseId = req.getCourseId();
        if (courseId == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }

        // 0. 检查每日对话次数限制
        checkDailyLimit(studentId, courseId);

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

        // 3. 获取课程AI配置和上下文消息
        EduAiConfig aiConfig = getCourseAiConfig(courseId);
        Long finalConversationId = conversationId;
        List<ChatMessage> contextMessages = getContextMessages(conversationId, courseId, aiConfig);

        // 4. 构建当前用户消息并添加到上下文
        UserMessage currentUserMessage = buildUserMessage(req);
        contextMessages.add(currentUserMessage);

        // 5. 调用同步AI模型
        String aiReply;
        try {
            log.info("调用AI模型 [{}]，消息数量: {}", aiConfig.getModel(), contextMessages.size());

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.setBearerAuth(defaultApiKey);

            java.util.Map<String, Object> requestBody = new java.util.HashMap<>();
            requestBody.put("model", aiConfig.getModel() != null ? aiConfig.getModel() : defaultModel);
            requestBody.put("messages", contextMessages.stream().map(msg -> {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("role", msg.type().name().toLowerCase());
                if (msg instanceof UserMessage) {
                    UserMessage userMsg = (UserMessage) msg;
                    boolean hasImage = userMsg.contents().stream()
                            .anyMatch(c -> c instanceof ImageContent);

                    if (hasImage) {
                        java.util.List<java.util.Map<String, Object>> contentArray = new java.util.ArrayList<>();
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
            requestBody.put("max_tokens", aiConfig.getMaxTokens() != null ? aiConfig.getMaxTokens() : 2000);

            org.springframework.http.HttpEntity<java.util.Map<String, Object>> entity =
                    new org.springframework.http.HttpEntity<>(requestBody, headers);

            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity(
                    defaultBaseUrl + "/chat/completions",
                    entity,
                    String.class);

            log.info("AI API 响应: {}", response.getBody());

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
        if (courseId == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }

        // 0. 检查每日对话次数限制
        checkDailyLimit(studentId, courseId);

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

        // 3. 获取课程AI配置和上下文消息
        EduAiConfig aiConfig = getCourseAiConfig(courseId);
        Long finalConversationId = conversationId;
        List<ChatMessage> contextMessages = getContextMessages(conversationId, courseId, aiConfig);

        // 4. 构建当前用户消息并添加到上下文
        UserMessage currentUserMessage = buildUserMessage(req);
        contextMessages.add(currentUserMessage);

        // 5. 异步执行流式调用（使用 RestTemplate 直接调用）
        List<ChatMessage> finalContextMessages = contextMessages;
        String modelName = aiConfig.getModel() != null ? aiConfig.getModel() : defaultModel;
        Integer maxTokens = aiConfig.getMaxTokens() != null ? aiConfig.getMaxTokens() : 2000;

        executorService.submit(() -> {
            StringBuilder fullResponse = new StringBuilder();
            try {
                emitter.send(SseEmitter.event()
                        .name("conversationId")
                        .data(finalConversationId.toString()));

                log.info("开始流式调用AI [{}]，消息数量: {}", modelName, finalContextMessages.size());

                org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
                headers.setBearerAuth(defaultApiKey);

                java.util.Map<String, Object> requestBody = new java.util.HashMap<>();
                requestBody.put("model", modelName);
                requestBody.put("stream", true);
                requestBody.put("messages", finalContextMessages.stream().map(msg -> {
                    java.util.Map<String, Object> m = new java.util.HashMap<>();
                    m.put("role", msg.type().name().toLowerCase());
                    if (msg instanceof UserMessage) {
                        UserMessage userMsg = (UserMessage) msg;
                        boolean hasImage = userMsg.contents().stream()
                                .anyMatch(c -> c instanceof ImageContent);

                        if (hasImage) {
                            java.util.List<java.util.Map<String, Object>> contentArray = new java.util.ArrayList<>();
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
                requestBody.put("max_tokens", maxTokens);

                org.springframework.http.HttpEntity<java.util.Map<String, Object>> entity =
                        new org.springframework.http.HttpEntity<>(requestBody, headers);

                org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> response =
                        restTemplate.exchange(
                                defaultBaseUrl + "/chat/completions",
                                org.springframework.http.HttpMethod.POST,
                                entity,
                                org.springframework.core.io.Resource.class);

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
     * 获取上下文消息（带记忆），注入课程信息使得AI知道当前课程
     */
    private List<ChatMessage> getContextMessages(Long conversationId, Long courseId, EduAiConfig aiConfig) {
        // 1. 查找缓存的对话历史（不含系统提示词），或从数据库加载
        List<ChatMessage> cachedMessages = chatMemory.computeIfAbsent(conversationId, id -> {
            List<EduAiMessage> history = messageMapper.selectByConversationId(id, MAX_CONTEXT_MESSAGES);
            Collections.reverse(history);
            List<ChatMessage> msgs = new ArrayList<>();
            for (EduAiMessage msg : history) {
                if ("student".equals(msg.getRole())) {
                    msgs.add(UserMessage.from(msg.getContent()));
                } else if ("assistant".equals(msg.getRole())) {
                    msgs.add(AiMessage.from(msg.getContent()));
                }
            }
            return msgs;
        });

        // 2. 如果缓存中第一条是旧的 SystemMessage，移除它（后续会重新注入）
        //    这确保升级部署后现有对话也能获得最新的课程上下文
        List<ChatMessage> historyMessages;
        if (!cachedMessages.isEmpty() && cachedMessages.get(0) instanceof SystemMessage) {
            historyMessages = new ArrayList<>(cachedMessages.subList(1, cachedMessages.size()));
        } else {
            historyMessages = new ArrayList<>(cachedMessages);
        }

        // 3. 每次重新构建系统提示词（含最新的课程名称），不缓存
        String systemPrompt = buildSystemPrompt(courseId, aiConfig);

        // 4. 组合：新鲜的系统提示词 + 历史消息
        List<ChatMessage> result = new ArrayList<>();
        result.add(SystemMessage.from(systemPrompt));
        result.addAll(historyMessages);
        return result;
    }

    /**
     * 构建系统提示词，注入课程上下文
     */
    private String buildSystemPrompt(Long courseId, EduAiConfig aiConfig) {
        StringBuilder courseContext = new StringBuilder();

        try {
            EduCourse course = courseMapper.selectById(courseId);
            if (course != null) {
                // 课程名称
                if (course.getName() != null) {
                    courseContext.append("课程名称：").append(course.getName()).append("\n");
                }

                // 授课教师
                String teacherName = course.getTeacher();
                if (teacherName != null && !teacherName.isEmpty()) {
                    courseContext.append("授课教师：").append(teacherName);
                    // 尝试获取教师简介
                    try {
                        EduTeacher teacher = teacherMapper.selectByName(teacherName);
                        if (teacher != null && teacher.getSignature() != null && !teacher.getSignature().isEmpty()) {
                            courseContext.append("（简介：").append(teacher.getSignature()).append("）");
                        }
                    } catch (Exception e) {
                        log.warn("查询教师信息失败: {}", e.getMessage());
                    }
                    courseContext.append("\n");
                }

                // 课程简介（截取前200字）
                if (course.getIntroduction() != null && !course.getIntroduction().isEmpty()) {
                    String intro = course.getIntroduction().replaceAll("<[^>]*>", ""); // 去HTML标签
                    if (intro.length() > 200) {
                        intro = intro.substring(0, 200) + "...";
                    }
                    courseContext.append("课程简介：").append(intro).append("\n");
                }

                // 学习目标
                if (course.getLearningObjectives() != null && !course.getLearningObjectives().isEmpty()) {
                    String objectives = course.getLearningObjectives().replaceAll("<[^>]*>", "");
                    if (objectives.length() > 200) {
                        objectives = objectives.substring(0, 200) + "...";
                    }
                    courseContext.append("学习目标：").append(objectives).append("\n");
                }

                // 课程统计信息
                courseContext.append("课程统计：");
                if (course.getChapterCount() != null) courseContext.append("章节数 ").append(course.getChapterCount()).append("，");
                if (course.getVideoCount() != null) courseContext.append("视频数 ").append(course.getVideoCount()).append("，");
                if (course.getQuestionCount() != null) courseContext.append("试题数 ").append(course.getQuestionCount()).append("，");
                if (course.getExamCount() != null) courseContext.append("试卷数 ").append(course.getExamCount()).append("，");
                if (course.getStudentCount() != null) courseContext.append("学员数 ").append(course.getStudentCount());
                courseContext.append("\n");

                // 章节列表（只取名称，最多10章）
                try {
                    List<EduChapter> chapters = chapterMapper.selectTreeByCourseId(courseId);
                    if (chapters != null && !chapters.isEmpty()) {
                        courseContext.append("课程章节：\n");
                        int count = 0;
                        for (EduChapter ch : chapters) {
                            if (count >= 10) {
                                courseContext.append("  ...等共").append(chapters.size()).append("章\n");
                                break;
                            }
                            courseContext.append("  ").append(ch.getName()).append("\n");
                            count++;
                        }
                    }
                } catch (Exception e) {
                    log.warn("查询课程章节失败: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.warn("查询课程信息失败: {}", e.getMessage());
        }

        String systemPrompt = aiConfig.getSystemPrompt();
        if (systemPrompt == null || systemPrompt.isEmpty()) {
            systemPrompt = "你是一个专业的课程AI助教，专门帮助学生解答关于课程的问题。请用简洁、专业的语言回答学生的问题。";
        }

        // 拼接课程上下文 + 系统提示词
        if (courseContext.length() > 0) {
            systemPrompt = "以下是当前课程的详细信息（基于数据库实时查询，请据此回答）：\n" + courseContext + "\n" + systemPrompt;
        }

        log.info("AI系统提示词:\n{}", systemPrompt);
        return systemPrompt;
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
