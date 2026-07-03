package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduChapterVideo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 章节视频 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 章节视频数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduChapterVideoMapper {

    /**
     * 根据章节ID查询视频列表
     *
     * @param chapterId 章节ID
     * @return 视频列表
     */
    List<EduChapterVideo> selectByChapterId(@Param("chapterId") Long chapterId);

    /**
     * 根据章节ID列表批量查询视频列表
     *
     * @param chapterIds 章节ID列表
     * @return 视频列表
     */
    List<EduChapterVideo> selectByChapterIds(@Param("chapterIds") List<Long> chapterIds);

    /**
     * 批量插入章节视频
     *
     * @param list 视频列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<EduChapterVideo> list);

    /**
     * 根据章节ID逻辑删除视频
     *
     * @param chapterId 章节ID
     * @return 影响行数
     */
    int deleteByChapterId(@Param("chapterId") Long chapterId);
}
