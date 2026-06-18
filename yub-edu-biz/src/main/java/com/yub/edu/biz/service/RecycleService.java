package com.yub.edu.biz.service;

import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.entity.EduQuestion;

import java.util.List;

/**
 * 回收站 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 回收站业务接口
 * @Version: 1.0.0
 */
public interface RecycleService {

    /** 专业回收站 */
    List<EduMajor> getMajorList();
    void restoreMajor(Long id);
    void deleteMajor(Long id);

    /** 课程回收站 */
    List<EduCourse> getCourseList();
    void restoreCourse(Long id);
    void deleteCourse(Long id);

    /** 试题回收站 */
    List<EduQuestion> getQuestionList();
    void restoreQuestion(Long id);
    void deleteQuestion(Long id);

    /** 试卷回收站 */
    List<EduExam> getExamList();
    void restoreExam(Long id);
    void deleteExam(Long id);
}
