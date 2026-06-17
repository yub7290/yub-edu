package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.AnnouncementCreateReqDTO;
import com.yub.edu.biz.dto.AnnouncementQueryDTO;
import com.yub.edu.biz.dto.AnnouncementUpdateReqDTO;
import com.yub.edu.biz.entity.EduAnnouncement;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduAnnouncementMapper;
import com.yub.edu.biz.service.EduAnnouncementService;
import com.yub.edu.biz.vo.AnnouncementVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公告服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 公告管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduAnnouncementServiceImpl implements EduAnnouncementService {

    private final EduAnnouncementMapper mapper;

    @Override
    public PageResult<AnnouncementVO> page(PageQuery<AnnouncementQueryDTO> pageQuery) {
        AnnouncementQueryDTO queryParam = pageQuery.getQueryParam();
        com.yub.common.model.PageParam pageParam = pageQuery.getPageParam();

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<EduAnnouncement> list = mapper.selectPage(
                queryParam.getCourseId(),
                queryParam.getTitle(),
                queryParam.getStatus()
        );
        PageInfo<EduAnnouncement> pageInfo = new PageInfo<>(list);

        List<AnnouncementVO> voList = pageInfo.getList().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, pageInfo.getTotal());
    }

    @Override
    public AnnouncementVO getDetail(Long id) {
        EduAnnouncement entity = mapper.selectById(id);
        if (entity == null) {
            throw new EduException(EduErrorCode.ANNOUNCEMENT_NOT_FOUND);
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(AnnouncementCreateReqDTO dto) {
        EduAnnouncement entity = new EduAnnouncement();
        entity.setCourseId(dto.getCourseId());
        entity.setTitle(dto.getTitle());
        entity.setLongTitle(dto.getLongTitle());
        entity.setCategory(dto.getCategory());
        entity.setSummary(dto.getSummary());
        entity.setSource(dto.getSource());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        entity.setContent(dto.getContent());
        Long currentUserId = SecurityUtils.getCurrentUserId();
        entity.setCreateBy(currentUserId);
        entity.setUpdateBy(currentUserId);
        mapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AnnouncementUpdateReqDTO dto) {
        EduAnnouncement entity = mapper.selectById(dto.getId());
        if (entity == null) {
            throw new EduException(EduErrorCode.ANNOUNCEMENT_NOT_FOUND);
        }
        entity.setCourseId(dto.getCourseId());
        entity.setTitle(dto.getTitle());
        entity.setLongTitle(dto.getLongTitle());
        entity.setCategory(dto.getCategory());
        entity.setSummary(dto.getSummary());
        entity.setSource(dto.getSource());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        entity.setContent(dto.getContent());
        entity.setUpdateBy(SecurityUtils.getCurrentUserId());
        mapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduAnnouncement entity = mapper.selectById(id);
        if (entity == null) {
            throw new EduException(EduErrorCode.ANNOUNCEMENT_NOT_FOUND);
        }
        mapper.deleteById(id);
    }

    private AnnouncementVO toVO(EduAnnouncement entity) {
        AnnouncementVO.AnnouncementVOBuilder builder = AnnouncementVO.builder()
                .id(entity.getId())
                .courseId(entity.getCourseId())
                .title(entity.getTitle())
                .longTitle(entity.getLongTitle())
                .category(entity.getCategory())
                .summary(entity.getSummary())
                .source(entity.getSource())
                .status(entity.getStatus())
                .content(entity.getContent())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .createBy(entity.getCreateBy())
                .updateBy(entity.getUpdateBy())
                .deleted(entity.getDeleted());

        if (entity.getCreateTime() != null) {
            builder.createTimeStr(entity.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        return builder.build();
    }
}
