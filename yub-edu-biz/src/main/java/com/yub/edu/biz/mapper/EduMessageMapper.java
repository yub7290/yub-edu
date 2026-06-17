package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.MessageQueryDTO;
import com.yub.edu.biz.entity.EduMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 留言 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 */
@Mapper
public interface EduMessageMapper {

    List<EduMessage> selectPage(@Param("query") MessageQueryDTO query);

    EduMessage selectById(@Param("id") Long id);

    int insert(EduMessage message);

    int deleteById(@Param("id") Long id);
}
