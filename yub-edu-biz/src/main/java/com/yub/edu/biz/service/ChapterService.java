package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.ChapterCreateReqDTO;
import com.yub.edu.biz.dto.ChapterUpdateReqDTO;
import com.yub.edu.biz.entity.EduChapter;

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
}
