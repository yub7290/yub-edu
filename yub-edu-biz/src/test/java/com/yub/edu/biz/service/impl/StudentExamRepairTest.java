package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.yub.edu.biz.dto.*;
import com.yub.edu.biz.entity.*;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentExamRepairTest {
    @Mock EduExamMapper exams;
    @Mock EduExamQuestionMapper examQuestions;
    @Mock EduExamRecordMapper records;
    @Mock EduExamRecordDetailMapper details;
    @Mock EduChapterMapper chapters;
    @Mock StudyRecordMapper study;
    @Mock EduQuestionMapper questions;
    @Mock EduQuestionOptionMapper options;
    @Mock EduExamQuestionTypeConfigMapper types;
    @Mock EduExamChapterQuestionConfigMapper chapterQuestions;
    @InjectMocks StudentExamServiceImpl service;

    @BeforeEach void login() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("STUDENT:7", null, List.of()));
    }
    @AfterEach void clear() { SecurityContextHolder.clearContext(); PageHelper.clearPage(); }

    private EduExam exam(int finalExam) {
        EduExam exam = new EduExam(); exam.setId(999L); exam.setStatus(1); exam.setIsFinalExam(finalExam);
        exam.setCourseId(2L); exam.setMaxAttempts(3); return exam;
    }

    @Test void finalLegacySubmissionIsRejectedBeforeAnyWrite() {
        when(exams.selectById(999L)).thenReturn(exam(1));
        ExamSubmitReqDTO dto = new ExamSubmitReqDTO(); dto.setExamId(999L);
        assertThatThrownBy(() -> service.submit(dto)).isInstanceOfSatisfying(EduException.class,
                e -> assertThat(e.getCode()).isEqualTo(EduErrorCode.EXAM_START_REQUIRED.getCode()));
        verifyNoInteractions(records, details, questions);
    }

    @Test void ordinaryLegacySubmissionCannotExceedAttemptLimit() {
        when(exams.selectById(999L)).thenReturn(exam(0));
        when(records.selectSubmittedCount(7L, 999L)).thenReturn(3);
        ExamSubmitReqDTO dto = new ExamSubmitReqDTO(); dto.setExamId(999L);
        assertThatThrownBy(() -> service.submit(dto)).isInstanceOfSatisfying(EduException.class,
                e -> assertThat(e.getCode()).isEqualTo(EduErrorCode.EXAM_MAX_ATTEMPTS_REACHED.getCode()));
        verify(records, never()).insert(any()); verifyNoInteractions(details);
    }

    @Test void startChecksFinalProgressBeforeCreatingARecord() {
        EduExam exam = exam(1); exam.setChapterPassRate(80);
        when(exams.selectById(999L)).thenReturn(exam);
        when(chapters.countByCourseId(2L)).thenReturn(10);
        when(study.countStudiedChapters(7L, 2L)).thenReturn(7);
        assertThatThrownBy(() -> service.startExam(999L)).isInstanceOfSatisfying(EduException.class,
                e -> assertThat(e.getCode()).isEqualTo(EduErrorCode.EXAM_CHAPTER_PROGRESS_NOT_ENOUGH.getCode()));
        verify(records, never()).insert(any()); verifyNoInteractions(details);
    }

    @Test void alreadySubmittedRecordCannotBeReplayed() {
        EduExamRecord record = new EduExamRecord(); record.setId(5L); record.setUserId(7L); record.setStatus(1);
        when(records.selectById(5L)).thenReturn(record);
        ExamSubmitReqDTO dto = new ExamSubmitReqDTO(); dto.setRecordId(5L);
        assertThatThrownBy(() -> service.submit(dto)).isInstanceOfSatisfying(EduException.class,
                e -> assertThat(e.getCode()).isEqualTo(EduErrorCode.EXAM_ALREADY_SUBMITTED.getCode()));
        verifyNoInteractions(details);
    }

    @Test void arbitraryExamIdsUseCurrentUsersRealAttemptStatus() {
        when(exams.selectPage(any())).thenReturn(List.of(exam(1)));
        ExamAttemptStatusDTO state = new ExamAttemptStatusDTO(); state.setExamId(999L); state.setAttemptStatus(1);
        when(records.selectAttemptStatuses(7L, List.of(999L))).thenReturn(List.of(state));
        assertThat(service.list(null, null, 1, 10).getList().get(0).getAttemptStatus()).isEqualTo(1);
    }

    @Test void noAttemptDefaultsToUnansweredInsteadOfMappingExamId() {
        EduExam exam = exam(0); exam.setId(2L);
        when(exams.selectPage(any())).thenReturn(List.of(exam));
        when(records.selectAttemptStatuses(7L, List.of(2L))).thenReturn(List.of());
        assertThat(service.list(null, null, 1, 10).getList().get(0).getAttemptStatus()).isZero();
    }
}
