package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageParam;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.StudyCardCreateReqDTO;
import com.yub.edu.biz.dto.StudyCardInstanceQueryDTO;
import com.yub.edu.biz.dto.StudyCardQueryDTO;
import com.yub.edu.biz.dto.StudyCardUpdateReqDTO;
import com.yub.edu.biz.dto.StudyCardUseReqDTO;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduStudyCard;
import com.yub.edu.biz.entity.EduStudyCardCourse;
import com.yub.edu.biz.entity.EduStudyCardInstance;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduStudyCardCourseMapper;
import com.yub.edu.biz.mapper.EduStudyCardInstanceMapper;
import com.yub.edu.biz.mapper.EduStudyCardMapper;
import com.yub.edu.biz.service.EduStudyCardService;
import com.yub.edu.biz.vo.StudyCardDetailRespVO;
import com.yub.edu.biz.vo.StudyCardInstancePageRespVO;
import com.yub.edu.biz.vo.StudyCardMyVO;
import com.yub.edu.biz.vo.StudyCardPageRespVO;
import com.yub.edu.biz.vo.StudyCardSummaryVO;
import com.yub.framework.security.SecurityUtils;
import com.yub.system.entity.user.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 学习卡服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡管理服务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduStudyCardServiceImpl implements EduStudyCardService {

    private final EduStudyCardMapper eduStudyCardMapper;
    private final EduStudyCardInstanceMapper eduStudyCardInstanceMapper;
    private final EduStudyCardCourseMapper eduStudyCardCourseMapper;
    private final EduCourseMapper eduCourseMapper;
    private final com.yub.system.mapper.user.SysUserMapper sysUserMapper;

    @Override
    public PageResult<StudyCardPageRespVO> page(PageQuery<StudyCardQueryDTO> pageQuery) {
        StudyCardQueryDTO queryParam = pageQuery.getQueryParam();
        com.yub.common.model.PageParam pageParam = pageQuery.getPageParam();

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<StudyCardPageRespVO> list = eduStudyCardMapper.selectPage(queryParam);
        PageInfo<StudyCardPageRespVO> pageInfo = new PageInfo<>(list);

        return PageResult.of(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public StudyCardDetailRespVO getDetail(Long id) {
        EduStudyCard card = eduStudyCardMapper.selectById(id);
        if (card == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_NOT_FOUND);
        }

        List<Long> courseIds = eduStudyCardCourseMapper.selectCourseIdsByCardId(id);

        return StudyCardDetailRespVO.builder()
                .id(card.getId())
                .title(card.getTitle())
                .amount(card.getAmount())
                .couponDeductible(card.getCouponDeductible())
                .quantity(card.getQuantity())
                .status(card.getStatus())
                .validStartDate(card.getValidStartDate())
                .validEndDate(card.getValidEndDate())
                .studyDuration(card.getStudyDuration())
                .studyDurationUnit(card.getStudyDurationUnit())
                .description(card.getDescription())
                .cardCodeLength(card.getCardCodeLength())
                .secretCodeLength(card.getSecretCodeLength())
                .courseIds(courseIds)
                .courseCount(courseIds.size())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(StudyCardCreateReqDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        EduStudyCard card = new EduStudyCard();
        card.setTitle(dto.getTitle());
        card.setAmount(dto.getAmount());
        card.setCouponDeductible(dto.getCouponDeductible() != null ? dto.getCouponDeductible() : 0);
        card.setQuantity(dto.getQuantity());
        card.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        card.setValidStartDate(dto.getValidStartDate());
        card.setValidEndDate(dto.getValidEndDate());
        card.setStudyDuration(dto.getStudyDuration());
        card.setStudyDurationUnit(dto.getStudyDurationUnit());
        card.setDescription(dto.getDescription());
        card.setCardCodeLength(dto.getCardCodeLength() != null ? dto.getCardCodeLength() : 8);
        card.setSecretCodeLength(dto.getSecretCodeLength() != null ? dto.getSecretCodeLength() : 6);
        card.setCreateBy(currentUserId);
        card.setUpdateBy(currentUserId);
        eduStudyCardMapper.insert(card);

        insertCourseRelations(card.getId(), dto.getCourseIds());
        batchGenerateInstances(card, dto.getQuantity());

        return card.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StudyCardUpdateReqDTO dto) {
        EduStudyCard card = eduStudyCardMapper.selectById(dto.getId());
        if (card == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_NOT_FOUND);
        }

        if (dto.getAmount() != null) { card.setAmount(dto.getAmount()); }
        if (dto.getCouponDeductible() != null) { card.setCouponDeductible(dto.getCouponDeductible()); }
        if (dto.getStatus() != null) { card.setStatus(dto.getStatus()); }
        if (dto.getValidStartDate() != null) { card.setValidStartDate(dto.getValidStartDate()); }
        if (dto.getValidEndDate() != null) { card.setValidEndDate(dto.getValidEndDate()); }
        if (dto.getStudyDuration() != null) { card.setStudyDuration(dto.getStudyDuration()); }
        if (dto.getStudyDurationUnit() != null) { card.setStudyDurationUnit(dto.getStudyDurationUnit()); }
        if (dto.getDescription() != null) { card.setDescription(dto.getDescription()); }
        if (dto.getCardCodeLength() != null) { card.setCardCodeLength(dto.getCardCodeLength()); }
        if (dto.getSecretCodeLength() != null) { card.setSecretCodeLength(dto.getSecretCodeLength()); }
        card.setUpdateBy(SecurityUtils.getCurrentUserId());
        eduStudyCardMapper.updateById(card);

        if (dto.getCourseIds() != null) {
            eduStudyCardCourseMapper.deleteByCardId(dto.getId());
            insertCourseRelations(dto.getId(), dto.getCourseIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EduStudyCard card = eduStudyCardMapper.selectById(id);
        if (card == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_NOT_FOUND);
        }
        eduStudyCardMapper.deleteById(id);
        eduStudyCardInstanceMapper.deleteByCardId(id);
        eduStudyCardCourseMapper.deleteByCardId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        EduStudyCard card = eduStudyCardMapper.selectById(id);
        if (card == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_NOT_FOUND);
        }
        eduStudyCardMapper.updateStatus(id, status);
    }

    @Override
    public PageResult<StudyCardInstancePageRespVO> getInstancePage(Long cardId,
                                                                   PageQuery<StudyCardInstanceQueryDTO> pageQuery) {
        EduStudyCard card = eduStudyCardMapper.selectById(cardId);
        if (card == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_NOT_FOUND);
        }

        StudyCardInstanceQueryDTO queryParam = pageQuery.getQueryParam();
        com.yub.common.model.PageParam pageParam = pageQuery.getPageParam();

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<StudyCardInstancePageRespVO> list = eduStudyCardInstanceMapper.selectPage(cardId, queryParam);
        PageInfo<StudyCardInstancePageRespVO> pageInfo = new PageInfo<>(list);

        return PageResult.of(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchGenerate(Long cardId, Integer count) {
        EduStudyCard card = eduStudyCardMapper.selectById(cardId);
        if (card == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_NOT_FOUND);
        }
        batchGenerateInstances(card, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollbackInstance(Long instanceId) {
        EduStudyCardInstance instance = eduStudyCardInstanceMapper.selectById(instanceId);
        if (instance == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_NOT_FOUND);
        }
        if (instance.getStatus() != 1) {
            throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_USED);
        }
        instance.setStatus(2);
        instance.setUserId(null);
        instance.setUserAccount(null);
        instance.setUseTime(null);
        eduStudyCardInstanceMapper.updateById(instance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleInstanceStatus(Long instanceId, Integer status) {
        EduStudyCardInstance instance = eduStudyCardInstanceMapper.selectById(instanceId);
        if (instance == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_NOT_FOUND);
        }
        eduStudyCardInstanceMapper.updateStatus(instanceId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void useCard(StudyCardUseReqDTO dto) {
        EduStudyCardInstance instance = eduStudyCardInstanceMapper.selectByCardNo(dto.getCardNo());
        if (instance == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_NO_NOT_EXIST);
        }
        if (instance.getStatus() != 0) {
            if (instance.getStatus() == 1) {
                throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_USED);
            } else if (instance.getStatus() == 2) {
                throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_ROLLBACK);
            } else if (instance.getStatus() == 3) {
                throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_DISABLED);
            }
        }

        EduStudyCard card = eduStudyCardMapper.selectById(instance.getCardId());
        if (card == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_NOT_FOUND);
        }
        LocalDate today = LocalDate.now();
        if (card.getValidStartDate() != null && today.isBefore(card.getValidStartDate())) {
            throw new EduException(EduErrorCode.STUDY_CARD_NO_EXPIRED);
        }
        if (card.getValidEndDate() != null && today.isAfter(card.getValidEndDate())) {
            throw new EduException(EduErrorCode.STUDY_CARD_NO_EXPIRED);
        }

        Long currentUserId = SecurityUtils.getCurrentUserId();
        instance.setStatus(1);
        instance.setUserId(currentUserId);
        instance.setUseTime(java.time.LocalDateTime.now());
        SysUser user = sysUserMapper.selectById(currentUserId);
        if (user != null) {
            instance.setUserAccount(user.getAccount());
        }
        eduStudyCardInstanceMapper.updateById(instance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCard(StudyCardUseReqDTO dto) {
        EduStudyCardInstance instance = eduStudyCardInstanceMapper.selectByCardNo(dto.getCardNo());
        if (instance == null) {
            throw new EduException(EduErrorCode.STUDY_CARD_NO_NOT_EXIST);
        }
        if (instance.getStatus() == 1) {
            throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_USED);
        }
        if (instance.getStatus() == 3) {
            throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_DISABLED);
        }
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (instance.getUserId() != null && !instance.getUserId().equals(currentUserId)) {
            throw new EduException(EduErrorCode.STUDY_CARD_INSTANCE_USED);
        }
        eduStudyCardInstanceMapper.bindUser(instance.getId(), currentUserId);
    }

    @Override
    public PageResult<StudyCardMyVO> getMyCards(PageQuery<?> pageQuery) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        PageParam pageParam = pageQuery.getPageParam();
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<EduStudyCardInstance> instances = eduStudyCardInstanceMapper.selectByUserId(currentUserId);
        PageInfo<EduStudyCardInstance> pageInfo = new PageInfo<>(instances);

        List<StudyCardMyVO> voList = new ArrayList<>();
        for (EduStudyCardInstance inst : pageInfo.getList()) {
            EduStudyCard card = eduStudyCardMapper.selectById(inst.getCardId());
            List<String> courseNames = new ArrayList<>();
            if (card != null) {
                List<Long> courseIds = eduStudyCardCourseMapper.selectCourseIdsByCardId(card.getId());
                for (Long cid : courseIds) {
                    EduCourse course = eduCourseMapper.selectById(cid);
                    if (course != null) {
                        courseNames.add(course.getName());
                    }
                }
            }
            StudyCardMyVO vo = StudyCardMyVO.builder()
                    .id(inst.getId())
                    .title(card != null ? card.getTitle() : "")
                    .cardNo(inst.getCardNo())
                    .amount(card != null ? card.getAmount() : null)
                    .status(inst.getStatus())
                    .validStartDate(card != null ? card.getValidStartDate() : null)
                    .validEndDate(card != null ? card.getValidEndDate() : null)
                    .studyDuration(card != null ? card.getStudyDuration() : null)
                    .studyDurationUnit(card != null ? card.getStudyDurationUnit() : null)
                    .courseNames(courseNames)
                    .useTime(inst.getUseTime())
                    .build();
            voList.add(vo);
        }
        return PageResult.of(voList, pageInfo.getTotal());
    }

    @Override
    public StudyCardSummaryVO getSummary() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        long totalCount = eduStudyCardInstanceMapper.countByUserId(currentUserId);
        long usedCount = eduStudyCardInstanceMapper.countUsedByUserId(currentUserId);
        return StudyCardSummaryVO.builder()
                .totalCount(totalCount)
                .usedCount(usedCount)
                .build();
    }

    private void insertCourseRelations(Long cardId, List<Long> courseIds) {
        if (CollectionUtils.isEmpty(courseIds)) {
            return;
        }
        List<EduStudyCardCourse> relations = new ArrayList<>();
        for (Long courseId : courseIds) {
            EduStudyCardCourse relation = new EduStudyCardCourse();
            relation.setCardId(cardId);
            relation.setCourseId(courseId);
            relations.add(relation);
        }
        eduStudyCardCourseMapper.batchInsert(relations);
    }

    private void batchGenerateInstances(EduStudyCard card, Integer count) {
        if (count == null || count <= 0) {
            return;
        }
        List<EduStudyCardInstance> instances = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            EduStudyCardInstance instance = new EduStudyCardInstance();
            instance.setCardId(card.getId());
            instance.setCardNo(generateRandomString(card.getCardCodeLength(), true));
            instance.setSecretCode(generateRandomString(card.getSecretCodeLength(), false));
            instance.setStatus(0);
            instances.add(instance);
        }
        eduStudyCardInstanceMapper.batchInsert(instances);
    }

    private String generateRandomString(int length, boolean alphanumeric) {
        String chars = alphanumeric ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" : "0123456789";
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
