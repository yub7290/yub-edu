package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduAiMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI助教消息 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: AI助教消息数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduAiMessageMapper {

    /**
     * 查询会话的历史消息
     *
     * @param conversationId 会话ID
     * @param limit          返回条数
     * @return 消息列表
     */
    List<EduAiMessage> selectByConversationId(@Param("conversationId") Long conversationId,
                                               @Param("limit") Integer limit);

    /**
     * 查询学生某课程的最近会话消息
     *
     * @param studentId 学生ID
     * @param courseId  课程ID
     * @param limit     返回条数
     * @return 消息列表
     */
    List<EduAiMessage> selectRecentByStudentAndCourse(@Param("studentId") Long studentId,
                                                       @Param("courseId") Long courseId,
                                                       @Param("limit") Integer limit);

    /**
     * 新增消息
     *
     * @param message 消息信息
     * @return 影响行数
     */
    int insert(EduAiMessage message);

    /**
     * 统计学生今日对话次数
     *
     * @param studentId 学生ID
     * @param courseId  课程ID
     * @return 对话次数
     */
    int countTodayMessages(@Param("studentId") Long studentId,
                           @Param("courseId") Long courseId);
}
