package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduQuestionFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试题收藏 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 试题收藏数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduQuestionFavoriteMapper {

    /**
     * 新增试题收藏
     *
     * @param favorite 试题收藏
     * @return 影响行数
     */
    int insert(EduQuestionFavorite favorite);

    /**
     * 删除试题收藏
     *
     * @param userId     用户ID
     * @param questionId 试题ID
     * @return 影响行数
     */
    int deleteByUserAndQuestion(@Param("userId") Long userId, @Param("questionId") Long questionId);

    /**
     * 根据用户和试题查询收藏记录
     *
     * @param userId     用户ID
     * @param questionId 试题ID
     * @return 试题收藏
     */
    EduQuestionFavorite selectByUserAndQuestion(@Param("userId") Long userId, @Param("questionId") Long questionId);

    /**
     * 查询用户在某课程下的试题收藏列表
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 试题收藏列表
     */
    List<EduQuestionFavorite> selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 统计用户在某课程下的收藏数量
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 收藏数量
     */
    int countByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
