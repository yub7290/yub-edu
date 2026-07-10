package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduPointsProduct;
import com.yub.edu.biz.mapper.EduPointsProductMapper;
import com.yub.edu.biz.service.EduPointsProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 积分商品服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-09
 * @Description: 积分商品管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduPointsProductServiceImpl implements EduPointsProductService {

    private final EduPointsProductMapper mapper;

    @Override
    public List<EduPointsProduct> selectPage(String name, Integer status) {
        return mapper.selectPage(name, status);
    }

    @Override
    public EduPointsProduct selectById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public int insert(EduPointsProduct product) {
        return mapper.insert(product);
    }

    @Override
    public int updateById(EduPointsProduct product) {
        return mapper.updateById(product);
    }

    @Override
    public int deleteById(Long id) {
        return mapper.deleteById(id);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        return mapper.updateStatus(id, status);
    }
}
