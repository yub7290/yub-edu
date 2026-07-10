package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.NewsCategoryCreateReqDTO;
import com.yub.edu.biz.dto.NewsCategoryUpdateReqDTO;
import com.yub.edu.biz.vo.NewsCategoryVO;

import java.util.List;

/**
 * 新闻资讯分类服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯分类服务
 * @Version: 1.0.0
 */
public interface EduNewsCategoryService {

    /**
     * 查询全部分类（管理端下拉与列表）
     */
    List<NewsCategoryVO> list();

    /**
     * 新增分类
     */
    Long create(NewsCategoryCreateReqDTO dto);

    /**
     * 编辑分类
     */
    void update(NewsCategoryUpdateReqDTO dto);

    /**
     * 删除分类（存在资讯时拒绝）
     */
    void delete(Long id);
}
