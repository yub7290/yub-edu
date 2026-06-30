package com.yub.edu.biz.job;

import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.entity.EduExamRecord;
import com.yub.edu.biz.mapper.EduExamMapper;
import com.yub.edu.biz.mapper.EduExamRecordMapper;
import com.yub.edu.biz.service.StudentExamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考试超时自动交卷定时任务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-27
 * @Description: 每分钟扫描进行中且超时的考试记录，自动交卷
 * @Version: 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExamTimeoutAutoSubmitJob {

    private final EduExamRecordMapper eduExamRecordMapper;
    private final EduExamMapper eduExamMapper;
    private final StudentExamService studentExamService;

    /**
     * 每分钟执行一次，扫描超时的考试记录并自动提交
     */
    @Scheduled(fixedRate = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void autoSubmitTimeoutExams() {
        log.debug("开始扫描超时考试记录...");
        List<EduExamRecord> timeoutRecords = eduExamRecordMapper.selectTimeoutRecordsByDuration();
        if (timeoutRecords.isEmpty()) {
            log.debug("无超时考试记录");
            return;
        }
        log.info("发现 {} 条超时考试记录，开始自动交卷", timeoutRecords.size());
        for (EduExamRecord record : timeoutRecords) {
            try {
                EduExam exam = eduExamMapper.selectById(record.getExamId());
                if (exam == null) {
                    log.warn("超时记录关联的试卷不存在，跳过: recordId={}, examId={}", record.getId(), record.getExamId());
                    continue;
                }
                studentExamService.autoSubmitTimeoutRecord(record, exam);
                log.info("自动交卷成功: recordId={}, userId={}, examId={}", record.getId(), record.getUserId(), record.getExamId());
            } catch (Exception e) {
                log.error("自动交卷失败: recordId={}", record.getId(), e);
            }
        }
    }
}
