package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.StudyCardCreateReqDTO;
import com.yub.edu.biz.dto.StudyCardInstanceQueryDTO;
import com.yub.edu.biz.dto.StudyCardQueryDTO;
import com.yub.edu.biz.dto.StudyCardUpdateReqDTO;
import com.yub.edu.biz.dto.StudyCardUseReqDTO;
import com.yub.edu.biz.vo.StudyCardDetailRespVO;
import com.yub.edu.biz.vo.StudyCardInstancePageRespVO;
import com.yub.edu.biz.vo.StudyCardMyVO;
import com.yub.edu.biz.vo.StudyCardPageRespVO;
import com.yub.edu.biz.vo.StudyCardSummaryVO;

/**
 * 学习卡服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡管理服务
 * @Version: 1.0.0
 */
public interface EduStudyCardService {

    /**
     * 分页查询学习卡
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<StudyCardPageRespVO> page(PageQuery<StudyCardQueryDTO> pageQuery);

    /**
     * 获取学习卡详情
     *
     * @param id 学习卡ID
     * @return 学习卡详情
     */
    StudyCardDetailRespVO getDetail(Long id);

    /**
     * 新增学习卡
     *
     * @param dto 新增参数
     * @return 学习卡ID
     */
    Long create(StudyCardCreateReqDTO dto);

    /**
     * 编辑学习卡
     *
     * @param dto 编辑参数
     */
    void update(StudyCardUpdateReqDTO dto);

    /**
     * 删除学习卡
     *
     * @param id 学习卡ID
     */
    void delete(Long id);

    /**
     * 切换学习卡状态
     *
     * @param id     学习卡ID
     * @param status 状态
     */
    void changeStatus(Long id, Integer status);

    /**
     * 分页查询学习卡实例
     *
     * @param cardId    学习卡模板ID
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<StudyCardInstancePageRespVO> getInstancePage(Long cardId,
                                                            PageQuery<StudyCardInstanceQueryDTO> pageQuery);

    /**
     * 批量生成学习卡实例
     *
     * @param cardId 学习卡模板ID
     * @param count  生成数量
     */
    void batchGenerate(Long cardId, Integer count);

    /**
     * 回滚学习卡实例
     *
     * @param instanceId 实例ID
     */
    void rollbackInstance(Long instanceId);

    /**
     * 切换学习卡实例状态
     *
     * @param instanceId 实例ID
     * @param status     状态
     */
    void toggleInstanceStatus(Long instanceId, Integer status);

    /**
     * 使用学习卡
     *
     * @param dto 卡号参数
     */
    void useCard(StudyCardUseReqDTO dto);

    /**
     * 暂存学习卡
     *
     * @param dto 卡号参数
     */
    void saveCard(StudyCardUseReqDTO dto);

    /**
     * 查询我的学习卡（分页）
     *
     * @param pageQuery 分页查询参数
     * @return 学习卡分页列表
     */
    PageResult<StudyCardMyVO> getMyCards(PageQuery<?> pageQuery);

    /**
     * 查询学习卡汇总信息
     *
     * @return 汇总（总数、已使用数）
     */
    StudyCardSummaryVO getSummary();
}
