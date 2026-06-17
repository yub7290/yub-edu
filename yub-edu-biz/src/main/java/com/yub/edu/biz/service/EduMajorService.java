package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.MajorCreateReqDTO;
import com.yub.edu.biz.dto.MajorQueryDTO;
import com.yub.edu.biz.dto.MajorUpdateReqDTO;
import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.vo.MajorDetailRespVO;

import java.util.List;

/**
 * 专业服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 专业管理服务
 * @Version: 1.0.0
 */
public interface EduMajorService {

    /**
     * 获取专业树
     *
     * @return 专业树
     */
    List<EduMajor> selectTree();

    /**
     * 按条件查询专业树
     *
     * @param query 查询参数
     * @return 专业树
     */
    List<EduMajor> selectTreeByCondition(MajorQueryDTO query);

    /**
     * 获取专业详情
     *
     * @param id 专业ID
     * @return 专业详情
     */
    MajorDetailRespVO getDetail(Long id);

    /**
     * 新增专业
     *
     * @param dto 新增参数
     * @return 专业ID
     */
    Long create(MajorCreateReqDTO dto);

    /**
     * 编辑专业
     *
     * @param dto 编辑参数
     */
    void update(MajorUpdateReqDTO dto);

    /**
     * 删除专业
     *
     * @param id 专业ID
     */
    void delete(Long id);

    /**
     * 切换专业状态
     *
     * @param id     专业ID
     * @param status 状态
     */
    void changeStatus(Long id, Integer status);

    /**
     * 切换推荐状态
     *
     * @param id          专业ID
     * @param recommended 推荐状态
     */
    void changeRecommended(Long id, Integer recommended);
}
