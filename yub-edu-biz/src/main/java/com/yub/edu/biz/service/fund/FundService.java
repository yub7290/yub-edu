package com.yub.edu.biz.service.fund;

import com.yub.edu.api.vo.app.FundSummaryVO;
import com.yub.edu.biz.entity.EduFundAccount;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduFundAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * @Author bing.yu
 * @CreateTime 2026-07-06
 * @Description 资金账户服务
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FundService {
    private final EduFundAccountMapper fundAccountMapper;
    // TODO: 架构治理 - Service间耦合: FundService 依赖 FundTransactionService，应通过 Manager 层解耦
    private final FundTransactionService fundTransactionService;
    private static final int MAX_RETRY = 3;

    public FundSummaryVO getSummary(Long userId) {
        EduFundAccount account = getOrCreateAccount(userId);
        FundSummaryVO vo = new FundSummaryVO();
        vo.setBalance(account.getBalance());
        vo.setTotalRecharge(account.getTotalRecharge());
        vo.setTotalConsumption(account.getTotalConsumption());
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void recharge(Long userId, BigDecimal amount, String orderNo) {
        EduFundAccount account = getOrCreateAccount(userId);
        for (int i = 0; i < MAX_RETRY; i++) {
            int rows = fundAccountMapper.recharge(account.getId(), amount, account.getVersion());
            if (rows > 0) {
                BigDecimal newBalance = account.getBalance().add(amount);
                fundTransactionService.createTransaction(userId, "RECHARGE", amount, newBalance, orderNo, "账户充值");
                log.info("充值成功, userId: {}, amount: {}, orderNo: {}", userId, amount, orderNo);
                return;
            }
            account = fundAccountMapper.selectByUserId(userId);
        }
        throw new EduException(EduErrorCode.REFUND_FAILED);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deduct(Long userId, BigDecimal amount, String relatedId, String description) {
        EduFundAccount account = getOrCreateAccount(userId);
        for (int i = 0; i < MAX_RETRY; i++) {
            int rows = fundAccountMapper.deduct(account.getId(), amount, account.getVersion());
            if (rows > 0) {
                BigDecimal newBalance = account.getBalance().subtract(amount);
                fundTransactionService.createTransaction(userId, "COURSE_PURCHASE", amount.negate(), newBalance, relatedId, description);
                log.info("扣款成功, userId: {}, amount: {}, relatedId: {}", userId, amount, relatedId);
                return;
            }
            account = fundAccountMapper.selectByUserId(userId);
        }
        throw new EduException(EduErrorCode.BALANCE_INSUFFICIENT);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBalance(Long userId, BigDecimal amount, String relatedId, String description) {
        EduFundAccount account = getOrCreateAccount(userId);
        for (int i = 0; i < MAX_RETRY; i++) {
            int rows = fundAccountMapper.recharge(account.getId(), amount, account.getVersion());
            if (rows > 0) {
                BigDecimal newBalance = account.getBalance().add(amount);
                fundTransactionService.createTransaction(userId, "REFUND", amount, newBalance, relatedId, description);
                log.info("加款成功, userId: {}, amount: {}, relatedId: {}", userId, amount, relatedId);
                return;
            }
            account = fundAccountMapper.selectByUserId(userId);
        }
        throw new EduException(EduErrorCode.REFUND_FAILED);
    }

    private EduFundAccount getOrCreateAccount(Long userId) {
        EduFundAccount account = fundAccountMapper.selectByUserId(userId);
        if (account == null) {
            account = new EduFundAccount();
            account.setUserId(userId);
            account.setBalance(BigDecimal.ZERO);
            account.setTotalRecharge(BigDecimal.ZERO);
            account.setTotalConsumption(BigDecimal.ZERO);
            account.setVersion(0);
            fundAccountMapper.insert(account);
            account = fundAccountMapper.selectByUserId(userId);
        }
        return account;
    }
}
