package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduNews;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 新闻资讯 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduNewsMapper {

    /**
     * 分页查询新闻（管理端）
     */
    List<com.yub.edu.biz.vo.NewsVO> selectPage(@Param("title") String title,
                                                 @Param("categoryId") Long categoryId,
                                                 @Param("status") Integer status);

    /**
     * 根据ID查询新闻
     */
    com.yub.edu.biz.vo.NewsVO selectById(@Param("id") Long id);

    /**
     * 新增新闻
     */
    int insert(EduNews news);

    /**
     * 更新新闻
     */
    int updateById(EduNews news);

    /**
     * 逻辑删除新闻
     */
    int deleteById(@Param("id") Long id);

    /**
     * 学员端新闻列表（仅返回已发布）
     */
    List<com.yub.edu.biz.vo.NewsVO> selectAppPage(@Param("categoryId") Long categoryId);

    /**
     * 阅读量 +1
     */
    int incrementViews(@Param("id") Long id);

    /**
     * 统计某分类下的新闻数（删除分类前校验）
     */
    int countByCategoryId(@Param("categoryId") Long categoryId);
}
