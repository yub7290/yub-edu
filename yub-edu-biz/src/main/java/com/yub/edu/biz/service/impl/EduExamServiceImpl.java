package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.ExamCreateReqDTO;
import com.yub.edu.biz.dto.ExamQueryDTO;
import com.yub.edu.biz.dto.ExamUpdateReqDTO;
import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.entity.EduExamChapterQuestionConfig;
import com.yub.edu.biz.entity.EduExamQuestionTypeConfig;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduExamChapterQuestionConfigMapper;
import com.yub.edu.biz.mapper.EduExamMapper;
import com.yub.edu.biz.mapper.EduExamQuestionTypeConfigMapper;
import com.yub.edu.biz.service.EduExamService;
import com.yub.edu.biz.vo.ExamDetailRespVO;
import com.yub.edu.biz.vo.ExamPageRespVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 试卷服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试卷管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduExamServiceImpl implements EduExamService {

    private final EduExamMapper eduExamMapper;
    private final EduExamQuestionTypeConfigMapper eduExamQuestionTypeConfigMapper;
    private final EduExamChapterQuestionConfigMapper eduExamChapterQuestionConfigMapper;

    @Override
    public PageResult<ExamPageRespVO> page(PageQuery<ExamQueryDTO> pageQuery) {
        ExamQueryDTO queryParam = pageQuery.getQueryParam();
        com.yub.common.model.PageParam pageParam = pageQuery.getPageParam();

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<EduExam> list = eduExamMapper.selectPage(queryParam);
        PageInfo<EduExam> pageInfo = new PageInfo<>(list);

        List<ExamPageRespVO> voList = pageInfo.getList().stream().map(exam -> {
            ExamPageRespVO vo = new ExamPageRespVO();
            vo.setId(exam.getId());
            vo.setTitle(exam.getTitle());
            vo.setMajorName(exam.getMajorName());
            vo.setCourseName(exam.getCourseName());
            vo.setTotalScore(exam.getTotalScore());
            vo.setDuration(exam.getDuration());
            vo.setIsFinalExam(exam.getIsFinalExam());
            vo.setStatus(exam.getStatus());
            vo.setDifficulty(exam.getDifficulty());
            vo.setExaminer(exam.getExaminer());
            vo.setCreateTime(exam.getCreateTime());
            return vo;
        }).toList();

        return PageResult.of(voList, pageInfo.getTotal());
    }

    @Override
    public ExamDetailRespVO getDetail(Long id) {
        EduExam exam = eduExamMapper.selectById(id);
        if (exam == null) {
            throw new EduException(EduErrorCode.EXAM_NOT_FOUND);
        }

        ExamDetailRespVO vo = new ExamDetailRespVO();
        vo.setId(exam.getId());
        vo.setTitle(exam.getTitle());
        vo.setSubtitle(exam.getSubtitle());
        vo.setMajorId(exam.getMajorId());
        vo.setCourseId(exam.getCourseId());
        vo.setMajorName(exam.getMajorName());
        vo.setCourseName(exam.getCourseName());
        vo.setIsFinalExam(exam.getIsFinalExam());
        vo.setStatus(exam.getStatus());
        vo.setRecommended(exam.getRecommended());
        vo.setDifficulty(exam.getDifficulty());
        vo.setDuration(exam.getDuration());
        vo.setTotalScore(exam.getTotalScore());
        vo.setPassScore(exam.getPassScore());
        vo.setIntroduction(exam.getIntroduction());
        vo.setNotes(exam.getNotes());
        vo.setExaminer(exam.getExaminer());
        vo.setLogo(exam.getLogo());
        vo.setQuestionRangeType(exam.getQuestionRangeType());
        vo.setCreateTime(exam.getCreateTime());
        vo.setUpdateTime(exam.getUpdateTime());

        // 加载类型配置
        List<EduExamQuestionTypeConfig> typeConfigs = eduExamQuestionTypeConfigMapper.selectByExamId(id);
        vo.setTypeConfigs(typeConfigs);

        // 加载章节配置
        List<EduExamChapterQuestionConfig> chapterConfigs = eduExamChapterQuestionConfigMapper.selectByExamId(id);
        vo.setChapterConfigs(chapterConfigs);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(ExamCreateReqDTO dto) {
        // 校验标题唯一性
        EduExam exist = eduExamMapper.selectByTitle(dto.getTitle());
        if (exist != null) {
            throw new EduException(EduErrorCode.EXAM_NAME_EXISTS);
        }

        EduExam exam = new EduExam();
        exam.setTitle(dto.getTitle());
        exam.setSubtitle(dto.getSubtitle());
        exam.setMajorId(dto.getMajorId());
        exam.setCourseId(dto.getCourseId());
        exam.setIsFinalExam(dto.getIsFinalExam() != null ? dto.getIsFinalExam() : 0);
        exam.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        exam.setRecommended(dto.getRecommended() != null ? dto.getRecommended() : 0);
        exam.setDifficulty(dto.getDifficulty() != null ? dto.getDifficulty() : 3);
        exam.setDuration(dto.getDuration() != null ? dto.getDuration() : 60);
        exam.setTotalScore(dto.getTotalScore() != null ? dto.getTotalScore() : 100);
        exam.setPassScore(dto.getPassScore() != null ? dto.getPassScore() : 60);
        exam.setIntroduction(dto.getIntroduction());
        exam.setNotes(dto.getNotes());
        exam.setExaminer(dto.getExaminer());
        exam.setLogo(dto.getLogo());
        exam.setQuestionRangeType(dto.getQuestionRangeType() != null ? dto.getQuestionRangeType() : 0);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        exam.setCreateBy(currentUserId);
        exam.setUpdateBy(currentUserId);
        eduExamMapper.insert(exam);

        Long examId = exam.getId();

        // 插入试题类型配置
        if (dto.getTypeConfigs() != null && !dto.getTypeConfigs().isEmpty()) {
            List<EduExamQuestionTypeConfig> typeConfigs = dto.getTypeConfigs().stream()
                    .map(item -> {
                        EduExamQuestionTypeConfig config = new EduExamQuestionTypeConfig();
                        config.setExamId(examId);
                        config.setQuestionType(item.getQuestionType());
                        config.setQuestionCount(item.getQuestionCount());
                        config.setScorePerQuestion(item.getScorePerQuestion());
                        return config;
                    }).toList();
            eduExamQuestionTypeConfigMapper.insertBatch(typeConfigs);
        }

        // 插入章节出题配置
        if (dto.getChapterConfigs() != null && !dto.getChapterConfigs().isEmpty()) {
            List<EduExamChapterQuestionConfig> chapterConfigs = dto.getChapterConfigs().stream()
                    .map(item -> {
                        EduExamChapterQuestionConfig config = new EduExamChapterQuestionConfig();
                        config.setExamId(examId);
                        config.setChapterId(item.getChapterId());
                        config.setQuestionType(item.getQuestionType());
                        config.setQuestionCount(item.getQuestionCount());
                        config.setScorePerQuestion(item.getScorePerQuestion());
                        return config;
                    }).toList();
            eduExamChapterQuestionConfigMapper.insertBatch(chapterConfigs);
        }

        return examId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExamUpdateReqDTO dto) {
        EduExam exam = eduExamMapper.selectById(dto.getId());
        if (exam == null) {
            throw new EduException(EduErrorCode.EXAM_NOT_FOUND);
        }

        // 校验标题唯一性（排除自身）
        EduExam exist = eduExamMapper.selectByTitle(dto.getTitle());
        if (exist != null && !exist.getId().equals(dto.getId())) {
            throw new EduException(EduErrorCode.EXAM_NAME_EXISTS);
        }

        exam.setTitle(dto.getTitle());
        exam.setSubtitle(dto.getSubtitle());
        exam.setMajorId(dto.getMajorId());
        exam.setCourseId(dto.getCourseId());
        exam.setIsFinalExam(dto.getIsFinalExam() != null ? dto.getIsFinalExam() : 0);
        exam.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        exam.setRecommended(dto.getRecommended() != null ? dto.getRecommended() : 0);
        exam.setDifficulty(dto.getDifficulty() != null ? dto.getDifficulty() : 3);
        exam.setDuration(dto.getDuration() != null ? dto.getDuration() : 60);
        exam.setTotalScore(dto.getTotalScore() != null ? dto.getTotalScore() : 100);
        exam.setPassScore(dto.getPassScore() != null ? dto.getPassScore() : 60);
        exam.setIntroduction(dto.getIntroduction());
        exam.setNotes(dto.getNotes());
        exam.setExaminer(dto.getExaminer());
        exam.setLogo(dto.getLogo());
        exam.setQuestionRangeType(dto.getQuestionRangeType() != null ? dto.getQuestionRangeType() : 0);
        exam.setUpdateBy(SecurityUtils.getCurrentUserId());
        eduExamMapper.updateById(exam);

        Long examId = dto.getId();

        // 试题类型配置：仅在传入新配置时才先删后插
        if (dto.getTypeConfigs() != null) {
            eduExamQuestionTypeConfigMapper.deleteByExamId(examId);
            if (!dto.getTypeConfigs().isEmpty()) {
                List<EduExamQuestionTypeConfig> typeConfigs = dto.getTypeConfigs().stream()
                        .map(item -> {
                            EduExamQuestionTypeConfig config = new EduExamQuestionTypeConfig();
                            config.setExamId(examId);
                            config.setQuestionType(item.getQuestionType());
                            config.setQuestionCount(item.getQuestionCount());
                            config.setScorePerQuestion(item.getScorePerQuestion());
                            return config;
                        }).toList();
                eduExamQuestionTypeConfigMapper.insertBatch(typeConfigs);
            }
        }

        // 章节出题配置：仅在传入新配置时才先删后插
        if (dto.getChapterConfigs() != null) {
            eduExamChapterQuestionConfigMapper.deleteByExamId(examId);
            if (!dto.getChapterConfigs().isEmpty()) {
                List<EduExamChapterQuestionConfig> chapterConfigs = dto.getChapterConfigs().stream()
                        .map(item -> {
                            EduExamChapterQuestionConfig config = new EduExamChapterQuestionConfig();
                            config.setExamId(examId);
                            config.setChapterId(item.getChapterId());
                            config.setQuestionType(item.getQuestionType());
                            config.setQuestionCount(item.getQuestionCount());
                            config.setScorePerQuestion(item.getScorePerQuestion());
                            return config;
                        }).toList();
                eduExamChapterQuestionConfigMapper.insertBatch(chapterConfigs);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduExam exam = eduExamMapper.selectById(id);
        if (exam == null) {
            throw new EduException(EduErrorCode.EXAM_NOT_FOUND);
        }
        // 级联清理关联配置
        eduExamQuestionTypeConfigMapper.deleteByExamId(id);
        eduExamChapterQuestionConfigMapper.deleteByExamId(id);
        eduExamMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        EduExam exam = eduExamMapper.selectById(id);
        if (exam == null) {
            throw new EduException(EduErrorCode.EXAM_NOT_FOUND);
        }
        eduExamMapper.updateStatus(id, status);
    }
}
