package com.yub.edu.biz.service.impl;

import com.yub.edu.api.dto.app.MyCoursePageQueryDTO;
import com.yub.edu.api.vo.app.MyCourseVO;
import com.yub.edu.biz.dto.AnswerSubmitReqDTO;
import com.yub.edu.biz.dto.FavoriteToggleReqDTO;
import com.yub.edu.biz.dto.NoteCreateReqDTO;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduPracticeRecord;
import com.yub.edu.biz.entity.EduPracticeSession;
import com.yub.edu.biz.entity.EduQuestion;
import com.yub.edu.biz.entity.EduQuestionFavorite;
import com.yub.edu.biz.entity.EduQuestionNote;
import com.yub.edu.biz.entity.EduQuestionOption;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduPracticeRecordMapper;
import com.yub.edu.biz.mapper.EduPracticeSessionMapper;
import com.yub.edu.biz.mapper.EduQuestionFavoriteMapper;
import com.yub.edu.biz.mapper.EduQuestionMapper;
import com.yub.edu.biz.mapper.EduQuestionNoteMapper;
import com.yub.edu.biz.mapper.EduQuestionOptionMapper;
import com.yub.edu.biz.service.PracticeService;
import com.yub.edu.biz.vo.*;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PracticeServiceImpl implements PracticeService {
    private final EduPracticeRecordMapper practiceRecordMapper;
    private final EduQuestionFavoriteMapper questionFavoriteMapper;
    private final EduQuestionNoteMapper questionNoteMapper;
    private final EduPracticeSessionMapper practiceSessionMapper;
    private final EduChapterMapper eduChapterMapper;
    private final EduQuestionMapper eduQuestionMapper;
    private final EduQuestionOptionMapper eduQuestionOptionMapper;
    private final EduCourseMapper eduCourseMapper;

    private Long getUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    @Override
    public PracticeOverviewRespVO getPracticeOverview(Long courseId) {
        Long userId = getUserId();
        int totalQuestions = eduQuestionMapper.countByCourseId(courseId);
        int practicedCount = practiceRecordMapper.countByUserAndCourse(userId, courseId);
        int correctCount = practiceRecordMapper.countCorrectByUserAndCourse(userId, courseId);
        int passRate = practicedCount > 0 ? correctCount * 100 / practicedCount : 0;

        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(courseId);
        List<ChapterPracticeProgressVO> chapterProgressList = buildChapterProgress(chapters, userId);

        return PracticeOverviewRespVO.builder()
                .totalQuestionCount(totalQuestions)
                .practicedCount(practicedCount)
                .totalAttempts(practicedCount)
                .passRate(passRate)
                .chapterProgressList(chapterProgressList)
                .build();
    }

    private List<ChapterPracticeProgressVO> buildChapterProgress(List<EduChapter> chapters, Long userId) {
        Map<Long, List<EduChapter>> parentMap = chapters.stream()
                .collect(Collectors.groupingBy(EduChapter::getParentId));
        List<ChapterPracticeProgressVO> result = new ArrayList<>();
        List<EduChapter> roots = parentMap.getOrDefault(0L, new ArrayList<>());
        roots.sort(Comparator.comparingInt(EduChapter::getSort));
        for (EduChapter root : roots) {
            result.add(buildChapterNode(root, parentMap, userId));
        }
        return result;
    }

    private ChapterPracticeProgressVO buildChapterNode(EduChapter chapter, Map<Long, List<EduChapter>> parentMap, Long userId) {
        List<EduChapter> children = parentMap.getOrDefault(chapter.getId(), new ArrayList<>());
        children.sort(Comparator.comparingInt(EduChapter::getSort));

        int totalQuestions = eduQuestionMapper.countByChapterId(chapter.getId());
        int practicedDistinct = practiceRecordMapper.countDistinctByUserAndChapter(userId, chapter.getId());
        int totalAttempts = practiceRecordMapper.countByUserAndChapter(userId, chapter.getId());
        int correctCount = practiceRecordMapper.countCorrectByUserAndChapter(userId, chapter.getId());

        List<ChapterPracticeProgressVO> childVos = new ArrayList<>();
        for (EduChapter child : children) {
            ChapterPracticeProgressVO childVo = buildChapterNode(child, parentMap, userId);
            childVos.add(childVo);
            totalQuestions += childVo.getTotalQuestionCount();
            practicedDistinct += childVo.getPracticedQuestionCount();
            totalAttempts += childVo.getTotalAttempts();
            correctCount += childVo.getCorrectCount();
        }

        int accuracyRate = totalAttempts > 0 ? correctCount * 100 / totalAttempts : 0;
        return ChapterPracticeProgressVO.builder()
                .chapterId(chapter.getId())
                .chapterName(chapter.getName())
                .practicedQuestionCount(practicedDistinct)
                .totalQuestionCount(totalQuestions)
                .accuracyRate(accuracyRate)
                .correctCount(correctCount)
                .totalAttempts(totalAttempts)
                .children(childVos.isEmpty() ? null : childVos)
                .build();
    }

    @Override
    public List<ChapterTreeNodeVO> getChapterTree(Long courseId) {
        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(courseId);
        return chapters.stream().map(ch -> ChapterTreeNodeVO.builder()
                .id(ch.getId()).name(ch.getName()).parentId(ch.getParentId())
                .sort(ch.getSort())
                .hasChildren(chapters.stream().anyMatch(c -> c.getParentId().equals(ch.getId())))
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<PracticeQuestionRespVO> getQuestions(Long courseId, Long chapterId, Integer practiceMode) {
        Long userId = getUserId();
        List<Long> questionIds = resolveQuestionIds(userId, courseId, chapterId, practiceMode);
        if (questionIds.isEmpty()) return new ArrayList<>();

        List<EduQuestion> questions = eduQuestionMapper.selectBatchByIds(questionIds);
        Map<Long, EduQuestion> questionMap = questions.stream()
                .collect(Collectors.toMap(EduQuestion::getId, q -> q));

        List<PracticeQuestionRespVO> result = new ArrayList<>();
        for (int i = 0; i < questionIds.size(); i++) {
            Long qid = questionIds.get(i);
            EduQuestion q = questionMap.get(qid);
            if (q == null) continue;

            boolean favorited = questionFavoriteMapper.selectByUserAndQuestion(userId, qid) != null;
            List<EduQuestionOption> options = (q.getQuestionType() != null && (q.getQuestionType() <= 1 || q.getQuestionType() == 4))
                    ? eduQuestionOptionMapper.selectByQuestionId(qid) : null;

            result.add(PracticeQuestionRespVO.builder()
                    .id(q.getId()).questionType(q.getQuestionType()).content(q.getContent())
                    .difficulty(q.getDifficulty()).knowledgePoints(q.getKnowledgePoints())
                    .options(options).favorited(favorited).currentIndex(i)
                    .totalCount(questionIds.size()).practiceMode(practiceMode)
                    .courseId(courseId).chapterId(q.getChapterId()).analysis(q.getAnalysis()).build());
        }
        return result;
    }

    private List<Long> resolveQuestionIds(Long userId, Long courseId, Long chapterId, Integer practiceMode) {
        if (practiceMode == 1) {
            return (chapterId != null && chapterId > 0)
                    ? eduQuestionMapper.selectQuestionIdsByChapterId(chapterId)
                    : eduQuestionMapper.selectQuestionIdsByCourseId(courseId);
        } else if (practiceMode == 2) {
            return practiceRecordMapper.selectWrongByUserAndCourse(userId, courseId).stream()
                    .map(EduPracticeRecord::getQuestionId).distinct().collect(Collectors.toList());
        } else if (practiceMode == 3) {
            return questionFavoriteMapper.selectByUserAndCourse(userId, courseId).stream()
                    .map(EduQuestionFavorite::getQuestionId).collect(Collectors.toList());
        } else if (practiceMode == 4) {
            return practiceRecordMapper.selectHighFreqWrong(userId, courseId, 50);
        } else if (practiceMode == 5) {
            PracticeSessionRespVO session = continuePractice(courseId);
            if (session != null && session.getChapterId() != null) {
                return eduQuestionMapper.selectQuestionIdsByChapterId(session.getChapterId());
            }
            return eduQuestionMapper.selectQuestionIdsByCourseId(courseId);
        }
        return eduQuestionMapper.selectQuestionIdsByCourseId(courseId);
    }

    @Override
    public PracticeQuestionRespVO getQuestion(Long courseId, Long chapterId, Integer practiceMode, Integer index) {
        List<PracticeQuestionRespVO> questions = getQuestions(courseId, chapterId, practiceMode);
        if (questions.isEmpty() || index < 0 || index >= questions.size()) {
            throw new EduException(EduErrorCode.NO_MORE_QUESTIONS);
        }
        return questions.get(index);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitAnswer(AnswerSubmitReqDTO dto) {
        Long userId = getUserId();
        EduPracticeRecord record = new EduPracticeRecord();
        record.setUserId(userId);
        record.setCourseId(dto.getCourseId());
        record.setChapterId(dto.getChapterId());
        record.setQuestionId(dto.getQuestionId());
        record.setUserAnswer(dto.getUserAnswer());
        record.setIsCorrect(dto.getIsCorrect());
        record.setAnswerDuration(dto.getAnswerDuration());
        record.setPracticeMode(dto.getPracticeMode());
        record.setSourceRecordId(dto.getSourceRecordId());
        practiceRecordMapper.insert(record);

        EduPracticeSession session = practiceSessionMapper.selectByUserAndCourse(userId, dto.getCourseId());
        if (session == null) {
            session = new EduPracticeSession();
            session.setUserId(userId);
            session.setCourseId(dto.getCourseId());
        }
        session.setChapterId(dto.getChapterId());
        session.setQuestionId(dto.getQuestionId());
        session.setPracticeMode(dto.getPracticeMode());
        session.setStatus(1);
        if (session.getId() == null) {
            practiceSessionMapper.insert(session);
        } else {
            practiceSessionMapper.updateById(session);
        }
    }

    @Override
    public List<PracticeQuestionSimpleVO> getWrongQuestions(Long courseId) {
        Long userId = getUserId();
        List<EduPracticeRecord> wrongRecords = practiceRecordMapper.selectWrongByUserAndCourse(userId, courseId);
        Map<Long, Long> wrongCountMap = wrongRecords.stream()
                .collect(Collectors.groupingBy(EduPracticeRecord::getQuestionId, Collectors.counting()));
        Map<Long, EduPracticeRecord> latestMap = new HashMap<>();
        for (EduPracticeRecord r : wrongRecords) {
            latestMap.putIfAbsent(r.getQuestionId(), r);
        }
        List<PracticeQuestionSimpleVO> result = new ArrayList<>();
        for (Map.Entry<Long, EduPracticeRecord> entry : latestMap.entrySet()) {
            Long qid = entry.getKey();
            EduQuestion q = eduQuestionMapper.selectById(qid);
            if (q == null) continue;
            result.add(PracticeQuestionSimpleVO.builder().id(q.getId()).questionType(q.getQuestionType())
                    .content(q.getContent()).difficulty(q.getDifficulty()).courseId(courseId)
                    .wrongCount(wrongCountMap.getOrDefault(qid, 0L).intValue())
                    .chapterName(getChapterName(q.getChapterId()))
                    .relatedTime(entry.getValue().getCreateTime()).build());
        }
        result.sort((a, b) -> b.getWrongCount().compareTo(a.getWrongCount()));
        return result;
    }

    private String getChapterName(Long chapterId) {
        if (chapterId == null) return null;
        EduChapter chapter = eduChapterMapper.selectById(chapterId);
        return chapter != null ? chapter.getName() : null;
    }

    @Override
    public List<PracticeQuestionSimpleVO> getFavorites(Long courseId) {
        Long userId = getUserId();
        List<EduQuestionFavorite> favorites = questionFavoriteMapper.selectByUserAndCourse(userId, courseId);
        List<PracticeQuestionSimpleVO> result = new ArrayList<>();
        for (EduQuestionFavorite fav : favorites) {
            EduQuestion q = eduQuestionMapper.selectById(fav.getQuestionId());
            if (q == null) continue;
            result.add(PracticeQuestionSimpleVO.builder().id(q.getId()).questionType(q.getQuestionType())
                    .content(q.getContent()).difficulty(q.getDifficulty()).courseId(fav.getCourseId())
                    .chapterName(getChapterName(q.getChapterId())).relatedTime(fav.getCreateTime()).build());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavoriteToggleRespVO toggleFavorite(FavoriteToggleReqDTO dto) {
        Long userId = getUserId();
        EduQuestionFavorite existing = questionFavoriteMapper.selectByUserAndQuestion(userId, dto.getQuestionId());
        if (existing != null) {
            questionFavoriteMapper.deleteByUserAndQuestion(userId, dto.getQuestionId());
            return FavoriteToggleRespVO.builder().favorited(false).build();
        } else {
            EduQuestionFavorite fav = new EduQuestionFavorite();
            fav.setUserId(userId);
            fav.setQuestionId(dto.getQuestionId());
            fav.setCourseId(dto.getCourseId());
            questionFavoriteMapper.insert(fav);
            return FavoriteToggleRespVO.builder().favorited(true).build();
        }
    }

    @Override
    public List<NoteRespVO> getNotes(Long courseId) {
        Long userId = getUserId();
        List<EduQuestionNote> notes = questionNoteMapper.selectByUserAndCourse(userId, courseId);
        List<NoteRespVO> result = new ArrayList<>();
        for (EduQuestionNote note : notes) {
            EduQuestion q = eduQuestionMapper.selectById(note.getQuestionId());
            result.add(NoteRespVO.builder().id(note.getId()).questionId(note.getQuestionId())
                    .courseId(note.getCourseId()).noteContent(note.getNoteContent())
                    .questionType(q != null ? q.getQuestionType() : null)
                    .questionContent(q != null ? q.getContent() : null)
                    .createTime(note.getCreateTime()).build());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NoteRespVO createNote(NoteCreateReqDTO dto) {
        Long userId = getUserId();
        EduQuestionNote note = new EduQuestionNote();
        note.setUserId(userId);
        note.setCourseId(dto.getCourseId());
        note.setQuestionId(dto.getQuestionId());
        note.setNoteContent(dto.getNoteContent());
        questionNoteMapper.insert(note);
        EduQuestion q = eduQuestionMapper.selectById(dto.getQuestionId());
        return NoteRespVO.builder().id(note.getId()).questionId(note.getQuestionId())
                .courseId(note.getCourseId()).noteContent(note.getNoteContent())
                .questionType(q != null ? q.getQuestionType() : null)
                .questionContent(q != null ? q.getContent() : null)
                .createTime(note.getCreateTime()).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNote(Long noteId) {
        EduQuestionNote note = questionNoteMapper.selectById(noteId);
        if (note == null) {
            throw new EduException(EduErrorCode.NOTE_NOT_FOUND);
        }
        if (!note.getUserId().equals(getUserId())) {
            throw new EduException(EduErrorCode.NOTE_NOT_FOUND);
        }
        questionNoteMapper.deleteById(noteId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNote(Long noteId, String noteContent) {
        if (noteContent == null || noteContent.isBlank()) {
            throw new EduException(EduErrorCode.PARAM_INVALID);
        }
        EduQuestionNote note = questionNoteMapper.selectById(noteId);
        if (note == null) {
            throw new EduException(EduErrorCode.NOTE_NOT_FOUND);
        }
        if (!note.getUserId().equals(getUserId())) {
            throw new EduException(EduErrorCode.NOTE_NOT_FOUND);
        }
        note.setNoteContent(noteContent);
        questionNoteMapper.updateById(note);
    }

    @Override
    public List<NoteRespVO> getNoteForQuestion(Long questionId) {
        Long userId = getUserId();
        List<EduQuestionNote> notes = questionNoteMapper.selectByUserAndQuestion(userId, questionId);
        List<NoteRespVO> result = new ArrayList<>();
        for (EduQuestionNote n : notes) {
            EduQuestion q = eduQuestionMapper.selectById(n.getQuestionId());
            result.add(NoteRespVO.builder().id(n.getId()).questionId(n.getQuestionId())
                    .courseId(n.getCourseId()).noteContent(n.getNoteContent())
                    .questionType(q != null ? q.getQuestionType() : null)
                    .questionContent(q != null ? q.getContent() : null)
                    .createTime(n.getCreateTime()).build());
        }
        return result;
    }

    @Override
    public List<PracticeQuestionSimpleVO> getHighFreqWrong(Long courseId) {
        Long userId = getUserId();
        List<Long> questionIds = practiceRecordMapper.selectHighFreqWrong(userId, courseId, 50);
        List<PracticeQuestionSimpleVO> result = new ArrayList<>();
        for (Long qid : questionIds) {
            EduQuestion q = eduQuestionMapper.selectById(qid);
            if (q == null) continue;
            result.add(PracticeQuestionSimpleVO.builder().id(q.getId()).questionType(q.getQuestionType())
                    .content(q.getContent()).difficulty(q.getDifficulty()).courseId(courseId).wrongCount(1)
                    .chapterName(getChapterName(q.getChapterId())).build());
        }
        return result;
    }

    @Override
    public PracticeSessionRespVO continuePractice(Long courseId) {
        Long userId = getUserId();
        EduPracticeSession session = practiceSessionMapper.selectByUserAndCourse(userId, courseId);
        if (session == null) {
            return PracticeSessionRespVO.builder().questionId(null).chapterId(null)
                    .practiceMode(1).currentIndex(0)
                    .totalCount(eduQuestionMapper.countByCourseId(courseId)).build();
        }
        List<Long> questionIds = (session.getPracticeMode() == 1 && session.getChapterId() != null)
                ? eduQuestionMapper.selectQuestionIdsByChapterId(session.getChapterId())
                : eduQuestionMapper.selectQuestionIdsByCourseId(courseId);
        int currentIndex = (session.getQuestionId() != null)
                ? Math.max(0, questionIds.indexOf(session.getQuestionId())) : 0;
        return PracticeSessionRespVO.builder().questionId(session.getQuestionId())
                .chapterId(session.getChapterId()).practiceMode(session.getPracticeMode())
                .currentIndex(currentIndex).totalCount(questionIds.size()).build();
    }

    @Override
    public List<CoursePracticeOverviewRespVO> getAllCoursesPracticeOverview() {
        Long userId = getUserId();
        // 获取用户所有课程
        MyCoursePageQueryDTO queryDTO = MyCoursePageQueryDTO.builder()
                .studentId(userId)
                .build();
        List<MyCourseVO> courses = eduCourseMapper.myCourse(queryDTO);

        // 构建每个课程的练习概览
        List<CoursePracticeOverviewRespVO> result = new ArrayList<>();
        for (MyCourseVO course : courses) {
            PracticeOverviewRespVO overview = getPracticeOverview(course.getId());
            result.add(CoursePracticeOverviewRespVO.builder()
                    .courseId(course.getId())
                    .courseName(course.getName())
                    .overview(overview)
                    .build());
        }
        return result;
    }
}