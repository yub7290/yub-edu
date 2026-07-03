package com.yub.edu.biz.mapper;

import com.yub.edu.api.dto.app.MyCoursePageQueryDTO;
import com.yub.edu.api.vo.app.MyCourseVO;
import com.yub.edu.biz.dto.CourseQueryDTO;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.vo.CoursePageRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 课程数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduCourseMapper {

    /**
     * 分页查询课程
     *
     * @param query 查询参数
     * @return 课程分页列表
     */
    List<CoursePageRespVO> selectPage(@Param("query") CourseQueryDTO query);

    /**
     * 根据ID查询课程
     *
     * @param id 课程ID
     * @return 课程
     */
    EduCourse selectById(@Param("id") Long id);

    /**
     * 根据名称查询课程（唯一性校验）
     *
     * @param name 课程名称
     * @return 课程
     */
    EduCourse selectByName(@Param("name") String name);

    /**
     * 新增课程
     *
     * @param course 课程
     * @return 影响行数
     */
    int insert(EduCourse course);

    /**
     * 更新课程
     *
     * @param course 课程
     * @return 影响行数
     */
    int updateById(EduCourse course);

    /**
     * 逻辑删除课程
     *
     * @param id 课程ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 恢复课程
     *
     * @param id 课程ID
     * @return 影响行数
     */
    int restoreById(@Param("id") Long id);

    /**
     * 更新课程状态
     *
     * @param id     课程ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新推荐状态
     *
     * @param id          课程ID
     * @param recommended 推荐状态
     * @return 影响行数
     */
    int updateRecommended(@Param("id") Long id, @Param("recommended") Integer recommended);

    /**
     * 查询推荐课程列表
     *
     * @return 推荐课程列表
     */
    List<EduCourse> selectByRecommended();

    /**
     * 查询学生端课程列表（分页用，返回全部字段）
     *
     * @param params 查询参数（cateId, tabType, keyword）
     * @return 课程列表
     */
    List<EduCourse> selectStudentList(@Param("cateId") Long cateId, @Param("tabType") Integer tabType, @Param("keyword") String keyword);

    /**
     * 查询回收站列表
     *
     * @return 已删除的课程列表
     */
    List<EduCourse> selectRecycleList();

    /**
     * 物理删除课程
     *
     * @param id 课程ID
     * @return 影响行数
     */
    int deletePhysicallyById(@Param("id") Long id);

    /**
     * 我的课程
     * @param queryParam 查询参数
     * @return 查询结果
     */
    List<MyCourseVO> myCourse(@Param("queryParam") MyCoursePageQueryDTO queryParam);
}
