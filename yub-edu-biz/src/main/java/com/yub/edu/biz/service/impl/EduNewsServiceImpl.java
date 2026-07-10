package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageParam;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.NewsCreateReqDTO;
import com.yub.edu.biz.dto.NewsQueryDTO;
import com.yub.edu.biz.dto.NewsUpdateReqDTO;
import com.yub.edu.biz.entity.EduNews;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduNewsMapper;
import com.yub.edu.biz.service.EduNewsService;
import com.yub.edu.biz.vo.NewsVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新闻资讯服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduNewsServiceImpl implements EduNewsService {

    private final EduNewsMapper mapper;

    @Override
    public PageResult<NewsVO> page(PageQuery<NewsQueryDTO> pageQuery) {
        NewsQueryDTO queryParam = pageQuery.getQueryParam();
        PageParam pageParam = pageQuery.getPageParam();
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<NewsVO> list = mapper.selectPage(
                queryParam.getTitle(), queryParam.getCategoryId(), queryParam.getStatus());
        PageInfo<NewsVO> pageInfo = new PageInfo<>(list);
        return PageResult.of(fillTimeStr(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public NewsVO getDetail(Long id) {
        NewsVO vo = mapper.selectById(id);
        if (vo == null) {
            throw new EduException(EduErrorCode.NEWS_NOT_FOUND);
        }
        fillTimeStr(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(NewsCreateReqDTO dto) {
        EduNews entity = new EduNews();
        entity.setTitle(dto.getTitle());
        entity.setSummary(dto.getSummary());
        entity.setCoverUrl(dto.getCoverUrl());
        entity.setContent(dto.getContent());
        entity.setCategoryId(dto.getCategoryId());
        entity.setSource(dto.getSource());
        entity.setAuthor(dto.getAuthor());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        entity.setCreateBy(currentUserId);
        entity.setUpdateBy(currentUserId);
        mapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NewsUpdateReqDTO dto) {
        NewsVO exist = mapper.selectById(dto.getId());
        if (exist == null) {
            throw new EduException(EduErrorCode.NEWS_NOT_FOUND);
        }
        EduNews entity = new EduNews();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setSummary(dto.getSummary());
        entity.setCoverUrl(dto.getCoverUrl());
        entity.setContent(dto.getContent());
        entity.setCategoryId(dto.getCategoryId());
        entity.setSource(dto.getSource());
        entity.setAuthor(dto.getAuthor());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        entity.setUpdateBy(SecurityUtils.getCurrentUserId());
        mapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        NewsVO exist = mapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.NEWS_NOT_FOUND);
        }
        mapper.deleteById(id);
    }

    @Override
    public PageResult<NewsVO> appPage(Long categoryId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<NewsVO> list = mapper.selectAppPage(categoryId);
        PageInfo<NewsVO> pageInfo = new PageInfo<>(list);
        return PageResult.of(fillTimeStr(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NewsVO appDetail(Long id) {
        NewsVO vo = mapper.selectById(id);
        if (vo == null) {
            throw new EduException(EduErrorCode.NEWS_NOT_FOUND);
        }
        mapper.incrementViews(id);
        if (vo.getViews() == null) {
            vo.setViews(1);
        } else {
            vo.setViews(vo.getViews() + 1);
        }
        fillTimeStr(vo);
        return vo;
    }

    private List<NewsVO> fillTimeStr(List<NewsVO> list) {
        return list.stream().peek(this::fillTimeStr).collect(Collectors.toList());
    }

    private void fillTimeStr(NewsVO vo) {
        if (vo.getPublishTime() != null) {
            vo.setPublishTimeStr(vo.getPublishTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (vo.getCreateTime() != null) {
            vo.setCreateTimeStr(vo.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
}
