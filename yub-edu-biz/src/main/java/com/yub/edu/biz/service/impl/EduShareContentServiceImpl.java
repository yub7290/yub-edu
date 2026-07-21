package com.yub.edu.biz.service.impl;

import com.yub.common.model.PageResult;
import com.yub.edu.biz.entity.EduShareContent;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduShareContentMapper;
import com.yub.edu.biz.service.EduShareContentService;
import com.yub.edu.biz.vo.ShareContentRespVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分享内容服务实现类
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-20
 * @Description: 分享内容管理服务实现
 * @Version: 1.0.0
 */
@Service
@RequiredArgsConstructor
public class EduShareContentServiceImpl implements EduShareContentService {

    private final EduShareContentMapper shareContentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(EduShareContent content) {
        Long uid = SecurityUtils.getCurrentUserId();
        content.setCreateBy(uid);
        content.setUpdateBy(uid);
        content.setDeleted(0);
        shareContentMapper.insert(content);
        return content.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EduShareContent content) {
        EduShareContent existing = shareContentMapper.selectById(id);
        if (existing == null) {
            throw new EduException(EduErrorCode.SHARE_CONTENT_NOT_FOUND);
        }
        content.setId(id);
        content.setUpdateBy(SecurityUtils.getCurrentUserId());
        shareContentMapper.updateById(content);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduShareContent existing = shareContentMapper.selectById(id);
        if (existing == null) {
            throw new EduException(EduErrorCode.SHARE_CONTENT_NOT_FOUND);
        }
        shareContentMapper.deleteById(id);
    }

    @Override
    public EduShareContent getById(Long id) {
        return shareContentMapper.selectById(id);
    }

    @Override
    public ShareContentRespVO getActiveContent() {
        EduShareContent content = shareContentMapper.selectActiveContent();
        if (content == null) {
            ShareContentRespVO defaultVO = new ShareContentRespVO();
            defaultVO.setTitle("邀请好友");
            defaultVO.setDescription("邀请好友一起学习，共同进步！");
            defaultVO.setImageUrl("");
            return defaultVO;
        }
        ShareContentRespVO vo = new ShareContentRespVO();
        vo.setTitle(content.getTitle());
        vo.setDescription(content.getDescription());
        vo.setImageUrl(content.getImageUrl());
        vo.setContent(content.getContent());
        return vo;
    }

    @Override
    public PageResult<EduShareContent> page(String title, Integer status, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<EduShareContent> list = shareContentMapper.selectPage(title, status, offset, pageSize);
        int total = shareContentMapper.count(title, status);
        return PageResult.of(list, total);
    }
}