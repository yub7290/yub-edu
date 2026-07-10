package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.NoticeCreateReqDTO;
import com.yub.edu.biz.dto.NoticeQueryDTO;
import com.yub.edu.biz.dto.NoticeUpdateReqDTO;
import com.yub.edu.biz.vo.NoticeStatsVO;
import com.yub.edu.biz.vo.NoticeVO;

/**
 * 课程通知服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 课程通知服务（通知与课程关联，仅发给绑定了该课程的学员）
 * @Version: 1.0.0
 */
public interface EduNoticeService {

    /**
     * 分页查询通知（管理端）
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<NoticeVO> page(PageQuery<NoticeQueryDTO> pageQuery);

    /**
     * 获取通知详情（管理端）
     *
     * @param id 通知ID
     * @return 通知详情
     */
    NoticeVO getDetail(Long id);

    /**
     * 新增通知
     *
     * @param dto 新增参数
     * @return 通知ID
     */
    Long create(NoticeCreateReqDTO dto);

    /**
     * 编辑通知
     *
     * @param dto 编辑参数
     */
    void update(NoticeUpdateReqDTO dto);

    /**
     * 删除通知（逻辑删除）
     *
     * @param id 通知ID
     */
    void delete(Long id);

    /**
     * 通知阅读统计（管理端）
     *
     * @param id 通知ID
     * @return 阅读统计
     */
    NoticeStatsVO stats(Long id);

    /**
     * 学员端通知列表（仅返回该学员所绑定课程下已发布的通知）
     *
     * @param studentId 学员ID
     * @param type      类型（可选）
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 分页结果（含是否已读标记）
     */
    PageResult<NoticeVO> appPage(Long studentId, Integer type, int pageNum, int pageSize);

    /**
     * 学员端通知详情（打开即标记已读）
     *
     * @param studentId 学员ID
     * @param id        通知ID
     * @return 通知详情（readFlag=1）
     */
    NoticeVO appDetail(Long studentId, Long id);

    /**
     * 学员端未读通知数
     *
     * @param studentId 学员ID
     * @return 未读数
     */
    int unreadCount(Long studentId);
}
