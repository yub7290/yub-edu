package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduAddress;
import com.yub.edu.biz.mapper.EduAddressMapper;
import com.yub.edu.biz.service.EduAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户地址服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 用户地址管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduAddressServiceImpl implements EduAddressService {

    private final EduAddressMapper mapper;

    @Override
    public List<EduAddress> selectByUserId(Long userId) {
        return mapper.selectByUserId(userId);
    }

    @Override
    public EduAddress selectById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public int insert(EduAddress address) {
        return mapper.insert(address);
    }

    @Override
    public int updateById(EduAddress address) {
        return mapper.updateById(address);
    }

    @Override
    public int deleteById(Long id, Long userId) {
        return mapper.deleteById(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearDefault(Long userId) {
        mapper.clearDefault(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id, Long userId) {
        mapper.clearDefault(userId);
        mapper.setDefault(id, userId);
    }
}
