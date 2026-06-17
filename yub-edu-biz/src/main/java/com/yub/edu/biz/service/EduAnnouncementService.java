package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.AnnouncementCreateReqDTO;
import com.yub.edu.biz.dto.AnnouncementQueryDTO;
import com.yub.edu.biz.dto.AnnouncementUpdateReqDTO;
import com.yub.edu.biz.vo.AnnouncementVO;

/**
 * 公告服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 公告管理服务
 * @Version: 1.0.0
 */
public interface EduAnnouncementService {

    /**
     * 分页查询公告
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<AnnouncementVO> page(PageQuery<AnnouncementQueryDTO> pageQuery);

    /**
     * 获取公告详情
     *
     * @param id 公告ID
     * @return 公告详情
     */
    AnnouncementVO getDetail(Long id);

    /**
     * 新增公告
     *
     * @param dto 新增参数
     * @return 公告ID
     */
    Long create(AnnouncementCreateReqDTO dto);

    /**
     * 编辑公告
     *
     * @param dto 编辑参数
     */
    void update(AnnouncementUpdateReqDTO dto);

    /**
     * 删除公告
     *
     * @param id 公告ID
     */
    void delete(Long id);
}
