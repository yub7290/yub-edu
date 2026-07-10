package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.MessageQueryDTO;
import com.yub.edu.biz.entity.EduMessage;
import com.yub.edu.biz.mapper.EduMessageMapper;
import com.yub.edu.biz.service.EduMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 留言服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 留言管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduMessageServiceImpl implements EduMessageService {

    private final EduMessageMapper mapper;

    @Override
    public List<EduMessage> selectPage(MessageQueryDTO query) {
        return mapper.selectPage(query);
    }

    @Override
    public EduMessage selectById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public int deleteById(Long id) {
        return mapper.deleteById(id);
    }
}
