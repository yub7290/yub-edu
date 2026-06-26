package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduPracticeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 练习记录 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 练习记录数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduPracticeRecordMapper {

    /**
     * 新增练习记录
     *
     * @param record 练习记录
     * @return 影响行数
     */
    int insert(EduPracticeRecord record);

    /**
     * 根据用户和课程查询练习记录
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 练习记录列表
     */
    List<EduPracticeRecord> selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 根据用户和章节查询练习记录
     *
     * @param userId    用户ID
     * @param chapterId 章节ID
     * @return 练习记录列表
     */
    List<EduPracticeRecord> selectByUserAndChapter(@Param("userId") Long userId, @Param("chapterId") Long chapterId);

    /**
     * 根据ID查询练习记录
     *
     * @param id 记录ID
     * @return 练习记录
     */
    EduPracticeRecord selectById(@Param("id") Long id);

    /**
     * 统计用户在某课程下的练习次数
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 练习次数
     */
    int countByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 统计用户在某课程下的正确答题数
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 正确答题数
     */
    int countCorrectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 统计用户在某章节练习的不同试题数
     *
     * @param userId    用户ID
     * @param chapterId 章节ID
     * @return 不同试题数
     */
    int countDistinctByUserAndChapter(@Param("userId") Long userId, @Param("chapterId") Long chapterId);

    /**
     * 统计用户在某章节下的练习次数
     *
     * @param userId    用户ID
     * @param chapterId 章节ID
     * @return 练习次数
     */
    int countByUserAndChapter(@Param("userId") Long userId, @Param("chapterId") Long chapterId);

    /**
     * 统计用户在某章节下的正确答题数
     *
     * @param userId    用户ID
     * @param chapterId 章节ID
     * @return 正确答题数
     */
    int countCorrectByUserAndChapter(@Param("userId") Long userId, @Param("chapterId") Long chapterId);

    /**
     * 统计用户对某道试题的练习次数
     *
     * @param userId     用户ID
     * @param questionId 试题ID
     * @return 练习次数
     */
    int countByUserQuestion(@Param("userId") Long userId, @Param("questionId") Long questionId);

    /**
     * 查询用户在某课程下的错题列表
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 错题练习记录列表
     */
    List<EduPracticeRecord> selectWrongByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 查询用户在某章节练习过的不同试题ID列表
     *
     * @param userId    用户ID
     * @param chapterId 章节ID
     * @return 试题ID列表
     */
    List<Long> selectDistinctQuestionIdsByUserAndChapter(@Param("userId") Long userId, @Param("chapterId") Long chapterId);

    /**
     * 查询用户在某课程下的高频错题ID列表
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @param limit    限制数量
     * @return 高频错题ID列表
     */
    List<Long> selectHighFreqWrong(@Param("userId") Long userId, @Param("courseId") Long courseId, @Param("limit") Integer limit);
}
