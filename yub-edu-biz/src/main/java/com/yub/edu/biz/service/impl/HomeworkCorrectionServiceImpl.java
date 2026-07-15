package com.yub.edu.biz.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.HomeworkReviewDTO;
import com.yub.edu.biz.dto.HomeworkSubmitDTO;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduHomeworkCorrection;
import com.yub.edu.biz.entity.EduHomeworkQuestion;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduHomeworkCorrectionMapper;
import com.yub.edu.biz.mapper.EduHomeworkQuestionMapper;
import com.yub.edu.biz.service.AiService;
import com.yub.edu.biz.service.HomeworkCorrectionService;
import com.yub.edu.biz.vo.HomeworkCorrectionVO;
import com.yub.edu.biz.vo.HomeworkPageVO;
import com.yub.edu.biz.vo.HomeworkQuestionVO;
import com.yub.framework.security.JwtProvider;
import com.yub.framework.security.SecurityUtils;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 作业批改服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业批改业务逻辑实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkCorrectionServiceImpl implements HomeworkCorrectionService {

    private final EduHomeworkCorrectionMapper correctionMapper;
    private final EduHomeworkQuestionMapper questionMapper;
    private final EduCourseMapper courseMapper;
    // TODO: 架构治理 - Service间耦合: HomeworkCorrectionService 依赖 AiService，应通过 Manager 层解耦
    private final AiService aiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ai.model}")
    private String defaultModel;

    @Value("${ai.max-tokens:8000}")
    private int defaultMaxTokens;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HomeworkCorrectionVO submit(Long studentId, HomeworkSubmitDTO dto) {
        EduCourse course = courseMapper.selectById(dto.getCourseId());
        if (course == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }
        if (dto.getImageUrls() == null || dto.getImageUrls().isEmpty()
                || dto.getImageUrls().size() > 9) {
            throw new EduException(EduErrorCode.HOMEWORK_IMAGE_LIMIT);
        }

        EduHomeworkCorrection correction = new EduHomeworkCorrection();
        correction.setCourseId(dto.getCourseId());
        correction.setStudentId(studentId);
        correction.setImages(objectMapper.valueToTree(dto.getImageUrls()).toString());
        correction.setStatus(0);
        correction.setTotalQuestions(0);
        correction.setCorrectCount(0);
        correction.setScore(BigDecimal.ZERO);
        correctionMapper.insert(correction);

        Long correctionId = correction.getId();
        List<String> imageUrls = new ArrayList<>(dto.getImageUrls());
        String courseContext = buildCourseContext(course);

        processHomeworkAsync(correctionId, imageUrls, courseContext);
        EduHomeworkCorrection result = correctionMapper.selectById(correctionId);
        return buildDetailVO(result);
    }

    @Override
    public PageResult<HomeworkPageVO> listByStudent(Long studentId, Long courseId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        params.put("courseId", courseId);
        List<EduHomeworkCorrection> list = correctionMapper.selectPage(params);
        PageInfo<EduHomeworkCorrection> pageInfo = new PageInfo<>(list);
        List<HomeworkPageVO> records = list.stream().map(this::toPageVO).collect(Collectors.toList());
        return PageResult.of(records, pageInfo.getTotal());
    }

    @Override
    public HomeworkCorrectionVO getDetail(Long studentId, Long id) {
        EduHomeworkCorrection correction = correctionMapper.selectById(id);
        if (correction == null || !correction.getStudentId().equals(studentId)) {
            throw new EduException(EduErrorCode.HOMEWORK_CORRECTION_NOT_FOUND);
        }
        return buildDetailVO(correction);
    }

    @Override
    public PageResult<HomeworkPageVO> page(Map<String, Object> queryParams, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduHomeworkCorrection> list = correctionMapper.selectPage(queryParams);
        PageInfo<EduHomeworkCorrection> pageInfo = new PageInfo<>(list);
        List<HomeworkPageVO> records = list.stream().map(this::toPageVO).collect(Collectors.toList());
        return PageResult.of(records, pageInfo.getTotal());
    }

    @Override
    public HomeworkCorrectionVO getDetailForAdmin(Long id) {
        EduHomeworkCorrection correction = correctionMapper.selectById(id);
        if (correction == null) {
            throw new EduException(EduErrorCode.HOMEWORK_CORRECTION_NOT_FOUND);
        }
        return buildDetailVO(correction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewQuestion(Long adminId, HomeworkReviewDTO dto) {
        EduHomeworkQuestion target = findQuestionById(dto.getQuestionId());
        target.setReviewStatus(1);
        target.setIsCorrect(dto.getIsCorrect());
        target.setReviewResult(dto.getIsCorrect());
        if (dto.getStudentAnswer() != null) {
            target.setStudentAnswer(dto.getStudentAnswer());
        }
        target.setReviewAnswer(dto.getReviewAnswer());
        target.setReviewAnalysis(dto.getReviewAnalysis());
        target.setReviewedBy(adminId);
        target.setReviewTime(LocalDateTime.now());
        questionMapper.updateById(target);
        recalculateCorrectionStats(target.getCorrectionId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoReviewCorrectQuestions(Long operatorId, Long correctionId) {
        assertCorrectionOwnedByTeacher(correctionId);
        List<EduHomeworkQuestion> questions = questionMapper.selectByCorrectionId(correctionId);
        int count = 0;
        LocalDateTime now = LocalDateTime.now();
        for (EduHomeworkQuestion q : questions) {
            // 仅对 AI 判定正确的题目批量标记已复查；采用 AI 判定答案与解析，不写回教师答案
            if (q.getIsCorrect() != null && q.getIsCorrect() == 1) {
                q.setReviewStatus(1);
                q.setReviewResult(1);
                q.setReviewAnswer(q.getCorrectAnswer());
                q.setReviewAnalysis(q.getAnalysis());
                q.setReviewedBy(operatorId);
                q.setReviewTime(now);
                questionMapper.updateById(q);
                count++;
            }
        }
        recalculateCorrectionStats(correctionId);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteQuestions(Long operatorId, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<EduHomeworkQuestion> questions = questionMapper.selectByIds(ids);
        if (questions.isEmpty()) {
            return;
        }
        // 教师身份数据隔离校验：待删题目必须全部属于该教师名下的课程
        if (JwtProvider.USER_TYPE_TEACHER.equals(SecurityUtils.getCurrentUserType())) {
            Set<Long> correctionIds = questions.stream()
                    .map(EduHomeworkQuestion::getCorrectionId)
                    .collect(Collectors.toSet());
            for (Long cid : correctionIds) {
                assertCorrectionOwnedByTeacher(cid);
            }
        }
        // 物理删除（不可恢复），随后按批改记录重算统计并写回 score
        questionMapper.deleteByIds(ids);
        Set<Long> affectedCorrections = questions.stream()
                .map(EduHomeworkQuestion::getCorrectionId)
                .collect(Collectors.toSet());
        for (Long cid : affectedCorrections) {
            recalculateCorrectionStats(cid);
        }
    }

    /**
     * 校验批改记录所属课程是否归当前教师所有（仅教师身份生效，ADMIN 跳过）
     *
     * @param correctionId 批改记录ID
     */
    private void assertCorrectionOwnedByTeacher(Long correctionId) {
        if (!JwtProvider.USER_TYPE_TEACHER.equals(SecurityUtils.getCurrentUserType())) {
            return;
        }
        EduHomeworkCorrection correction = correctionMapper.selectById(correctionId);
        if (correction == null) {
            throw new EduException(EduErrorCode.HOMEWORK_CORRECTION_NOT_FOUND);
        }
        EduCourse course = courseMapper.selectById(correction.getCourseId());
        Long teacherId = SecurityUtils.getCurrentUserId();
        if (course == null || course.getTeacherId() == null || !course.getTeacherId().equals(teacherId)) {
            throw new EduException(EduErrorCode.HOMEWORK_QUESTION_ACCESS_DENIED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduHomeworkCorrection correction = correctionMapper.selectById(id);
        if (correction == null) {
            throw new EduException(EduErrorCode.HOMEWORK_CORRECTION_NOT_FOUND);
        }
        correctionMapper.softDeleteById(id);
    }

    private void processHomeworkAsync(Long correctionId, List<String> imageUrls, String courseContext) {
        try {
            EduHomeworkCorrection update = new EduHomeworkCorrection();
            update.setId(correctionId);
            update.setStatus(1);
            correctionMapper.updateById(update);

            String systemPrompt = loadPrompt() + "\n\n" + courseContext;
            String aiResponse = callAiApi(systemPrompt, imageUrls);
            parseAndSaveResult(correctionId, imageUrls, aiResponse);

        } catch (Exception e) {
            log.error("作业批改处理失败: correctionId={}, error={}", correctionId, e.getMessage(), e);
            EduHomeworkCorrection failUpdate = new EduHomeworkCorrection();
            failUpdate.setId(correctionId);
            failUpdate.setStatus(3);
            correctionMapper.updateById(failUpdate);
        }
    }

    private String loadPrompt() {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/homework-correction-prompt.md");
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("加载提示词文件失败，使用默认提示词: {}", e.getMessage());
            return "你是一个作业批改助手。请识别图片中的题目和答案，判断正误并给出解析。输出JSON格式。";
        }
    }

    private String callAiApi(String systemPrompt, List<String> imageUrls) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(SystemMessage.from(systemPrompt));
        List<Content> contents = new ArrayList<>();
        contents.add(TextContent.from("请批改以下作业图片中的所有题目"));
        imageUrls.forEach(url -> contents.add(ImageContent.from(url)));
        messages.add(UserMessage.from(contents.toArray(new Content[0])));
        return aiService.completeSync(messages);
    }

    private void parseAndSaveResult(Long correctionId, List<String> imageUrls, String aiResponse) {
        try {
            String jsonStr = sanitizeJsonEscapes(extractJson(aiResponse));
            JsonNode root;
            try {
                root = objectMapper.readTree(jsonStr);
            } catch (Exception e) {
                log.warn("JSON解析失败，尝试修复截断的JSON: {}", e.getMessage());
                String fixed = fixTruncatedJson(jsonStr);
                root = objectMapper.readTree(fixed);
            }
            JsonNode questionsNode = root.path("questions");
            JsonNode summaryNode = root.path("summary");

            List<EduHomeworkQuestion> questions = new ArrayList<>();
            int idx = 0;
            for (JsonNode qNode : questionsNode) {
                EduHomeworkQuestion question = new EduHomeworkQuestion();
                question.setCorrectionId(correctionId);
                question.setQuestionNo(qNode.path("questionNo").asInt(idx + 1));
                question.setQuestionContent(qNode.path("questionContent").asText(""));
                question.setStudentAnswer(qNode.path("studentAnswer").asText(""));
                question.setCorrectAnswer(qNode.path("correctAnswer").asText(""));
                String isCorrectStr = qNode.path("isCorrect").asText("null");
                if ("null".equals(isCorrectStr)) {
                    question.setIsCorrect(null);
                } else {
                    question.setIsCorrect(Boolean.parseBoolean(isCorrectStr) ? 1 : 0);
                }
                question.setAnalysis(qNode.path("analysis").asText(""));
                int imgIdx = qNode.path("sourceImage").asInt(1);
                question.setSourceImage(imgIdx >= 1 && imgIdx <= imageUrls.size()
                        ? imageUrls.get(imgIdx - 1) : imageUrls.get(0));
                question.setSort(idx + 1);
                questions.add(question);
                idx++;
            }

            if (!questions.isEmpty()) {
                questionMapper.batchInsert(questions);
            }

            int total = summaryNode.path("totalQuestions").asInt(questions.size());
            int correct = summaryNode.path("correctCount").asInt(0);
            int scoreVal = total > 0 ? BigDecimal.valueOf(correct)
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).intValue() : 0;

            EduHomeworkCorrection update = new EduHomeworkCorrection();
            update.setId(correctionId);
            update.setStatus(2);
            update.setTotalQuestions(total);
            update.setCorrectCount(correct);
            update.setScore(BigDecimal.valueOf(scoreVal));
            correctionMapper.updateById(update);

        } catch (Exception e) {
            log.error("解析AI批改结果失败: {}", e.getMessage(), e);
            EduHomeworkCorrection failUpdate = new EduHomeworkCorrection();
            failUpdate.setId(correctionId);
            failUpdate.setStatus(3);
            correctionMapper.updateById(failUpdate);
        }
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        // AI输出可能被截断，尝试修复不完整的JSON
        if (start >= 0) {
            String partial = text.substring(start);
            return fixTruncatedJson(partial);
        }
        return text;
    }

    /**
     * 清洗AI输出中的非法JSON转义字符（如LaTeX的\frac、\(、\d等）
     * 将非标准转义的反斜杠替换为双反斜杠
     * 同时移除JSON字符串值中的原始控制字符（换行、回车等）
     */
    private String sanitizeJsonEscapes(String json) {
        StringBuilder sb = new StringBuilder(json.length());
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(i + 1);
                // 仅保留合法JSON转义："\ / b f n r t u
                if ("\"\\/bfnrtu".indexOf(next) >= 0) {
                    sb.append(c);
                } else {
                    sb.append("\\\\");
                }
            } else if (c == '\n' || c == '\r') {
                // 原始换行/回车在JSON字符串值中不合法，替换为空格
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 修复被截断的JSON：补全缺失的闭合括号
     */
    private String fixTruncatedJson(String json) {
        // 统计未闭合的括号
        int openBraces = 0, openBrackets = 0;
        boolean inString = false;
        boolean escaped = false;
        for (char c : json.toCharArray()) {
            if (escaped) { escaped = false; continue; }
            if (c == '\\') { escaped = true; continue; }
            if (c == '"') { inString = !inString; continue; }
            if (inString) continue;
            if (c == '{') openBraces++;
            else if (c == '}') openBraces--;
            else if (c == '[') openBrackets++;
            else if (c == ']') openBrackets--;
        }
        StringBuilder sb = new StringBuilder(json);
        // 移除最后一个不完整的值（如果有逗号）
        int lastComma = sb.lastIndexOf(",");
        int lastColon = sb.lastIndexOf(":");
        if (lastComma > lastColon && lastComma > 0) {
            sb.delete(lastComma, sb.length());
        }
        // 补全闭合括号
        for (int i = 0; i < openBrackets; i++) sb.append(']');
        for (int i = 0; i < openBraces; i++) sb.append('}');
        log.warn("JSON被截断，已尝试修复: 补了{}个']'和{}个'}}'", openBrackets, openBraces);
        return sb.toString();
    }

    private String buildCourseContext(EduCourse course) {
        StringBuilder sb = new StringBuilder("当前课程信息：\n");
        sb.append("课程名称：").append(course.getName()).append("\n");
        if (course.getTeacher() != null) {
            sb.append("授课教师：").append(course.getTeacher()).append("\n");
        }
        return sb.toString();
    }

    private HomeworkPageVO toPageVO(EduHomeworkCorrection c) {
        return HomeworkPageVO.builder()
                .id(c.getId())
                .courseId(c.getCourseId())
                .courseName(c.getCourseName())
                .studentId(c.getStudentId())
                .studentName(c.getStudentName())
                .images(c.getImages())
                .totalQuestions(c.getTotalQuestions())
                .correctCount(c.getCorrectCount())
                .score(c.getScore())
                .status(c.getStatus())
                .createTime(c.getCreateTime())
                .build();
    }

    private HomeworkCorrectionVO buildDetailVO(EduHomeworkCorrection correction) {
        List<EduHomeworkQuestion> questions = questionMapper.selectByCorrectionId(correction.getId());
        List<HomeworkQuestionVO> questionVOs = questions.stream()
                .map(this::toQuestionVO)
                .collect(Collectors.toList());
        return HomeworkCorrectionVO.builder()
                .id(correction.getId())
                .courseId(correction.getCourseId())
                .courseName(correction.getCourseName())
                .studentId(correction.getStudentId())
                .studentName(correction.getStudentName())
                .images(correction.getImages())
                .totalQuestions(correction.getTotalQuestions())
                .correctCount(correction.getCorrectCount())
                .score(correction.getScore())
                .status(correction.getStatus())
                .createTime(correction.getCreateTime())
                .questions(questionVOs)
                .build();
    }

    private HomeworkQuestionVO toQuestionVO(EduHomeworkQuestion q) {
        return HomeworkQuestionVO.builder()
                .id(q.getId())
                .correctionId(q.getCorrectionId())
                .questionNo(q.getQuestionNo())
                .questionContent(q.getQuestionContent())
                .studentAnswer(q.getStudentAnswer())
                .correctAnswer(q.getCorrectAnswer())
                .isCorrect(q.getIsCorrect())
                .analysis(q.getAnalysis())
                .sourceImage(q.getSourceImage())
                .reviewStatus(q.getReviewStatus())
                .reviewResult(q.getReviewResult())
                .reviewAnswer(q.getReviewAnswer())
                .reviewAnalysis(q.getReviewAnalysis())
                .reviewedBy(q.getReviewedBy())
                .reviewTime(q.getReviewTime())
                .build();
    }

    private EduHomeworkQuestion findQuestionById(Long questionId) {
        EduHomeworkQuestion question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new EduException(EduErrorCode.HOMEWORK_QUESTION_NOT_FOUND);
        }
        return question;
    }

    private void recalculateCorrectionStats(Long correctionId) {
        Map<String, Object> stats = questionMapper.recalculateStats(correctionId);
        if (stats == null) {
            return;
        }
        int total = ((Number) stats.get("totalQuestions")).intValue();
        int correct = ((Number) stats.get("correctCount")).intValue();
        BigDecimal score = total > 0
                ? BigDecimal.valueOf(correct).divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        EduHomeworkCorrection update = new EduHomeworkCorrection();
        update.setId(correctionId);
        update.setTotalQuestions(total);
        update.setCorrectCount(correct);
        update.setScore(score);
        correctionMapper.updateById(update);
    }
}
