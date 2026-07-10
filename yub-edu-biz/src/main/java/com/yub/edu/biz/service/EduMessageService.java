package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.MessageQueryDTO;
import com.yub.edu.biz.entity.EduMessage;

import java.util.List;

/**
 * 留言服务
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 留言管理服务
 * @Version: 1.0.0
 */
public interface EduMessageService {

    /**
     * 分页查询留言
     *
     * @param query 查询参数
     * @return 留言列表
     */
    List<EduMessage> selectPage(MessageQueryDTO query);

    /**
     * 根据ID查询留言
     *
     * @param id 留言ID
     * @return 留言
     */
    EduMessage selectById(Long id);

    /**
     * 删除留言
     *
     * @param id 留言ID
     * @return 影响行数
     */
    int deleteById(Long id);
}
