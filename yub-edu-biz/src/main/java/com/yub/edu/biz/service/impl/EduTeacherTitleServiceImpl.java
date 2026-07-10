package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduTeacherTitle;
import com.yub.edu.biz.mapper.EduTeacherTitleMapper;
import com.yub.edu.biz.service.EduTeacherTitleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 教师职称 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 教师职称业务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduTeacherTitleServiceImpl implements EduTeacherTitleService {

    private final EduTeacherTitleMapper eduTeacherTitleMapper;

    @Override
    public List<EduTeacherTitle> selectBatchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return eduTeacherTitleMapper.selectBatchByIds(ids);
    }
}
