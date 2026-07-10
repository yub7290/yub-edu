package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduNewsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 新闻资讯分类 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯分类数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduNewsCategoryMapper {

    /**
     * 查询全部分类（按排序、ID 升序）
     */
    List<EduNewsCategory> selectList();

    /**
     * 根据ID查询分类
     */
    EduNewsCategory selectById(@Param("id") Long id);

    /**
     * 新增分类
     */
    int insert(EduNewsCategory category);

    /**
     * 更新分类
     */
    int updateById(EduNewsCategory category);

    /**
     * 逻辑删除分类
     */
    int deleteById(@Param("id") Long id);

    /**
     * 统计同名分类数（排除指定ID，用于重名校验）
     */
    int countByName(@Param("name") String name, @Param("excludeId") Long excludeId);
}
