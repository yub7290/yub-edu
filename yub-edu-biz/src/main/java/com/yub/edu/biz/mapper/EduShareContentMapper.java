package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduShareContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分享内容 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-20
 * @Description: 分享内容数据访问
 * @Version: 1.0.0
 */
@Mapper
public interface EduShareContentMapper {

    int insert(EduShareContent content);

    int updateById(EduShareContent content);

    int deleteById(@Param("id") Long id);

    EduShareContent selectById(@Param("id") Long id);

    EduShareContent selectActiveContent();

    List<EduShareContent> selectList(@Param("title") String title, @Param("status") Integer status);

    List<EduShareContent> selectPage(@Param("title") String title, @Param("status") Integer status,
                                      @Param("offset") int offset, @Param("limit") int limit);

    int count(@Param("title") String title, @Param("status") Integer status);
}