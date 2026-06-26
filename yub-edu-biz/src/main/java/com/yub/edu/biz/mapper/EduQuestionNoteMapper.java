package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduQuestionNote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试题笔记 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 试题笔记数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduQuestionNoteMapper {

    /**
     * 新增试题笔记
     *
     * @param note 试题笔记
     * @return 影响行数
     */
    int insert(EduQuestionNote note);

    /**
     * 更新试题笔记
     *
     * @param note 试题笔记
     * @return 影响行数
     */
    int updateById(EduQuestionNote note);

    /**
     * 删除试题笔记
     *
     * @param id 笔记ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据ID查询试题笔记
     *
     * @param id 笔记ID
     * @return 试题笔记
     */
    EduQuestionNote selectById(@Param("id") Long id);

    /**
     * 查询用户在某课程下的试题笔记列表
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 试题笔记列表
     */
    List<EduQuestionNote> selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 查询用户在某试题下的笔记列表
     *
     * @param userId     用户ID
     * @param questionId 试题ID
     * @return 试题笔记列表
     */
    List<EduQuestionNote> selectByUserAndQuestion(@Param("userId") Long userId, @Param("questionId") Long questionId);

    /**
     * 统计用户在某课程下的笔记数量
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 笔记数量
     */
    int countByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
