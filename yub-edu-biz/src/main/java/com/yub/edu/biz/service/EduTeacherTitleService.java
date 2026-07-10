package com.yub.edu.biz.service;

import com.yub.edu.biz.entity.EduTeacherTitle;

import java.util.List;

/**
 * 教师职称 Service 接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 教师职称业务接口
 * @Version: 1.0.0
 */
public interface EduTeacherTitleService {

    /**
     * 批量查询职称（优化 N+1）
     *
     * @param ids 职称ID列表
     * @return 职称列表
     */
    List<EduTeacherTitle> selectBatchByIds(List<Long> ids);
}
