package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.MajorQueryDTO;
import com.yub.edu.biz.entity.EduMajor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 专业 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 专业数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduMajorMapper {

    /**
     * 查询所有专业（管理端用）
     *
     * @return 专业列表
     */
    List<EduMajor> selectTree();

    /**
     * 按条件查询专业列表
     *
     * @param query 查询参数
     * @return 专业列表
     */
    List<EduMajor> selectList(@Param("query") MajorQueryDTO query);

    /**
     * 根据ID查询专业
     *
     * @param id 专业ID
     * @return 专业
     */
    EduMajor selectById(@Param("id") Long id);

    /**
     * 根据名称和父ID查询专业（唯一性校验）
     *
     * @param name     专业名称
     * @param parentId 父专业ID
     * @return 专业
     */
    EduMajor selectByNameAndParentId(@Param("name") String name, @Param("parentId") Long parentId);

    /**
     * 查询子专业数量
     *
     * @param parentId 父专业ID
     * @return 子专业数量
     */
    int countByParentId(@Param("parentId") Long parentId);

    /**
     * 查询专业下的课程数量
     *
     * @param majorId 专业ID
     * @return 课程数量
     */
    int countCoursesByMajorId(@Param("majorId") Long majorId);

    /**
     * 查询专业下的试题数量
     *
     * @param majorId 专业ID
     * @return 试题数量
     */
    int countQuestionsByMajorId(@Param("majorId") Long majorId);

    /**
     * 查询专业下的试卷数量
     *
     * @param majorId 专业ID
     * @return 试卷数量
     */
    int countExamsByMajorId(@Param("majorId") Long majorId);

    /**
     * 新增专业
     *
     * @param major 专业
     * @return 影响行数
     */
    int insert(EduMajor major);

    /**
     * 更新专业
     *
     * @param major 专业
     * @return 影响行数
     */
    int updateById(EduMajor major);

    /**
     * 逻辑删除专业
     *
     * @param id 专业ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 恢复专业
     *
     * @param id 专业ID
     * @return 影响行数
     */
    int restoreById(@Param("id") Long id);

    /**
     * 更新专业状态
     *
     * @param id     专业ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新推荐状态
     *
     * @param id          专业ID
     * @param recommended 推荐状态
     * @return 影响行数
     */
    int updateRecommended(@Param("id") Long id, @Param("recommended") Integer recommended);

    /**
     * 查询回收站列表
     *
     * @return 已删除的专业列表
     */
    List<EduMajor> selectRecycleList();

    /**
     * 物理删除专业（回收站用）
     *
     * @param id 专业ID
     * @return 影响行数
     */
    int deletePhysicallyById(@Param("id") Long id);
}
