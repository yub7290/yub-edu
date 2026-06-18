package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduAiConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * AI助教会话 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: AI助教会话数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduAiConversationMapper {

    /**
     * 根据ID查询会话
     *
     * @param id 会话ID
     * @return 会话信息
     */
    EduAiConversation selectById(@Param("id") Long id);

    /**
     * 新增会话
     *
     * @param conversation 会话信息
     * @return 影响行数
     */
    int insert(EduAiConversation conversation);
}
