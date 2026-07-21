package com.yub.edu.biz.service;

import com.yub.common.model.PageResult;
import com.yub.edu.biz.entity.EduShareContent;
import com.yub.edu.biz.vo.ShareContentRespVO;

/**
 * 分享内容服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-20
 * @Description: 分享内容管理服务
 * @Version: 1.0.0
 */
public interface EduShareContentService {

    Long create(EduShareContent content);

    void update(Long id, EduShareContent content);

    void delete(Long id);

    EduShareContent getById(Long id);

    ShareContentRespVO getActiveContent();

    PageResult<EduShareContent> page(String title, Integer status, int pageNum, int pageSize);
}