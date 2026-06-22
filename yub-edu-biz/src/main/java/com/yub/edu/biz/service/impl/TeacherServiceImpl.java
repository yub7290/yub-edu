package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.framework.security.SecurityUtils;
import com.yub.framework.util.BeanUtils;
import com.yub.edu.biz.dto.TeacherCreateReqDTO;
import com.yub.edu.biz.dto.TeacherQueryDTO;
import com.yub.edu.biz.dto.TeacherUpdateReqDTO;
import com.yub.edu.biz.entity.EduTeacher;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduTeacherMapper;
import com.yub.edu.biz.mapper.EduTeacherTitleMapper;
import com.yub.edu.biz.service.TeacherService;
import com.yub.edu.biz.vo.TeacherDetailRespVO;
import com.yub.edu.biz.vo.TeacherPageRespVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * 教师 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师业务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private static final String DEFAULT_PASSWORD_SM3 = "667c756cf9334e328a56e44e906245c8e214c655a160f18fdb84d79c209c49cf";

    private final EduTeacherMapper eduTeacherMapper;
    private final EduTeacherTitleMapper eduTeacherTitleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<TeacherPageRespVO> page(PageQuery<TeacherQueryDTO> pageQuery) {
        PageHelper.startPage(pageQuery.getPageParam().getPageNum(), pageQuery.getPageParam().getPageSize());
        List<EduTeacher> list = eduTeacherMapper.selectPage(pageQuery.getQueryParam());
        PageInfo<EduTeacher> pageInfo = new PageInfo<>(list);

        // 批量加载职称名称（优化 N+1）
        Map<Long, String> titleMap = loadTitleNames(list);

        List<TeacherPageRespVO> records = list.stream()
                .map(teacher -> convertToPageVO(teacher, titleMap))
                .collect(Collectors.toList());

        return PageResult.of(records, pageInfo.getTotal());
    }

    private TeacherPageRespVO convertToPageVO(EduTeacher teacher, Map<Long, String> titleMap) {
        TeacherPageRespVO vo = BeanUtils.copy(teacher, TeacherPageRespVO.class);

        // 职称名称从批量加载的 Map 中获取
        if (teacher.getTitleId() != null) {
            vo.setTitleName(titleMap.getOrDefault(teacher.getTitleId(), null));
        }

        // 计算年龄
        if (teacher.getBirthDate() != null) {
            vo.setAge(Period.between(teacher.getBirthDate(), LocalDate.now()).getYears());
        }

        // 课程数暂为0，后续可通过课程表关联统计
        vo.setCourseCount(0);

        return vo;
    }

    /**
     * 批量加载职称名称 Map
     */
    private Map<Long, String> loadTitleNames(List<EduTeacher> teachers) {
        List<Long> titleIds = teachers.stream()
                .map(EduTeacher::getTitleId)
                .filter(id -> id != null)
                .distinct()
                .toList();
        if (titleIds.isEmpty()) return Map.of();
        return eduTeacherTitleMapper.selectBatchByIds(titleIds).stream()
                .collect(toMap(t -> t.getId(), t -> t.getName()));
    }

    @Override
    public TeacherDetailRespVO getDetail(Long id) {
        EduTeacher teacher = eduTeacherMapper.selectById(id);
        if (teacher == null) {
            throw new EduException(EduErrorCode.TEACHER_NOT_FOUND);
        }

        TeacherDetailRespVO vo = BeanUtils.copy(teacher, TeacherDetailRespVO.class);

        // 查询职称名称
        if (teacher.getTitleId() != null) {
            var title = eduTeacherTitleMapper.selectById(teacher.getTitleId());
            if (title != null) {
                vo.setTitleName(title.getName());
            }
        }

        return vo;
    }

    @Override
    @Transactional
    public Long create(TeacherCreateReqDTO req) {
        EduTeacher exist = eduTeacherMapper.selectByAccountIncludeDeleted(req.getAccount());
        if (exist != null) {
            throw new EduException(EduErrorCode.TEACHER_ACCOUNT_EXISTS);
        }

        EduTeacher teacher = new EduTeacher();
        teacher.setAvatarUrl(req.getAvatarUrl());
        teacher.setAccount(req.getAccount());
        teacher.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD_SM3));
        teacher.setName(req.getName());
        teacher.setTitleId(req.getTitleId());
        teacher.setPinyinAbbr(req.getPinyinAbbr());
        teacher.setGender(req.getGender());
        teacher.setPhone(req.getPhone());
        teacher.setIdCard(req.getIdCard());
        teacher.setStatus(req.getStatus());
        teacher.setFixedPhone(req.getFixedPhone());
        teacher.setEmail(req.getEmail());
        teacher.setQq(req.getQq());
        teacher.setWechat(req.getWechat());
        teacher.setAddress(req.getAddress());
        teacher.setMailingAddress(req.getMailingAddress());
        teacher.setZipCode(req.getZipCode());
        teacher.setEmergencyContact(req.getEmergencyContact());
        teacher.setEmergencyPhone(req.getEmergencyPhone());
        teacher.setBirthDate(req.getBirthDate());
        teacher.setEducation(req.getEducation());
        teacher.setMajor(req.getMajor());
        teacher.setNativePlace(req.getNativePlace());
        teacher.setWorkUnit(req.getWorkUnit());
        teacher.setNationality(req.getNationality());
        teacher.setSignature(req.getSignature());
        teacher.setBio(req.getBio());

        Long currentUserId = SecurityUtils.getCurrentUserId();
        teacher.setCreateBy(currentUserId);
        teacher.setUpdateBy(currentUserId);
        eduTeacherMapper.insert(teacher);
        return teacher.getId();
    }

    @Override
    @Transactional
    public void update(TeacherUpdateReqDTO req) {
        EduTeacher exist = eduTeacherMapper.selectById(req.getId());
        if (exist == null) {
            throw new EduException(EduErrorCode.TEACHER_NOT_FOUND);
        }

        EduTeacher teacher = new EduTeacher();
        teacher.setId(req.getId());
        teacher.setAvatarUrl(req.getAvatarUrl());
        teacher.setName(req.getName());
        teacher.setTitleId(req.getTitleId());
        teacher.setPinyinAbbr(req.getPinyinAbbr());
        teacher.setGender(req.getGender());
        teacher.setPhone(req.getPhone());
        teacher.setIdCard(req.getIdCard());
        teacher.setStatus(req.getStatus());
        teacher.setFixedPhone(req.getFixedPhone());
        teacher.setEmail(req.getEmail());
        teacher.setQq(req.getQq());
        teacher.setWechat(req.getWechat());
        teacher.setAddress(req.getAddress());
        teacher.setMailingAddress(req.getMailingAddress());
        teacher.setZipCode(req.getZipCode());
        teacher.setEmergencyContact(req.getEmergencyContact());
        teacher.setEmergencyPhone(req.getEmergencyPhone());
        teacher.setBirthDate(req.getBirthDate());
        teacher.setEducation(req.getEducation());
        teacher.setMajor(req.getMajor());
        teacher.setNativePlace(req.getNativePlace());
        teacher.setWorkUnit(req.getWorkUnit());
        teacher.setNationality(req.getNationality());
        teacher.setSignature(req.getSignature());
        teacher.setBio(req.getBio());
        teacher.setUpdateBy(SecurityUtils.getCurrentUserId());

        eduTeacherMapper.updateById(teacher);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EduTeacher exist = eduTeacherMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.TEACHER_NOT_FOUND);
        }
        eduTeacherMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        eduTeacherMapper.deleteBatch(ids);
    }

    @Override
    @Transactional
    public void resetPassword(Long id) {
        EduTeacher exist = eduTeacherMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.TEACHER_NOT_FOUND);
        }
        eduTeacherMapper.updatePassword(id, passwordEncoder.encode(DEFAULT_PASSWORD_SM3));
        log.info("教师 {} 密码已重置为默认密码，操作者={}", id, SecurityUtils.getCurrentUserId());
    }

    @Override
    @Transactional
    public void changeStatus(Long id, Integer status) {
        EduTeacher exist = eduTeacherMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.TEACHER_NOT_FOUND);
        }
        eduTeacherMapper.updateStatus(id, status);
    }

    @Override
    public List<EduTeacher> selectAllEnabled() {
        return eduTeacherMapper.selectAllEnabled();
    }

    @Override
    public List<EduTeacher> selectRecommended() {
        return eduTeacherMapper.selectRecommended();
    }

    @Override
    public List<EduTeacher> selectStudentList() {
        return eduTeacherMapper.selectStudentList();
    }

    @Override
    @Transactional
    public void setRecommended(Long id, Integer recommended) {
        EduTeacher exist = eduTeacherMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.TEACHER_NOT_FOUND);
        }
        eduTeacherMapper.updateRecommended(id, recommended);
    }
}
