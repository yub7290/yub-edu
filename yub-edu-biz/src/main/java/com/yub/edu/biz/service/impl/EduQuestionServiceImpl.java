package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.QuestionCreateReqDTO;
import com.yub.edu.biz.dto.QuestionQueryDTO;
import com.yub.edu.biz.dto.QuestionUpdateReqDTO;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.entity.EduQuestion;
import com.yub.edu.biz.entity.EduQuestionOption;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduMajorMapper;
import com.yub.edu.biz.mapper.EduQuestionMapper;
import com.yub.edu.biz.mapper.EduQuestionOptionMapper;
import com.yub.edu.biz.service.EduQuestionService;
import com.yub.edu.biz.vo.QuestionDetailRespVO;
import com.yub.edu.biz.vo.QuestionPageRespVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 试题服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试题管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduQuestionServiceImpl implements EduQuestionService {

    private final EduQuestionMapper eduQuestionMapper;
    private final EduQuestionOptionMapper eduQuestionOptionMapper;
    private final EduCourseMapper eduCourseMapper;
    private final EduMajorMapper eduMajorMapper;

    @Override
    public PageResult<QuestionPageRespVO> page(PageQuery<QuestionQueryDTO> pageQuery) {
        QuestionQueryDTO queryParam = pageQuery.getQueryParam();
        com.yub.common.model.PageParam pageParam = pageQuery.getPageParam();

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<QuestionPageRespVO> list = eduQuestionMapper.selectPage(queryParam);
        PageInfo<QuestionPageRespVO> pageInfo = new PageInfo<>(list);

        return PageResult.of(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public QuestionDetailRespVO getDetail(Long id) {
        EduQuestion question = eduQuestionMapper.selectById(id);
        if (question == null) {
            throw new EduException(EduErrorCode.QUESTION_NOT_FOUND);
        }

        String majorName = null;
        if (question.getMajorId() != null) {
            EduMajor major = eduMajorMapper.selectById(question.getMajorId());
            if (major != null) majorName = major.getName();
        }

        String courseName = null;
        if (question.getCourseId() != null) {
            EduCourse course = eduCourseMapper.selectById(question.getCourseId());
            if (course != null) courseName = course.getName();
        }

        // 加载选项列表
        List<EduQuestionOption> options = null;
        if (question.getQuestionType() != null && (question.getQuestionType() <= 1 || question.getQuestionType() == 4)) {
            options = eduQuestionOptionMapper.selectByQuestionId(id);
        }

        return QuestionDetailRespVO.builder()
                .id(question.getId())
                .questionType(question.getQuestionType())
                .content(question.getContent())
                .difficulty(question.getDifficulty())
                .status(question.getStatus())
                .majorId(question.getMajorId())
                .majorName(majorName)
                .courseId(question.getCourseId())
                .courseName(courseName)
                .chapterId(question.getChapterId())
                .analysis(question.getAnalysis())
                .knowledgePoints(question.getKnowledgePoints())
                .answer(question.getAnswer())
                .options(options)
                .createTime(question.getCreateTime())
                .updateTime(question.getUpdateTime())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(QuestionCreateReqDTO dto) {
        EduQuestion question = new EduQuestion();
        question.setQuestionType(dto.getQuestionType());
        question.setContent(dto.getContent());
        question.setDifficulty(dto.getDifficulty());
        question.setStatus(dto.getStatus());
        question.setMajorId(dto.getMajorId());
        question.setCourseId(dto.getCourseId());
        question.setChapterId(dto.getChapterId());
        question.setAnalysis(dto.getAnalysis());
        question.setKnowledgePoints(dto.getKnowledgePoints());
        question.setAnswer(dto.getAnswer());
        Long userId = SecurityUtils.getCurrentUserId();
        question.setCreateBy(userId);
        question.setUpdateBy(userId);
        eduQuestionMapper.insert(question);

        // 插入选项
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            insertOptions(dto.getOptions(), question.getId());
        }

        return question.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(QuestionUpdateReqDTO dto) {
        EduQuestion question = eduQuestionMapper.selectById(dto.getId());
        if (question == null) {
            throw new EduException(EduErrorCode.QUESTION_NOT_FOUND);
        }

        question.setQuestionType(dto.getQuestionType());
        question.setContent(dto.getContent());
        question.setDifficulty(dto.getDifficulty());
        question.setStatus(dto.getStatus());
        question.setMajorId(dto.getMajorId());
        question.setCourseId(dto.getCourseId());
        question.setChapterId(dto.getChapterId());
        question.setAnalysis(dto.getAnalysis());
        question.setKnowledgePoints(dto.getKnowledgePoints());
        question.setAnswer(dto.getAnswer());
        question.setUpdateBy(SecurityUtils.getCurrentUserId());
        eduQuestionMapper.updateById(question);

        // 先删后插选项
        if (dto.getQuestionType() != null && (dto.getQuestionType() <= 1 || dto.getQuestionType() == 4)) {
            eduQuestionOptionMapper.deleteByQuestionId(dto.getId());
            if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
                insertOptions(dto.getOptions(), dto.getId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduQuestion question = eduQuestionMapper.selectById(id);
        if (question == null) {
            throw new EduException(EduErrorCode.QUESTION_NOT_FOUND);
        }
        eduQuestionMapper.deleteById(id);
        eduQuestionOptionMapper.deleteByQuestionId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        EduQuestion question = eduQuestionMapper.selectById(id);
        if (question == null) {
            throw new EduException(EduErrorCode.QUESTION_NOT_FOUND);
        }
        eduQuestionMapper.updateStatus(id, status);
    }

    /**
     * 批量插入选项
     */
    private void insertOptions(List<QuestionCreateReqDTO.QuestionOptionDTO> dtoOptions, Long questionId) {
        List<EduQuestionOption> options = new ArrayList<>();
        for (int i = 0; i < dtoOptions.size(); i++) {
            QuestionCreateReqDTO.QuestionOptionDTO dto = dtoOptions.get(i);
            EduQuestionOption opt = new EduQuestionOption();
            opt.setQuestionId(questionId);
            opt.setLabel(dto.getLabel());
            opt.setContent(dto.getContent());
            opt.setIsCorrect(dto.getIsCorrect() != null ? dto.getIsCorrect() : 0);
            opt.setSort(dto.getSort() != null ? dto.getSort() : i);
            options.add(opt);
        }
        if (!options.isEmpty()) {
            eduQuestionOptionMapper.insertBatch(options);
        }
    }
}
