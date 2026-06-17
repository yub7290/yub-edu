package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.ExamQueryDTO;
import com.yub.edu.biz.entity.EduExam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试卷数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduExamMapper {

    /**
     * 根据ID查询试卷
     *
     * @param id 试卷ID
     * @return 试卷
     */
    EduExam selectById(@Param("id") Long id);

    /**
     * 新增试卷
     *
     * @param exam 试卷
     * @return 影响行数
     */
    int insert(EduExam exam);

    /**
     * 更新试卷
     *
     * @param exam 试卷
     * @return 影响行数
     */
    int updateById(EduExam exam);

    /**
     * 逻辑删除试卷
     *
     * @param id 试卷ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 恢复试卷
     *
     * @param id 试卷ID
     * @return 影响行数
     */
    int restoreById(@Param("id") Long id);

    /**
     * 查询回收站列表
     *
     * @return 已删除的试卷列表
     */
    List<EduExam> selectRecycleList();

    /**
     * 物理删除试卷
     *
     * @param id 试卷ID
     * @return 影响行数
     */
    int deletePhysicallyById(@Param("id") Long id);

    /**
     * 分页查询试卷列表
     *
     * @param query 查询参数
     * @return 试卷分页列表
     */
    List<EduExam> selectPage(@Param("query") ExamQueryDTO query);

    /**
     * 分页查询总记录数
     *
     * @param query 查询参数
     * @return 总记录数
     */
    long selectPageCount(@Param("query") ExamQueryDTO query);

    /**
     * 根据标题查询试卷（唯一性校验）
     *
     * @param title 试卷标题
     * @return 试卷
     */
    EduExam selectByTitle(@Param("title") String title);

    /**
     * 更新试卷状态
     *
     * @param id     试卷ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
