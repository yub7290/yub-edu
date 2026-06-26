package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.ExamSubmitReqDTO;
import com.yub.edu.biz.vo.ExamInfoRespVO;
import com.yub.edu.biz.vo.ExamListRespVO;
import com.yub.edu.biz.vo.ExamQuestionRespVO;
import com.yub.edu.biz.vo.ExamResultRespVO;

import java.util.List;

/**
 * 学生端考试服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生端在线测试服务
 * @Version: 1.0.0
 */
public interface StudentExamService {

    /**
     * 考试列表查询
     *
     * @param courseId 课程ID
     * @param keyword  关键词
     * @param page     页码
     * @param pageSize 每页条数
     * @return 考试列表
     */
    ExamListRespVO list(Long courseId, String keyword, Integer page, Integer pageSize);

    /**
     * 考试详情查询（含历史成绩）
     *
     * @param id 试卷ID
     * @return 考试详情
     */
    ExamInfoRespVO info(Long id);

    /**
     * 获取题目（不含正确答案）
     *
     * @param examId 试卷ID
     * @return 题目列表
     */
    List<ExamQuestionRespVO> questions(Long examId);

    /**
     * 提交考试
     *
     * @param dto 提交请求
     * @return 判分结果
     */
    ExamResultRespVO submit(ExamSubmitReqDTO dto);

    /**
     * 清空当前用户指定考试的历史成绩
     *
     * @param examId 试卷ID
     */
    void clearHistory(Long examId);
}
