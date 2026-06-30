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
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.mapper.EduQuestionMapper;
import com.yub.edu.biz.mapper.EduQuestionOptionMapper;
import com.yub.edu.biz.mapper.StudyRecordMapper;
import com.yub.edu.biz.service.StudentExamService;
import com.yub.edu.biz.vo.CourseFinalExamRespVO;
import com.yub.edu.biz.vo.ExamHistoryRespVO;
import com.yub.edu.biz.vo.ExamInfoRespVO;
import com.yub.edu.biz.vo.ExamListRespVO;
import com.yub.edu.biz.vo.ExamQuestionRespVO;
import com.yub.edu.biz.vo.ExamQuestionResultRespVO;
import com.yub.edu.biz.vo.ExamQuestionOptionRespVO;
import com.yub.edu.biz.vo.ExamResultRespVO;
import com.yub.edu.biz.vo.ExamStartRespVO;
import com.yub.framework.redis.RedisUtils;
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
    private final EduChapterMapper eduChapterMapper;
    private final StudyRecordMapper studyRecordMapper;
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
        Long userId = getUserId();

        // 新流程：有 recordId，根据 recordId 查询记录，仅 status=0 可提交
        if (dto.getRecordId() != null) {
            EduExamRecord record = eduExamRecordMapper.selectById(dto.getRecordId());
            if (record == null || !record.getUserId().equals(userId)) {
                throw new EduException(EduErrorCode.EXAM_RECORD_NOT_FOUND);
            }
            if (record.getStatus() != 0) {
                throw new EduException(EduErrorCode.EXAM_ALREADY_SUBMITTED);
            }
            // 复用原有判分逻辑，使用 record 关联的试卷
            ExamSubmitReqDTO oldDto = new ExamSubmitReqDTO();
            oldDto.setExamId(record.getExamId());
            oldDto.setAnswers(dto.getAnswers());
            oldDto.setDuration(dto.getDuration());
            return doSubmit(oldDto, record, userId);
        }

        // 旧流程：无 recordId，创建新记录（兼容已有调用方）
        EduExam exam = eduExamMapper.selectById(dto.getExamId());
        validateExamEnabled(exam);
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

    @Override
    public CourseFinalExamRespVO getCourseFinalExam(Long courseId) {
        // 查询课程关联的结课考试
        EduExam exam = eduExamMapper.selectFinalExamByCourseId(courseId);
        if (exam == null) {
            return CourseFinalExamRespVO.builder()
                    .examId(null)
                    .canTake(false)
                    .cannotTakeReason("该课程暂未设置结课考试")
                    .attemptedCount(0)
                    .remainingAttempts(0)
                    .currentCompletionRate(0)
                    .chapterCompletionRate(0)
                    .chapterProgressMet(false)
                    .highestScore(0)
                    .everPassed(false)
                    .historyList(Collections.emptyList())
                    .build();
        }

        Long userId = getUserId();

        // 计算已考次数和最高分
        int submittedCount = eduExamRecordMapper.selectSubmittedCount(userId, exam.getId());
        Integer maxScore = eduExamRecordMapper.selectMaxScore(userId, exam.getId());
        int highestScore = maxScore != null ? maxScore : 0;

        // 查询历史成绩列表
        List<EduExamRecord> records = eduExamRecordMapper.selectByUserAndExam(userId, exam.getId());
        List<ExamHistoryRespVO> historyList = records.stream()
                .map(this::convertHistory).toList();

        // 是否有过及格的记录
        boolean everPassed = records.stream().anyMatch(r -> r.getIsPass() != null && r.getIsPass() == 1);

        // 计算章节完成率
        int chapterProgress = calculateChapterProgress(courseId, userId);

        int maxAttempts = exam.getMaxAttempts() != null ? exam.getMaxAttempts() : 0;
        int chapterPassRate = exam.getChapterPassRate() != null ? exam.getChapterPassRate() : 0;

        // 校验是否可考
        boolean canTake = true;
        String reason = null;

        // 校验重考次数
        if (maxAttempts > 0 && submittedCount >= maxAttempts) {
            canTake = false;
            reason = "已达到最大参考次数（" + maxAttempts + "次），无法继续考试";
        }

        // 校验章节完成率
        if (canTake && chapterPassRate > 0 && chapterProgress < chapterPassRate) {
            canTake = false;
            reason = "章节学习进度不足（当前" + chapterProgress + "%，需要" + chapterPassRate + "%），无法参加结课考试";
        }

        int remainingAttempts = maxAttempts > 0 ? Math.max(0, maxAttempts - submittedCount) : -1;

        return CourseFinalExamRespVO.builder()
                .examId(exam.getId())
                .examName(exam.getTitle())
                .duration(exam.getDuration())
                .totalScore(exam.getTotalScore())
                .passScore(exam.getPassScore())
                .maxAttempts(maxAttempts)
                .chapterCompletionRate(chapterPassRate)
                .currentCompletionRate(chapterProgress)
                .canTake(canTake)
                .cannotTakeReason(reason)
                .attemptedCount(submittedCount)
                .remainingAttempts(remainingAttempts)
                .chapterProgressMet(chapterProgress >= chapterPassRate)
                .highestScore(highestScore)
                .everPassed(everPassed)
                .historyList(historyList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamStartRespVO startExam(Long examId) {
        EduExam exam = eduExamMapper.selectById(examId);
        validateExamEnabled(exam);

        Long userId = getUserId();

        // 校验重考次数
        int submittedCount = eduExamRecordMapper.selectSubmittedCount(userId, examId);
        int maxAttempts = exam.getMaxAttempts() != null ? exam.getMaxAttempts() : 0;
        if (maxAttempts > 0 && submittedCount >= maxAttempts) {
            throw new EduException(EduErrorCode.EXAM_MAX_ATTEMPTS_REACHED);
        }

        // 校验章节完成率（结课考试检查，其他考试跳过）
        if (exam.getIsFinalExam() != null && exam.getIsFinalExam() == 1) {
            int chapterPassRate = exam.getChapterPassRate() != null ? exam.getChapterPassRate() : 0;
            if (chapterPassRate > 0) {
                int chapterProgress = calculateChapterProgress(exam.getCourseId(), userId);
                if (chapterProgress < chapterPassRate) {
                    throw new EduException(EduErrorCode.EXAM_CHAPTER_PROGRESS_NOT_ENOUGH);
                }
            }
        }

        // 检查是否有进行中的记录
        EduExamRecord inProgress = eduExamRecordMapper.selectInProgress(userId, examId);
        if (inProgress != null) {
            // 直接返回进行中的考试
            List<ExamQuestionRespVO> existingQuestions = questions(examId);
            LocalDateTime endTime = inProgress.getStartTime().plusMinutes(exam.getDuration());
            return ExamStartRespVO.builder()
                    .recordId(inProgress.getId())
                    .duration(exam.getDuration())
                    .endTime(endTime)
                    .questions(existingQuestions)
                    .build();
        }

        // 使用分布式锁防止并发创建
        String lockKey = "exam:start:" + userId + ":" + examId;
        final EduExamRecord[] recordHolder = new EduExamRecord[1];
        final boolean[] isNewRecord = {false};

        RedisUtils.runWithLock(lockKey, () -> {
            // 双重检查：锁内再次检查是否有进行中的记录
            EduExamRecord existing = eduExamRecordMapper.selectInProgress(userId, examId);
            if (existing != null) {
                recordHolder[0] = existing;
                return;
            }

            // 创建考试记录
            LocalDateTime now = LocalDateTime.now();
            EduExamRecord record = new EduExamRecord();
            record.setUserId(userId);
            record.setExamId(examId);
            record.setAttemptNo(submittedCount + 1);
            record.setStatus(0);
            record.setScore(0);
            record.setTotalScore(exam.getTotalScore());
            record.setPassScore(exam.getPassScore());
            record.setIsPass(0);
            record.setDuration(0);
            record.setStartTime(now);
            record.setSubmitTime(now);
            record.setHeartbeatTime(now);
            eduExamRecordMapper.insert(record);
            recordHolder[0] = record;
            isNewRecord[0] = true;
        });

        EduExamRecord record = recordHolder[0];

        List<ExamQuestionRespVO> questionVOs;
        if (isNewRecord[0]) {
            // 新创建的记录：抽题
            Long courseId = exam.getCourseId();
            Integer questionRangeType = exam.getQuestionRangeType();
            Map<Long, Integer> scoreMap = pickQuestions(exam, courseId, questionRangeType);
            questionVOs = buildQuestionVOs(scoreMap);
        } else {
            // 已存在的进行中记录：使用现有题目
            questionVOs = questions(examId);
        }
        LocalDateTime endTime = record.getStartTime().plusMinutes(exam.getDuration());
        return ExamStartRespVO.builder()
                .recordId(record.getId())
                .duration(exam.getDuration())
                .endTime(endTime)
                .questions(questionVOs)
                .build();
    }

    @Override
    public ExamResultRespVO getExamResult(Long recordId) {
        Long userId = getUserId();
        EduExamRecord record = eduExamRecordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw new EduException(EduErrorCode.EXAM_RECORD_NOT_FOUND);
        }

        EduExam exam = eduExamMapper.selectById(record.getExamId());
        if (exam == null) {
            throw new EduException(EduErrorCode.EXAM_NOT_FOUND);
        }

        // 加载答题明细
        List<EduExamRecordDetail> details = eduExamRecordDetailMapper.selectByRecordId(recordId);

        // 统计正确/错误题数
        int correctCount = 0;
        int wrongCount = 0;
        List<Long> questionIds = new ArrayList<>();
        for (EduExamRecordDetail detail : details) {
            if (detail.getIsCorrect() != null && detail.getIsCorrect() == 1) {
                correctCount++;
            } else {
                wrongCount++;
            }
            questionIds.add(detail.getQuestionId());
        }

        // 计算总题数
        int totalCount;
        if (exam.getQuestionRangeType() == 0) {
            totalCount = eduQuestionMapper.selectQuestionIdsByCourseId(exam.getCourseId()).size();
        } else {
            var configs = eduExamChapterQuestionConfigMapper.selectByExamId(exam.getId());
            totalCount = configs.stream().mapToInt(EduExamChapterQuestionConfig::getQuestionCount).sum();
        }

        // 加载题目和选项
        Map<Long, EduQuestion> questionMap = getQuestionMap(questionIds);
        Map<Long, List<EduQuestionOption>> optionMap = getOptionMap(questionIds);

        // 构建每题结果
        List<ExamQuestionResultRespVO> questionResults = details.stream()
                .map(detail -> {
                    EduQuestion question = questionMap.get(detail.getQuestionId());
                    List<EduQuestionOption> options = optionMap.getOrDefault(detail.getQuestionId(), Collections.emptyList());
                    boolean correct = detail.getIsCorrect() != null && detail.getIsCorrect() == 1;
                    List<ExamQuestionOptionRespVO> optionVOs = options.stream()
                            .map(opt -> ExamQuestionOptionRespVO.builder().label(opt.getLabel()).content(opt.getContent()).sort(opt.getSort()).build())
                            .toList();
                    return ExamQuestionResultRespVO.builder()
                            .questionId(detail.getQuestionId())
                            .questionType(question != null ? question.getQuestionType() : null)
                            .content(question != null ? question.getContent() : null)
                            .options(optionVOs)
                            .userAnswer(detail.getUserAnswer())
                            .correctAnswer(detail.getCorrectAnswer())
                            .isCorrect(correct)
                            .score(detail.getScore())
                            .knowledgePoint(question != null ? question.getKnowledgePoints() : null)
                            .analysis(question != null ? question.getAnalysis() : null)
                            .build();
                })
                .toList();

        return buildResult(record, correctCount, wrongCount, totalCount, questionResults);
    }

    @Override
    public void heartbeat(Long recordId) {
        Long userId = getUserId();
        EduExamRecord record = eduExamRecordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw new EduException(EduErrorCode.EXAM_RECORD_NOT_FOUND);
        }
        if (record.getStatus() != 0) {
            throw new EduException(EduErrorCode.EXAM_ALREADY_SUBMITTED);
        }

        // 获取考试时长，校验是否超时
        EduExam exam = eduExamMapper.selectById(record.getExamId());
        if (exam != null) {
            LocalDateTime deadline = record.getStartTime().plusMinutes(exam.getDuration());
            if (LocalDateTime.now().isAfter(deadline)) {
                // 超时，自动提交
                autoSubmitTimeoutRecord(record, exam);
                return;
            }
        }

        // 更新心跳时间
        eduExamRecordMapper.updateHeartbeat(recordId, LocalDateTime.now());
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
                .maxAttempts(exam.getMaxAttempts())
                .chapterPassRate(exam.getChapterPassRate())
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

    // ==================== 结课考试辅助方法 ====================

    /**
     * 计算用户在课程中的章节完成率
     *
     * @param courseId 课程ID
     * @param userId   用户ID
     * @return 章节完成率（%）
     */
    private int calculateChapterProgress(Long courseId, Long userId) {
        int totalChapters = eduChapterMapper.countByCourseId(courseId);
        if (totalChapters == 0) {
            return 0;
        }
        int studiedChapters = studyRecordMapper.countStudiedChapters(userId, courseId);
        return studiedChapters * 100 / totalChapters;
    }

    /**
     * 根据抽题结果构建题目 VO 列表
     */
    private List<ExamQuestionRespVO> buildQuestionVOs(Map<Long, Integer> scoreMap) {
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
            List<ExamQuestionOptionRespVO> optionVOs = options.stream()
                    .map(opt -> ExamQuestionOptionRespVO.builder().label(opt.getLabel()).content(opt.getContent()).sort(opt.getSort()).build())
                    .toList();
            result.add(ExamQuestionRespVO.builder().questionId(qId).questionType(question.getQuestionType()).content(question.getContent()).score(ms).sort(sort++).options(optionVOs).build());
        }
        return result;
    }

    /**
     * 新流程判分（基于已有 record，不新建）
     */
    private ExamResultRespVO doSubmit(ExamSubmitReqDTO dto, EduExamRecord record, Long userId) {
        EduExam exam = eduExamMapper.selectById(dto.getExamId() != null ? dto.getExamId() : record.getExamId());
        if (exam == null) {
            throw new EduException(EduErrorCode.EXAM_NOT_FOUND);
        }
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
        // 更新已有记录
        int isPass = totalScore >= exam.getPassScore() ? 1 : 0;
        int status = 1;
        eduExamRecordMapper.updateStatus(record.getId(), status, totalScore, isPass,
                dto.getDuration() != null ? dto.getDuration() : 0, now);
        // 写答题明细
        saveDetails(details, record.getId());
        // 构造结果 VO
        record.setScore(totalScore);
        record.setTotalScore(exam.getTotalScore());
        record.setPassScore(exam.getPassScore());
        record.setIsPass(isPass);
        record.setDuration(dto.getDuration() != null ? dto.getDuration() : 0);
        record.setSubmitTime(now);
        record.setStatus(status);
        return buildResult(record, correctCount, wrongCount, expectedTotalCount, questionResults);
    }

    /**
     * 自动提交超时记录（定时任务/心跳检测到超时时调用）
     */
    @Transactional(rollbackFor = Exception.class)
    public void autoSubmitTimeoutRecord(EduExamRecord record, EduExam exam) {
        if (record.getStatus() != 0) {
            return;
        }
        log.info("自动提交超时考试记录: recordId={}, userId={}, examId={}", record.getId(), record.getUserId(), record.getExamId());
        // 超时交卷，无答案，得 0 分
        int status = 2;
        int score = 0;
        int isPass = 0;
        LocalDateTime now = LocalDateTime.now();
        // 计算已用时间（秒）
        long usedSeconds = java.time.Duration.between(record.getStartTime(), now).getSeconds();
        int duration = (int) Math.min(usedSeconds, Integer.MAX_VALUE);
        eduExamRecordMapper.updateStatus(record.getId(), status, score, isPass, duration, now);
    }
}
