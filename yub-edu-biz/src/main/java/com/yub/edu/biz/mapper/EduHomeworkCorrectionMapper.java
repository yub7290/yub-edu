package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduHomeworkCorrection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 作业批改记录 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业批改记录数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduHomeworkCorrectionMapper {

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 批改记录
     */
    EduHomeworkCorrection selectById(@Param("id") Long id);

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return 批改记录列表
     */
    List<EduHomeworkCorrection> selectPage(@Param("params") Map<String, Object> params);

    /**
     * 新增
     *
     * @param correction 批改记录
     * @return 影响行数
     */
    int insert(EduHomeworkCorrection correction);

    /**
     * 更新
     *
     * @param correction 批改记录
     * @return 影响行数
     */
    int updateById(EduHomeworkCorrection correction);

    /**
     * 逻辑删除
     *
     * @param id 主键ID
     * @return 影响行数
     */
    int softDeleteById(@Param("id") Long id);

    /**
     * 统计课程下学生的作业数
     *
     * @param courseId   课程ID
     * @param studentId 学生ID
     * @return 数量
     */
    int countByCourseStudent(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
}
