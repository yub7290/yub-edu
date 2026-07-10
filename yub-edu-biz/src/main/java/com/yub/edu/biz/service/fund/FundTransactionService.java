package com.yub.edu.biz.service.fund;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageResult;
import com.yub.edu.api.vo.app.FundTransactionVO;
import com.yub.edu.biz.entity.EduFundTransaction;
import com.yub.edu.biz.mapper.EduFundTransactionMapper;
import com.yub.framework.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 资金交易记录服务
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FundTransactionService {
    private final EduFundTransactionMapper transactionMapper;

    public void createTransaction(Long userId, String type, BigDecimal amount, BigDecimal balanceAfter, String relatedId, String description) {
        EduFundTransaction transaction = new EduFundTransaction();
        transaction.setUserId(userId);
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setRelatedId(relatedId);
        transaction.setDescription(description);
        transactionMapper.insert(transaction);
    }

    public PageResult<FundTransactionVO> page(Long userId, String transactionType, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduFundTransaction> list = transactionMapper.selectPage(userId, transactionType);
        PageInfo<EduFundTransaction> pageInfo = new PageInfo<>(list);
        List<FundTransactionVO> records = BeanUtils.copyList(list, FundTransactionVO.class);
        return PageResult.of(records, pageInfo.getTotal());
    }

    public PageResult<FundTransactionVO> allPage(Long userId, String userName, String transactionType, String startTime, String endTime, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduFundTransaction> list = transactionMapper.selectAllPage(userId, userName, transactionType, startTime, endTime);
        PageInfo<EduFundTransaction> pageInfo = new PageInfo<>(list);
        List<FundTransactionVO> records = BeanUtils.copyList(list, FundTransactionVO.class);
        return PageResult.of(records, pageInfo.getTotal());
    }
}
