package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageResult;
import com.yub.edu.api.vo.app.PointsAccountVO;
import com.yub.edu.api.vo.app.PointsRecordVO;
import com.yub.edu.biz.entity.EduPointsAccount;
import com.yub.edu.biz.entity.EduPointsRecord;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduPointsAccountMapper;
import com.yub.edu.biz.mapper.EduPointsRecordMapper;
import com.yub.edu.biz.service.PointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分业务实现
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PointsServiceImpl implements PointsService {

    /** 最大乐观锁重试次数 */
    private static final int MAX_RETRY = 3;
    /** 重试间隔（毫秒） */
    private static final long RETRY_INTERVAL_MS = 100;

    private final EduPointsAccountMapper pointsAccountMapper;
    private final EduPointsRecordMapper pointsRecordMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PointsAccountVO getAccountInfo(Long userId) {
        EduPointsAccount account = pointsAccountMapper.selectByUserId(userId);
        if (account == null) {
            account = new EduPointsAccount();
            account.setUserId(userId);
            account.setAvailablePoints(0);
            account.setTotalPoints(0);
            account.setVersion(0);
            pointsAccountMapper.insert(account);
        }
        PointsAccountVO vo = new PointsAccountVO();
        vo.setAvailablePoints(account.getAvailablePoints());
        vo.setTotalPoints(account.getTotalPoints());
        return vo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void earnPoints(Long userId, Integer points, Integer changeType,
                           String description, String bizId, String bizType) {
        EduPointsAccount account = getOrCreateAccount(userId);
        int retryCount = 0;
        while (retryCount < MAX_RETRY) {
            account.setAvailablePoints(account.getAvailablePoints() + points);
            account.setTotalPoints(account.getTotalPoints() + points);
            int rows = pointsAccountMapper.updateByUserId(account);
            if (rows > 0) {
                insertRecord(userId, points, changeType, description, bizId, bizType);
                log.info("积分获得成功, userId={}, points={}, retry={}", userId, points, retryCount);
                return;
            }
            retryCount++;
            account = pointsAccountMapper.selectByUserId(userId);
            if (retryCount < MAX_RETRY) {
                try {
                    Thread.sleep(RETRY_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new EduException(EduErrorCode.POINTS_CONFLICT);
                }
            }
        }
        log.warn("积分获得失败（乐观锁冲突）, userId={}, points={}, retry={}", userId, points, retryCount);
        throw new EduException(EduErrorCode.POINTS_CONFLICT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spendPoints(Long userId, Integer points, Integer changeType,
                            String description, String bizId, String bizType) {
        EduPointsAccount account = getOrCreateAccount(userId);
        if (account.getAvailablePoints() < points) {
            log.warn("积分余额不足, userId={}, available={}, spend={}",
                    userId, account.getAvailablePoints(), points);
            throw new EduException(EduErrorCode.POINTS_INSUFFICIENT);
        }
        int retryCount = 0;
        while (retryCount < MAX_RETRY) {
            account.setAvailablePoints(account.getAvailablePoints() - points);
            int rows = pointsAccountMapper.updateByUserId(account);
            if (rows > 0) {
                insertRecord(userId, -points, changeType, description, bizId, bizType);
                log.info("积分消耗成功, userId={}, points={}, retry={}", userId, points, retryCount);
                return;
            }
            retryCount++;
            account = pointsAccountMapper.selectByUserId(userId);
            if (retryCount < MAX_RETRY) {
                try {
                    Thread.sleep(RETRY_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new EduException(EduErrorCode.POINTS_CONFLICT);
                }
            }
        }
        log.warn("积分消耗失败（乐观锁冲突）, userId={}, points={}, retry={}", userId, points, retryCount);
        throw new EduException(EduErrorCode.POINTS_CONFLICT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResult<PointsRecordVO> getRecords(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduPointsRecord> records = pointsRecordMapper.selectByUserId(userId);
        PageInfo<EduPointsRecord> pageInfo = new PageInfo<>(records);
        List<PointsRecordVO> voList = records.stream().map(this::toRecordVO).collect(Collectors.toList());
        return PageResult.of(voList, pageInfo.getTotal());
    }

    /**
     * 获取或创建积分账户
     *
     * @param userId 用户ID
     * @return 积分账户
     */
    private EduPointsAccount getOrCreateAccount(Long userId) {
        EduPointsAccount account = pointsAccountMapper.selectByUserId(userId);
        if (account == null) {
            account = new EduPointsAccount();
            account.setUserId(userId);
            account.setAvailablePoints(0);
            account.setTotalPoints(0);
            account.setVersion(0);
            pointsAccountMapper.insert(account);
        }
        return account;
    }

    /**
     * 插入积分记录
     *
     * @param userId      用户ID
     * @param points      积分变动
     * @param changeType  变动类型
     * @param description 描述
     * @param bizId       业务ID
     * @param bizType     业务类型
     */
    private void insertRecord(Long userId, Integer points, Integer changeType,
                              String description, String bizId, String bizType) {
        EduPointsRecord record = new EduPointsRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setChangeType(changeType);
        record.setDescription(description);
        record.setBizId(bizId);
        record.setBizType(bizType);
        pointsRecordMapper.insert(record);
    }

    /**
     * 将积分记录实体转为VO
     *
     * @param record 积分记录实体
     * @return 积分记录VO
     */
    private PointsRecordVO toRecordVO(EduPointsRecord record) {
        PointsRecordVO vo = new PointsRecordVO();
        vo.setId(record.getId());
        vo.setPoints(record.getPoints());
        vo.setChangeType(record.getChangeType());
        vo.setDescription(record.getDescription());
        vo.setCreateTime(record.getCreateTime());
        return vo;
    }
}
