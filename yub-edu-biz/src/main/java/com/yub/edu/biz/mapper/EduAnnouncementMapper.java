package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduAnnouncement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 公告数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduAnnouncementMapper {

    /**
     * 分页查询公告
     *
     * @param courseId 课程ID
     * @param title    公告标题（模糊搜索）
     * @param status   状态
     * @return 公告列表
     */
    List<EduAnnouncement> selectPage(@Param("courseId") Long courseId,
                                     @Param("title") String title,
                                     @Param("status") Integer status);

    /**
     * 查询公告总数（分页）
     *
     * @param courseId 课程ID
     * @param title    公告标题（模糊搜索）
     * @param status   状态
     * @return 总数
     */
    int selectPageCount(@Param("courseId") Long courseId,
                        @Param("title") String title,
                        @Param("status") Integer status);

    /**
     * 查询某课程已启用的公告列表（按时间倒序）
     *
     * @param courseId 课程ID
     * @return 公告列表
     */
    List<EduAnnouncement> selectActiveByCourseId(@Param("courseId") Long courseId);

    /**
     * 根据ID查询已启用的公告（学生端详情用，同时校验 deleted=0 AND status=1）
     *
     * @param id 公告ID
     * @return 公告
     */
    EduAnnouncement selectActiveById(@Param("id") Long id);

    /**
     * 根据ID查询公告
     *
     * @param id 公告ID
     * @return 公告
     */
    EduAnnouncement selectById(@Param("id") Long id);

    /**
     * 新增公告
     *
     * @param announcement 公告
     * @return 影响行数
     */
    int insert(EduAnnouncement announcement);

    /**
     * 更新公告
     *
     * @param announcement 公告
     * @return 影响行数
     */
    int updateById(EduAnnouncement announcement);

    /**
     * 逻辑删除公告
     *
     * @param id 公告ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
