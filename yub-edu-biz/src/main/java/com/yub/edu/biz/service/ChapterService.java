package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.ChapterCreateReqDTO;
import com.yub.edu.biz.dto.ChapterUpdateReqDTO;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduChapterKnowledgePoint;

import java.util.List;

/**
 * 章节管理 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 章节业务接口
 * @Version: 1.0.0
 */
public interface ChapterService {

    /**
     * 获取课程的章节树
     *
     * @param courseId 课程ID
     * @return 章节树
     */
    List<EduChapter> getTree(Long courseId);

    /**
     * 获取章节详情
     *
     * @param id 章节ID
     * @return 章节详情
     */
    EduChapter getDetail(Long id);

    /**
     * 新增章节
     *
     * @param dto 创建参数
     * @return 章节ID
     */
    Long create(ChapterCreateReqDTO dto);

    /**
     * 编辑章节
     *
     * @param dto 编辑参数
     */
    void update(ChapterUpdateReqDTO dto);

    /**
     * 删除章节
     *
     * @param id 章节ID
     */
    void delete(Long id);

    /**
     * 获取章节关联的知识点ID列表
     *
     * @param chapterId 章节ID
     * @return 知识点ID列表
     */
    List<Long> getKnowledgePointIds(Long chapterId);

    /**
     * 更新章节关联的知识点
     *
     * @param chapterId       章节ID
     * @param knowledgePointIds 知识点ID列表
     */
    void updateKnowledgePoints(Long chapterId, List<Long> knowledgePointIds);

    /**
     * 根据多个章节ID批量查询章节知识点关联
     *
     * @param chapterIds 章节ID集合
     * @return 关联记录列表
     */
    List<EduChapterKnowledgePoint> selectByChapterIds(List<Long> chapterIds);

    /**
     * 查询课程的扁平章节列表（不含树形嵌套）
     *
     * @param courseId 课程ID
     * @return 扁平章节列表
     */
    List<EduChapter> selectFlatByCourseId(Long courseId);
}
