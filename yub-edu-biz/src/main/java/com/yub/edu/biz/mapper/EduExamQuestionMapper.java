package com.yub.edu.biz.mapper;

import com.yub.edu.biz.vo.ExamQuestionRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷题目关联 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 试卷题目关联数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduExamQuestionMapper {

    /**
     * 查询某试卷下的题目及选项（学生端，不含正确答案标记）
     *
     * @param examId 试卷ID
     * @return 题目列表
     */
    List<ExamQuestionRespVO> selectQuestionsByExamId(@Param("examId") Long examId);

    /**
     * 查询某试卷下的题目ID列表
     *
     * @param examId 试卷ID
     * @return 题目ID列表
     */
    List<Long> selectQuestionIdsByExamId(@Param("examId") Long examId);
}
