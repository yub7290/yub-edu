package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduChapterKnowledgePoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 章节关联知识点 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 章节关联知识点数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduChapterKnowledgePointMapper {

    /**
     * 批量插入章节知识点关联
     */
    int batchInsert(@Param("list") List<EduChapterKnowledgePoint> list);

    /**
     * 根据章节ID删除所有关联
     */
    int deleteByChapterId(@Param("chapterId") Long chapterId);

    /**
     * 根据章节ID查询知识点ID列表
     */
    List<Long> selectKnowledgePointIdsByChapterId(@Param("chapterId") Long chapterId);

    /**
     * 根据多个章节ID批量查询知识点ID列表（用于N+1优化）
     *
     * @param chapterIds 章节ID集合
     * @return 关联记录
     */
    List<EduChapterKnowledgePoint> selectByChapterIds(@Param("chapterIds") List<Long> chapterIds);
}
