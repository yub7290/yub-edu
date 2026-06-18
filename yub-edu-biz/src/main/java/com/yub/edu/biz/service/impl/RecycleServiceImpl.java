package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.entity.EduQuestion;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduExamMapper;
import com.yub.edu.biz.mapper.EduMajorMapper;
import com.yub.edu.biz.mapper.EduQuestionMapper;
import com.yub.edu.biz.service.RecycleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 回收站 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 回收站业务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecycleServiceImpl implements RecycleService {

    private final EduMajorMapper eduMajorMapper;
    private final EduCourseMapper eduCourseMapper;
    private final EduQuestionMapper eduQuestionMapper;
    private final EduExamMapper eduExamMapper;

    // ========== 专业回收 ==========

    @Override
    public List<EduMajor> getMajorList() {
        return eduMajorMapper.selectRecycleList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreMajor(Long id) {
        EduMajor major = eduMajorMapper.selectById(id);
        if (major == null || major.getDeleted() == 0) {
            log.warn("专业不存在或未删除, id={}", id);
            return;
        }
        eduMajorMapper.restoreById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMajor(Long id) {
        eduMajorMapper.deletePhysicallyById(id);
    }

    // ========== 课程回收 ==========

    @Override
    public List<EduCourse> getCourseList() {
        return eduCourseMapper.selectRecycleList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreCourse(Long id) {
        EduCourse course = eduCourseMapper.selectById(id);
        if (course == null || course.getDeleted() == 0) {
            log.warn("课程不存在或未删除, id={}", id);
            return;
        }
        eduCourseMapper.restoreById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourse(Long id) {
        eduCourseMapper.deletePhysicallyById(id);
    }

    // ========== 试题回收 ==========

    @Override
    public List<EduQuestion> getQuestionList() {
        return eduQuestionMapper.selectRecycleList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreQuestion(Long id) {
        EduQuestion question = eduQuestionMapper.selectById(id);
        if (question == null || question.getDeleted() == 0) {
            log.warn("试题不存在或未删除, id={}", id);
            return;
        }
        eduQuestionMapper.restoreById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQuestion(Long id) {
        eduQuestionMapper.deletePhysicallyById(id);
    }

    // ========== 试卷回收 ==========

    @Override
    public List<EduExam> getExamList() {
        return eduExamMapper.selectRecycleList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreExam(Long id) {
        EduExam exam = eduExamMapper.selectById(id);
        if (exam == null || exam.getDeleted() == 0) {
            log.warn("试卷不存在或未删除, id={}", id);
            return;
        }
        eduExamMapper.restoreById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteExam(Long id) {
        eduExamMapper.deletePhysicallyById(id);
    }
}
