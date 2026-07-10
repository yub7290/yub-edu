package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduChapterAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 章节附件 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 章节附件数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduChapterAttachmentMapper {

    /**
     * 根据章节ID查询附件列表
     *
     * @param chapterId 章节ID
     * @return 附件列表
     */
    List<EduChapterAttachment> selectByChapterId(@Param("chapterId") Long chapterId);

    /**
     * 根据章节ID列表批量查询附件列表
     *
     * @param chapterIds 章节ID列表
     * @return 附件列表
     */
    List<EduChapterAttachment> selectByChapterIds(@Param("chapterIds") List<Long> chapterIds);

    /**
     * 批量插入章节附件
     *
     * @param list 附件列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<EduChapterAttachment> list);

    /**
     * 根据章节ID逻辑删除附件
     *
     * @param chapterId 章节ID
     * @return 影响行数
     */
    int deleteByChapterId(@Param("chapterId") Long chapterId);
}
