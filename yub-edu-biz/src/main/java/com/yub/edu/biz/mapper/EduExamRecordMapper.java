package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduExamRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试记录 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 考试记录数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduExamRecordMapper {

    /**
     * 插入考试记录
     *
     * @param record 考试记录
     * @return 影响行数
     */
    int insert(EduExamRecord record);

    /**
     * 根据ID查询考试记录
     *
     * @param id 记录ID
     * @return 考试记录
     */
    EduExamRecord selectById(@Param("id") Long id);

    /**
     * 根据用户ID和试卷ID查询历史考试记录
     *
     * @param userId 用户ID
     * @param examId 试卷ID
     * @return 历史记录列表
     */
    List<EduExamRecord> selectByUserAndExam(@Param("userId") Long userId, @Param("examId") Long examId);

    /**
     * 逻辑删除当前用户指定试卷的所有历史记录
     *
     * @param userId 用户ID
     * @param examId 试卷ID
     * @return 影响行数
     */
    int deleteByUserAndExam(@Param("userId") Long userId, @Param("examId") Long examId);
}
