package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduChapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 章节 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 章节数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduChapterMapper {

    /**
     * 根据课程ID查询章节树
     *
     * @param courseId 课程ID
     * @return 章节列表
     */
    List<EduChapter> selectTreeByCourseId(@Param("courseId") Long courseId);

    /**
     * 根据ID查询章节
     *
     * @param id 章节ID
     * @return 章节
     */
    EduChapter selectById(@Param("id") Long id);

    /**
     * 查询子章节数量
     *
     * @param parentId 父章节ID
     * @return 子章节数量
     */
    int countByParentId(@Param("parentId") Long parentId);

    /**
     * 查询课程下的章节总数（仅统计叶子节点或启用状态的章节）
     *
     * @param courseId 课程ID
     * @return 章节总数
     */
    int countByCourseId(@Param("courseId") Long courseId);

    /**
     * 新增章节
     *
     * @param chapter 章节
     * @return 影响行数
     */
    int insert(EduChapter chapter);

    /**
     * 更新章节
     *
     * @param chapter 章节
     * @return 影响行数
     */
    int updateById(EduChapter chapter);

    /**
     * 逻辑删除章节
     *
     * @param id 章节ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询回收站列表
     *
     * @return 已删除的章节列表
     */
    List<EduChapter> selectRecycleList();

    /**
     * 物理删除章节
     *
     * @param id 章节ID
     * @return 影响行数
     */
    int deletePhysicallyById(@Param("id") Long id);
}
