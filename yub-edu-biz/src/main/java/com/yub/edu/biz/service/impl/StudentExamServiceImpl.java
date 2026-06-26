package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.edu.biz.dto.ExamQueryDTO;
import com.yub.edu.biz.dto.ExamSubmitReqDTO;
import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.entity.EduExamQuestion;
import com.yub.edu.biz.entity.EduExamRecord;
import com.yub.edu.biz.entity.EduExamRecordDetail;
import com.yub.edu.biz.entity.EduExamChapterQuestionConfig;
import com.yub.edu.biz.entity.EduExamQuestionTypeConfig;
import com.yub.edu.biz.entity.EduQuestion;
import com.yub.edu.biz.entity.EduQuestionOption;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduExamMapper;
import com.yub.edu.biz.mapper.EduExamChapterQuestionConfigMapper;
import com.yub.edu.biz.mapper.EduExamQuestionMapper;
import com.yub.edu.biz.mapper.EduExamQuestionTypeConfigMapper;
import com.yub.edu.biz.mapper.EduExamRecordDetailMapper;
import com.yub.edu.biz.mapper.EduExamRecordMapper;
import com.yub.edu.biz.mapper.EduQuestionMapper;
import com.yub.edu.biz.mapper.EduQuestionOptionMapper;
import com.yub.edu.biz.service.StudentExamService;
import com.yub.edu.biz.vo.ExamHistoryRespVO;
import com.yub.edu.biz.vo.ExamInfoRespVO;
import com.yub.edu.biz.vo.ExamListRespVO;
import com.yub.edu.biz.vo.ExamQuestionRespVO;
import com.yub.edu.biz.vo.ExamQuestionResultRespVO;
import com.yub.edu.biz.vo.ExamQuestionOptionRespVO;
import com.yub.edu.biz.vo.ExamResultRespVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 学生端考试服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生端在线测试服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentExamServiceImpl implements StudentExamService {

    private final EduExamMapper eduExamMapper;
    private final EduExamQuestionMapper eduExamQuestionMapper;
    private final EduExamRecordMapper eduExamRecordMapper;
    private final EduExamRecordDetailMapper eduExamRecordDetailMapper;
    private final EduQuestionMapper eduQuestionMapper;
    private final EduQuestionOptionMapper eduQuestionOptionMapper;
    private final EduExamQuestionTypeConfigMapper eduExamQuestionTypeConfigMapper;
    private final EduExamChapterQuestionConfigMapper eduExamChapterQuestionConfigMapper;

    @Override
    public ExamListRespVO list(Long courseId, String keyword, Integer page, Integer pageSize) {
        ExamQueryDTO query = new ExamQueryDTO();
        query.setCourseId(courseId);
        query.setTitle(keyword);
        query.setStatus(1);

        PageHelper.startPage(page, pageSize);
        List<EduExam> exams = eduExamMapper.selectPage(query);
        PageInfo<EduExam> pageInfo = new PageInfo<>(exams);

        List<ExamListRespVO.ExamItemVO> list = pageInfo.getList().stream()
                .map(this::convertExamItem).toList();
        return ExamListRespVO.builder().list(list).total(pageInfo.getTotal()).build();
    }

    @Override
    public ExamInfoRespVO info(Long id) {
        EduExam exam = eduExamMapper.selectById(id);
        validateExamEnabled(exam);

        Long userId = getUserId();
        List<EduExamRecord> records = eduExamRecordMapper.selectByUserAndExam(userId, id);
        List<ExamHistoryRespVO> historyList = records.stream()
                .map(this::convertHistory).toList();
        return buildExamInfo(exam, historyList);
    }

    @Override
    public List<ExamQuestionRespVO> questions(Long examId) {
        EduExam exam = eduExamMapper.selectById(examId);
        validateExamEnabled(exam);
        Long courseId = exam.getCourseId();
        Integer questionRangeType = exam.getQuestionRangeType();
        Map<Long, Integer> scoreMap = pickQuestions(exam, courseId, questionRangeType);
        if (scoreMap.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> selectedIds = new ArrayList<>(scoreMap.keySet());
        Map<Long, EduQuestion> questionMap = getQuestionMap(selectedIds);
        Map<Long, List<EduQuestionOption>> optionMap = getOptionMap(selectedIds);
        List<ExamQuestionRespVO> result = new ArrayList<>();
        int sort = 1;
        for (Long qId : selectedIds) {
            EduQuestion question = questionMap.get(qId);
            if (question == null) continue;
            Integer ms = scoreMap.get(qId);
            List<EduQuestionOption> options = optionMap.getOrDefault(qId, Collections.emptyList());
            List<ExamQuestionOptionRespVO> optionVOs = options.stream().map(opt -> ExamQuestionOptionRespVO.builder().label(opt.getLabel()).content(opt.getContent()).sort(opt.getSort()).build()).toList();
            result.add(ExamQuestionRespVO.builder().questionId(qId).questionType(question.getQuestionType()).content(question.getContent()).score(ms).sort(sort++).options(optionVOs).build());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamResultRespVO submit(ExamSubmitReqDTO dto) {
        EduExam exam = eduExamMapper.selectById(dto.getExamId());
        validateExamEnabled(exam);
        Long userId = getUserId();
        Long courseId = exam.getCourseId();
        Integer questionRangeType = exam.getQuestionRangeType();
        Map<Integer, Integer> expectedTypeCountMap = new HashMap<>();
        Map<Long, Integer> questionIdScoreMap = new HashMap<>();
        Set<Long> validQuestionIdPool = new HashSet<>();
        int expectedTotalCount = 0;
        if (questionRangeType == 0) {
            var candidates = eduQuestionMapper.selectQuestionIdsByCourseId(courseId);
            if (candidates.isEmpty()) {
                throw new EduException(EduErrorCode.EXAM_QUESTION_NOT_ENOUGH);
            }
            int count = candidates.size();
            int totalScore = exam.getTotalScore() != null ? exam.getTotalScore() : 0;
            int baseScore = totalScore > 0 ? totalScore / count : 0;
            int remainder = totalScore > 0 ? totalScore % count : 0;
            for (int i = 0; i < count; i++) {
                int score = baseScore + (i == count - 1 ? remainder : 0);
                questionIdScoreMap.put(candidates.get(i), score);
            }
            validQuestionIdPool.addAll(candidates);
            expectedTotalCount = count;
        } else {
            var configs = eduExamChapterQuestionConfigMapper.selectByExamId(exam.getId());
            for (var cfg : configs) {
                expectedTypeCountMap.merge(cfg.getQuestionType(), cfg.getQuestionCount(), Integer::sum);
                var candidates = eduQuestionMapper.selectIdsByChapterIdAndType(cfg.getChapterId(), cfg.getQuestionType());
                validQuestionIdPool.addAll(candidates);
                candidates.forEach(id -> questionIdScoreMap.put(id, cfg.getScorePerQuestion()));
            }
            expectedTotalCount = expectedTypeCountMap.values().stream().mapToInt(Integer::intValue).sum();
        }
        if (CollectionUtils.isEmpty(dto.getAnswers())) {
            throw new EduException(EduErrorCode.EXAM_ANSWER_INVALID);
        }
        List<Long> submittedIds = dto.getAnswers().stream()
                .map(ExamSubmitReqDTO.AnswerItem::getQuestionId)
                .toList();
        for (Long id : submittedIds) {
            if (!validQuestionIdPool.contains(id)) {
                throw new EduException(EduErrorCode.EXAM_ANSWER_INVALID);
            }
        }
        Map<Long, EduQuestion> questionMap = getQuestionMap(submittedIds);
        Map<Long, List<EduQuestionOption>> optionMap = getOptionMap(submittedIds);
        if (questionRangeType == 0) {
            if (submittedIds.size() != expectedTotalCount) {
                throw new EduException(EduErrorCode.EXAM_ANSWER_INVALID);
            }
        } else {
            Map<Integer, Long> actualTypeCountMap = submittedIds.stream()
                    .map(questionMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(EduQuestion::getQuestionType, Collectors.counting()));
            for (Map.Entry<Integer, Integer> entry : expectedTypeCountMap.entrySet()) {
                long actual = actualTypeCountMap.getOrDefault(entry.getKey(), 0L);
                if (actual != entry.getValue()) {
                    throw new EduException(EduErrorCode.EXAM_ANSWER_INVALID);
                }
            }
        }
        LocalDateTime now = LocalDateTime.now();
        int totalScore = 0;
        int correctCount = 0;
        int wrongCount = 0;
        List<EduExamRecordDetail> details = new ArrayList<>();
        List<ExamQuestionResultRespVO> questionResults = new ArrayList<>();
        for (ExamSubmitReqDTO.AnswerItem answer : dto.getAnswers()) {
            EduQuestion question = questionMap.get(answer.getQuestionId());
            Integer qScore = questionIdScoreMap.get(answer.getQuestionId());
            if (question == null || qScore == null) { continue; }
            List<EduQuestionOption> options = optionMap.getOrDefault(answer.getQuestionId(), Collections.emptyList());
            boolean correct = judgeCorrect(question, options, answer.getUserAnswer());
            int score = correct ? qScore : 0;
            totalScore += score;
            if (correct) { correctCount++; } else { wrongCount++; }
            details.add(buildDetail(answer, question, options, correct, score, exam.getId(), now));
            questionResults.add(buildQuestionResult(answer, question, options, correct, score));
        }
        EduExamRecord record = buildRecord(userId, exam, totalScore, dto.getDuration(), now);
        eduExamRecordMapper.insert(record);
        saveDetails(details, record.getId());
        return buildResult(record, correctCount, wrongCount, expectedTotalCount, questionResults);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearHistory(Long examId) {
        Long userId = getUserId();
        eduExamRecordMapper.deleteByUserAndExam(userId, examId);
    }

    private Long getUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    private void validateExamEnabled(EduExam exam) {
        if (exam == null) {
            throw new EduException(EduErrorCode.EXAM_NOT_FOUND);
        }
        if (exam.getStatus() == null || exam.getStatus() != 1) {
            throw new EduException(EduErrorCode.EXAM_DISABLED);
        }
    }

    private Map<Long, Integer> pickQuestions(EduExam exam, Long courseId, Integer questionRangeType) {
        if (questionRangeType == 0) {
            return pickByCourseAll(courseId, exam.getTotalScore());
        } else {
            List<EduExamChapterQuestionConfig> configs = eduExamChapterQuestionConfigMapper.selectByExamId(exam.getId());
            return pickByChapterConfig(configs);
        }
    }

    private Map<Long, Integer> pickByCourseAll(Long courseId, Integer totalScore) {
        List<Long> candidates = eduQuestionMapper.selectQuestionIdsByCourseId(courseId);
        if (candidates.isEmpty()) {
            throw new EduException(EduErrorCode.EXAM_QUESTION_NOT_ENOUGH);
        }
        int count = candidates.size();
        int baseScore = totalScore != null && totalScore > 0 ? totalScore / count : 0;
        int remainder = totalScore != null && totalScore > 0 ? totalScore % count : 0;
        Map<Long, Integer> result = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            int score = baseScore + (i == count - 1 ? remainder : 0);
            result.put(candidates.get(i), score);
        }
        return result;
    }

    private Map<Long, Integer> pickByChapterConfig(List<EduExamChapterQuestionConfig> configs) {
        Map<Long, Integer> result = new LinkedHashMap<>();
        for (EduExamChapterQuestionConfig cfg : configs) {
            List<Long> candidates = eduQuestionMapper.selectIdsByChapterIdAndType(cfg.getChapterId(), cfg.getQuestionType());
            if (candidates.size() < cfg.getQuestionCount()) {
                throw new EduException(EduErrorCode.EXAM_QUESTION_NOT_ENOUGH);
            }
            Collections.shuffle(candidates);
            for (int i = 0; i < cfg.getQuestionCount(); i++) {
                result.put(candidates.get(i), cfg.getScorePerQuestion());
            }
        }
        return result;
    }

    
    private Map<Long, EduQuestion> getQuestionMap(List<Long> questionIds) {
        if (CollectionUtils.isEmpty(questionIds)) {
            return Collections.emptyMap();
        }
        return eduQuestionMapper.selectBatchByIds(questionIds).stream()
                .collect(Collectors.toMap(EduQuestion::getId, q -> q));
    }

    private Map<Long, List<EduQuestionOption>> getOptionMap(List<Long> questionIds) {
        if (CollectionUtils.isEmpty(questionIds)) {
            return Collections.emptyMap();
        }
        return eduQuestionOptionMapper.selectByQuestionIds(questionIds).stream()
                .collect(Collectors.groupingBy(EduQuestionOption::getQuestionId));
    }

    private boolean judgeCorrect(EduQuestion question, List<EduQuestionOption> options, String userAnswer) {
        Integer type = question.getQuestionType();
        if (type == null) {
            return false;
        }
        return switch (type) {
            case 0 -> judgeSingleChoice(options, userAnswer);
            case 1 -> judgeMultipleChoice(options, userAnswer);
            case 2 -> judgeJudgment(question.getAnswer(), userAnswer);
            case 3 -> judgeShortAnswer(userAnswer);
            case 4 -> judgeFillBlank(question.getAnswer(), userAnswer);
            default -> false;
        };
    }

    private boolean judgeSingleChoice(List<EduQuestionOption> options, String userAnswer) {
        return options.stream()
                .filter(opt -> opt.getIsCorrect() != null && opt.getIsCorrect() == 1)
                .anyMatch(opt -> Objects.equals(opt.getLabel(), userAnswer));
    }

    private boolean judgeMultipleChoice(List<EduQuestionOption> options, String userAnswer) {
        String correct = options.stream()
                .filter(opt -> opt.getIsCorrect() != null && opt.getIsCorrect() == 1)
                .map(EduQuestionOption::getLabel)
                .filter(StringUtils::hasText)
                .sorted()
                .collect(Collectors.joining(", "));
        String answered = Arrays.stream(userAnswer.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .sorted()
                .collect(Collectors.joining(", "));
        return Objects.equals(correct, answered);
    }

    private boolean judgeJudgment(String correctAnswer, String userAnswer) {
        if (!StringUtils.hasText(correctAnswer)) {
            return false;
        }
        String expected = Boolean.parseBoolean(correctAnswer) ? "正确" : "错误";
        return Objects.equals(expected, userAnswer != null ? userAnswer.trim() : null);
    }

    private boolean judgeFillBlank(String correctAnswer, String userAnswer) {
        if (!StringUtils.hasText(correctAnswer) || !StringUtils.hasText(userAnswer)) {
            return false;
        }
        return Objects.equals(correctAnswer.trim().toLowerCase(), userAnswer.trim().toLowerCase());
    }

    private boolean judgeShortAnswer(String userAnswer) {
        return StringUtils.hasText(userAnswer);
    }

    private EduExamRecordDetail buildDetail(ExamSubmitReqDTO.AnswerItem answer, EduQuestion question,
            List<EduQuestionOption> options, boolean correct, int score, Long examId, LocalDateTime now) {
        EduExamRecordDetail detail = new EduExamRecordDetail();
        detail.setExamId(examId);
        detail.setQuestionId(question.getId());
        detail.setUserAnswer(answer.getUserAnswer());
        detail.setCorrectAnswer(resolveCorrectAnswer(question, options));
        detail.setIsCorrect(correct ? 1 : 0);
        detail.setScore(score);
        return detail;
    }

    private String resolveCorrectAnswer(EduQuestion question, List<EduQuestionOption> options) {
        Integer type = question.getQuestionType();
        if (type == null) {
            return null;
        }
        return switch (type) {
            case 0, 1 -> options.stream()
                    .filter(opt -> opt.getIsCorrect() != null && opt.getIsCorrect() == 1)
                    .map(EduQuestionOption::getLabel)
                    .sorted()
                    .collect(Collectors.joining(","));
            case 2 -> Boolean.parseBoolean(question.getAnswer()) ? "正确" : "错误";
            default -> question.getAnswer();
        };
    }

    private EduExamRecord buildRecord(Long userId, EduExam exam, int totalScore, Integer duration, LocalDateTime now) {
        EduExamRecord record = new EduExamRecord();
        record.setUserId(userId);
        record.setExamId(exam.getId());
        record.setScore(totalScore);
        record.setTotalScore(exam.getTotalScore());
        record.setPassScore(exam.getPassScore());
        record.setIsPass(totalScore >= exam.getPassScore() ? 1 : 0);
        record.setDuration(duration != null ? duration : 0);
        record.setSubmitTime(now);
        return record;
    }

    private void saveDetails(List<EduExamRecordDetail> details, Long recordId) {
        if (CollectionUtils.isEmpty(details)) {
            return;
        }
        for (EduExamRecordDetail detail : details) {
            detail.setRecordId(recordId);
        }
        eduExamRecordDetailMapper.insertBatch(details);
    }

    private ExamResultRespVO buildResult(EduExamRecord record, int correctCount, int wrongCount, int totalCount,
            List<ExamQuestionResultRespVO> questionResults) {
        int unansweredCount = totalCount - correctCount - wrongCount;
        return ExamResultRespVO.builder()
                .recordId(record.getId())
                .score(record.getScore())
                .totalScore(record.getTotalScore())
                .passScore(record.getPassScore())
                .isPass(record.getIsPass())
                .correctCount(correctCount)
                .wrongCount(wrongCount)
                .unansweredCount(Math.max(0, unansweredCount))
                .totalCount(totalCount)
                .duration(record.getDuration())
                .questionResults(questionResults)
                .build();
    }

    private ExamQuestionResultRespVO buildQuestionResult(ExamSubmitReqDTO.AnswerItem answer, EduQuestion question, List<EduQuestionOption> options, boolean correct, int score) {
        List<ExamQuestionOptionRespVO> optionVOs = options.stream().map(opt -> ExamQuestionOptionRespVO.builder().label(opt.getLabel()).content(opt.getContent()).sort(opt.getSort()).build()).toList();
        return ExamQuestionResultRespVO.builder().questionId(question.getId()).questionType(question.getQuestionType()).content(question.getContent()).options(optionVOs).userAnswer(answer.getUserAnswer()).correctAnswer(resolveCorrectAnswer(question, options)).isCorrect(correct).score(score).knowledgePoint(question.getKnowledgePoints()).analysis(question.getAnalysis()).build();
    }

    private ExamHistoryRespVO convertHistory(EduExamRecord record) {
        return ExamHistoryRespVO.builder()
                .recordId(record.getId())
                .score(record.getScore())
                .totalScore(record.getTotalScore())
                .passScore(record.getPassScore())
                .isPass(record.getIsPass())
                .submitTime(record.getSubmitTime())
                .build();
    }

    private ExamInfoRespVO buildExamInfo(EduExam exam, List<ExamHistoryRespVO> historyList) {
        return ExamInfoRespVO.builder()
                .id(exam.getId())
                .name(exam.getTitle())
                .subtitle(exam.getSubtitle())
                .courseId(exam.getCourseId())
                .courseName(exam.getCourseName())
                .duration(exam.getDuration())
                .totalScore(exam.getTotalScore())
                .passScore(exam.getPassScore())
                .introduction(exam.getIntroduction())
                .notes(exam.getNotes())
                .examiner(exam.getExaminer())
                .historyList(historyList)
                .build();
    }

    private ExamListRespVO.ExamItemVO convertExamItem(EduExam exam) {
        return ExamListRespVO.ExamItemVO.builder()
                .id(exam.getId())
                .title(exam.getTitle())
                .subtitle(exam.getSubtitle())
                .courseId(exam.getCourseId())
                .courseName(exam.getCourseName())
                .duration(exam.getDuration())
                .totalScore(exam.getTotalScore())
                .passScore(exam.getPassScore())
                .difficulty(exam.getDifficulty())
                .isFinalExam(exam.getIsFinalExam())
                .build();
    }
}
