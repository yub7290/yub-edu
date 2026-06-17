package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.KnowledgePointCreateReqDTO;
import com.yub.edu.biz.dto.KnowledgePointQueryDTO;
import com.yub.edu.biz.dto.KnowledgePointUpdateReqDTO;
import com.yub.edu.biz.entity.EduKnowledgePoint;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduKnowledgePointMapper;
import com.yub.edu.biz.service.EduKnowledgePointService;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 知识点服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduKnowledgePointServiceImpl implements EduKnowledgePointService {

    private final EduKnowledgePointMapper mapper;

    @Override
    public PageResult<EduKnowledgePoint> page(PageQuery<KnowledgePointQueryDTO> pageQuery) {
        KnowledgePointQueryDTO q = pageQuery.getQueryParam();
        com.yub.common.model.PageParam p = pageQuery.getPageParam();
        PageHelper.startPage(p.getPageNum(), p.getPageSize());
        List<EduKnowledgePoint> list = mapper.selectPage(q);
        PageInfo<EduKnowledgePoint> info = new PageInfo<>(list);
        return PageResult.of(info.getList(), info.getTotal());
    }

    @Override
    public EduKnowledgePoint getDetail(Long id) {
        EduKnowledgePoint pt = mapper.selectById(id);
        if (pt == null) throw new EduException(EduErrorCode.KNOWLEDGE_POINT_NOT_FOUND);
        return pt;
    }

    @Override
    public List<EduKnowledgePoint> listByCategoryId(Long categoryId) {
        return mapper.selectByCategoryId(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(KnowledgePointCreateReqDTO dto) {
        EduKnowledgePoint pt = new EduKnowledgePoint();
        pt.setCategoryId(dto.getCategoryId());
        pt.setTitle(dto.getTitle());
        pt.setContent(dto.getContent());
        pt.setStatus(dto.getStatus());
        Long uid = SecurityUtils.getCurrentUserId();
        pt.setCreateBy(uid);
        pt.setUpdateBy(uid);
        mapper.insert(pt);
        return pt.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KnowledgePointUpdateReqDTO dto) {
        EduKnowledgePoint pt = mapper.selectById(dto.getId());
        if (pt == null) throw new EduException(EduErrorCode.KNOWLEDGE_POINT_NOT_FOUND);
        pt.setCategoryId(dto.getCategoryId());
        pt.setTitle(dto.getTitle());
        pt.setContent(dto.getContent());
        pt.setStatus(dto.getStatus());
        pt.setUpdateBy(SecurityUtils.getCurrentUserId());
        mapper.updateById(pt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduKnowledgePoint pt = mapper.selectById(id);
        if (pt == null) throw new EduException(EduErrorCode.KNOWLEDGE_POINT_NOT_FOUND);
        mapper.deleteById(id);
    }
}
