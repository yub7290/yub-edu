package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduStudyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 学习记录 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学习记录数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface StudyRecordMapper {

    /**
     * 查询学习记录（按学员+课程+章节）
     *
     * @param studentId 学员ID
     * @param courseId  课程ID
     * @param chapterId 章节ID
     * @return 学习记录
     */
    EduStudyRecord selectByStudentAndChapter(@Param("studentId") Long studentId,
                                              @Param("courseId") Long courseId,
                                              @Param("chapterId") Long chapterId);

    /**
     * 新增学习记录
     *
     * @param record 学习记录
     * @return 影响行数
     */
    int insert(EduStudyRecord record);

    /**
     * 更新学习记录
     *
     * @param record 学习记录
     * @return 影响行数
     */
    int updateById(EduStudyRecord record);

    /**
     * 查询学员总学习时长（秒）
     *
     * @param studentId 学员ID
     * @return 总学习时长
     */
    Integer selectTotalStudySecond(@Param("studentId") Long studentId);

    /**
     * 查询学员学习课程数
     *
     * @param studentId 学员ID
     * @return 课程数
     */
    Integer selectCourseCount(@Param("studentId") Long studentId);

    /**
     * 查询学员在某课程中已学习章节数
     *
     * @param studentId 学员ID
     * @param courseId  课程ID
     * @return 已学习章节数
     */
    int countStudiedChapters(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
