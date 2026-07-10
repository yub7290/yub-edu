package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduNotice;
import com.yub.edu.biz.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程通知 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 课程通知数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduNoticeMapper {

    /**
     * 分页查询通知（管理端）
     *
     * @param courseId  课程ID
     * @param title     标题（模糊搜索）
     * @param status    状态
     * @param type      类型
     * @param teacherId 教师ID（教师端过滤）
     * @return 通知列表
     */
    List<NoticeVO> selectPage(@Param("courseId") Long courseId,
                               @Param("title") String title,
                               @Param("status") Integer status,
                               @Param("type") Integer type,
                               @Param("teacherId") Long teacherId);

    /**
     * 根据ID查询通知
     *
     * @param id 通知ID
     * @return 通知
     */
    NoticeVO selectById(@Param("id") Long id);

    /**
     * 新增通知
     *
     * @param notice 通知
     * @return 影响行数
     */
    int insert(EduNotice notice);

    /**
     * 更新通知
     *
     * @param notice 通知
     * @return 影响行数
     */
    int updateById(EduNotice notice);

    /**
     * 逻辑删除通知
     *
     * @param id 通知ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 学员端通知列表（仅返回该学员所绑定课程下已发布的通知，并标记是否已读）
     *
     * @param studentId 学员ID
     * @param type      类型（可选）
     * @return 通知列表
     */
    List<NoticeVO> selectStudentPage(@Param("studentId") Long studentId,
                                      @Param("type") Integer type);

    /**
     * 学员端未读通知数（已发布 + 绑定课程 + 未阅读）
     *
     * @param studentId 学员ID
     * @return 未读数
     */
    int countStudentUnread(@Param("studentId") Long studentId);

    /**
     * 标记通知已读（已读则忽略，依赖唯一索引 uk_notice_student）
     *
     * @param noticeId  通知ID
     * @param studentId 学员ID
     * @return 影响行数
     */
    int markRead(@Param("noticeId") Long noticeId,
                 @Param("studentId") Long studentId);

    /**
     * 统计某课程的接收学员总数（绑定该课程的学员组内的去重学员数）
     *
     * @param courseId 课程ID
     * @return 接收人数
     */
    int countReceivers(@Param("courseId") Long courseId);

    /**
     * 统计某通知已读人数
     *
     * @param noticeId 通知ID
     * @return 已读数
     */
    int countRead(@Param("noticeId") Long noticeId);
}
