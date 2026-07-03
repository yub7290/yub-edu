package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduStudyCardCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习卡关联课程 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡关联课程数据访问
 * @Version: 1.0.0
 */
@Mapper
public interface EduStudyCardCourseMapper {

    /**
     * 根据学习卡ID查询关联课程ID列表
     *
     * @param cardId 学习卡模板ID
     * @return 课程ID列表
     */
    List<Long> selectCourseIdsByCardId(@Param("cardId") Long cardId);

    /**
     * 批量插入关联关系
     *
     * @param list 关联关系列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<EduStudyCardCourse> list);

    /**
     * 根据学习卡ID删除关联关系
     *
     * @param cardId 学习卡模板ID
     * @return 影响行数
     */
    int deleteByCardId(@Param("cardId") Long cardId);
}
