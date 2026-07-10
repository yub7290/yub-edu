package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.NewsCategoryCreateReqDTO;
import com.yub.edu.biz.dto.NewsCategoryUpdateReqDTO;
import com.yub.edu.biz.entity.EduNewsCategory;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduNewsCategoryMapper;
import com.yub.edu.biz.mapper.EduNewsMapper;
import com.yub.edu.biz.service.EduNewsCategoryService;
import com.yub.edu.biz.vo.NewsCategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新闻资讯分类服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯分类服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduNewsCategoryServiceImpl implements EduNewsCategoryService {

    private final EduNewsCategoryMapper categoryMapper;
    private final EduNewsMapper newsMapper;

    @Override
    public List<NewsCategoryVO> list() {
        return categoryMapper.selectList().stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(NewsCategoryCreateReqDTO dto) {
        if (categoryMapper.countByName(dto.getName(), null) > 0) {
            throw new EduException(EduErrorCode.NEWS_CATEGORY_NAME_EXISTS);
        }
        EduNewsCategory entity = new EduNewsCategory();
        entity.setName(dto.getName());
        entity.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        categoryMapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NewsCategoryUpdateReqDTO dto) {
        EduNewsCategory exist = categoryMapper.selectById(dto.getId());
        if (exist == null) {
            throw new EduException(EduErrorCode.NEWS_CATEGORY_NOT_FOUND);
        }
        if (categoryMapper.countByName(dto.getName(), dto.getId()) > 0) {
            throw new EduException(EduErrorCode.NEWS_CATEGORY_NAME_EXISTS);
        }
        EduNewsCategory entity = new EduNewsCategory();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        categoryMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduNewsCategory exist = categoryMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.NEWS_CATEGORY_NOT_FOUND);
        }
        if (newsMapper.countByCategoryId(id) > 0) {
            throw new EduException(EduErrorCode.NEWS_CATEGORY_HAS_NEWS);
        }
        categoryMapper.deleteById(id);
    }

    private NewsCategoryVO toVO(EduNewsCategory entity) {
        return NewsCategoryVO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .sortOrder(entity.getSortOrder())
                .status(entity.getStatus())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
}
