package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduPracticeSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 练习会话 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 练习会话数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduPracticeSessionMapper {

    /**
     * 新增练习会话
     *
     * @param session 练习会话
     * @return 影响行数
     */
    int insert(EduPracticeSession session);

    /**
     * 更新练习会话
     *
     * @param session 练习会话
     * @return 影响行数
     */
    int updateById(EduPracticeSession session);

    /**
     * 根据用户和课程查询练习会话
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 练习会话
     */
    EduPracticeSession selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 删除用户在某课程下的练习会话
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 影响行数
     */
    int deleteByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
