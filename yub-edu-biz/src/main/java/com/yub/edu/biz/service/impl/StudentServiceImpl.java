package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.framework.security.SecurityUtils;
import com.yub.framework.util.BeanUtils;
import com.yub.edu.biz.dto.StudentCreateReqDTO;
import com.yub.edu.biz.dto.StudentQueryDTO;
import com.yub.edu.biz.dto.StudentUpdateReqDTO;
import com.yub.edu.biz.entity.EduStudent;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduStudentMapper;
import com.yub.edu.biz.service.StudentService;
import com.yub.edu.biz.vo.StudentDetailRespVO;
import com.yub.edu.biz.vo.StudentPageRespVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学员 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 学员业务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final EduStudentMapper eduStudentMapper;
    private final PasswordEncoder passwordEncoder;

    /** 默认密码 admin123 的 SM3 哈希值 */
    private static final String DEFAULT_PASSWORD_SM3 = "667c756cf9334e328a56e44e906245c8e214c655a160f18fdb84d79c209c49cf";

    @Override
    public PageResult<StudentPageRespVO> page(PageQuery<StudentQueryDTO> pageQuery) {
        PageHelper.startPage(pageQuery.getPageParam().getPageNum(), pageQuery.getPageParam().getPageSize());
        List<EduStudent> list = eduStudentMapper.selectPage(pageQuery.getQueryParam());
        PageInfo<EduStudent> pageInfo = new PageInfo<>(list);

        List<StudentPageRespVO> records = list.stream()
                .map(this::convertToPageVO)
                .collect(Collectors.toList());

        return PageResult.of(records, pageInfo.getTotal());
    }

    private StudentPageRespVO convertToPageVO(EduStudent student) {
        StudentPageRespVO vo = BeanUtils.copy(student, StudentPageRespVO.class);

        // 计算年龄
        if (student.getBirthDate() != null) {
            vo.setAge(Period.between(student.getBirthDate(), LocalDate.now()).getYears());
        }

        return vo;
    }

    @Override
    public StudentDetailRespVO getDetail(Long id) {
        EduStudent student = eduStudentMapper.selectById(id);
        if (student == null) {
            throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
        }

        StudentDetailRespVO vo = BeanUtils.copy(student, StudentDetailRespVO.class);

        // 计算年龄
        if (student.getBirthDate() != null) {
            vo.setAge(Period.between(student.getBirthDate(), LocalDate.now()).getYears());
        }

        return vo;
    }

    @Override
    @Transactional
    public Long create(StudentCreateReqDTO req) {
        EduStudent exist = eduStudentMapper.selectByAccountIncludeDeleted(req.getAccount());
        if (exist != null) {
            throw new EduException(EduErrorCode.STUDENT_ACCOUNT_EXISTS);
        }

        EduStudent student = new EduStudent();
        student.setAvatarUrl(req.getAvatarUrl());
        student.setAccount(req.getAccount());
        student.setPassword(passwordEncoder.encode(
                req.getPassword() != null && !req.getPassword().isEmpty()
                        ? req.getPassword() : DEFAULT_PASSWORD_SM3));
        student.setName(req.getName());
        student.setPinyinAbbr(req.getPinyinAbbr());
        student.setStudentNo(req.getStudentNo());
        student.setGroupId(req.getGroupId());
        student.setGender(req.getGender());
        student.setIdCard(req.getIdCard());
        student.setPhone(req.getPhone());
        student.setPhonePublic(req.getPhonePublic());
        student.setFixedPhone(req.getFixedPhone());
        student.setFixedPhonePublic(req.getFixedPhonePublic());
        student.setEmail(req.getEmail());
        student.setQq(req.getQq());
        student.setWechat(req.getWechat());
        student.setAddress(req.getAddress());
        student.setMailingAddress(req.getMailingAddress());
        student.setZipCode(req.getZipCode());
        student.setEmergencyContact(req.getEmergencyContact());
        student.setEmergencyPhone(req.getEmergencyPhone());
        student.setBirthDate(req.getBirthDate());
        student.setEducation(req.getEducation());
        student.setMajor(req.getMajor());
        student.setNativePlace(req.getNativePlace());
        student.setSchool(req.getSchool());
        student.setNationality(req.getNationality());
        student.setSignature(req.getSignature());
        student.setBio(req.getBio());
        student.setStatus(req.getStatus());

        Long currentUserId = SecurityUtils.getCurrentUserId();
        student.setCreateBy(currentUserId);
        student.setUpdateBy(currentUserId);
        eduStudentMapper.insert(student);
        return student.getId();
    }

    @Override
    @Transactional
    public void update(StudentUpdateReqDTO req) {
        EduStudent exist = eduStudentMapper.selectById(req.getId());
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
        }

        EduStudent student = new EduStudent();
        student.setId(req.getId());
        student.setAvatarUrl(req.getAvatarUrl());
        student.setName(req.getName());
        student.setPinyinAbbr(req.getPinyinAbbr());
        student.setStudentNo(req.getStudentNo());
        student.setGroupId(req.getGroupId());
        student.setGender(req.getGender());
        student.setIdCard(req.getIdCard());
        student.setPhone(req.getPhone());
        student.setPhonePublic(req.getPhonePublic());
        student.setFixedPhone(req.getFixedPhone());
        student.setFixedPhonePublic(req.getFixedPhonePublic());
        student.setEmail(req.getEmail());
        student.setQq(req.getQq());
        student.setWechat(req.getWechat());
        student.setAddress(req.getAddress());
        student.setMailingAddress(req.getMailingAddress());
        student.setZipCode(req.getZipCode());
        student.setEmergencyContact(req.getEmergencyContact());
        student.setEmergencyPhone(req.getEmergencyPhone());
        student.setBirthDate(req.getBirthDate());
        student.setEducation(req.getEducation());
        student.setMajor(req.getMajor());
        student.setNativePlace(req.getNativePlace());
        student.setSchool(req.getSchool());
        student.setNationality(req.getNationality());
        student.setSignature(req.getSignature());
        student.setBio(req.getBio());
        student.setStatus(req.getStatus());
        student.setUpdateBy(SecurityUtils.getCurrentUserId());

        eduStudentMapper.updateById(student);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EduStudent exist = eduStudentMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
        }
        eduStudentMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        eduStudentMapper.deleteBatch(ids);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, Integer status) {
        EduStudent exist = eduStudentMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
        }
        eduStudentMapper.updateStatus(id, status);
    }

    @Override
    @Transactional
    public void batchDisable(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        eduStudentMapper.updateStatusBatch(ids, 0);
    }

    @Override
    @Transactional
    public void resetPassword(Long id) {
        EduStudent exist = eduStudentMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
        }
        eduStudentMapper.updatePassword(id, passwordEncoder.encode(DEFAULT_PASSWORD_SM3));
        log.info("学员 {} 密码已重置为默认密码，操作者={}", id, SecurityUtils.getCurrentUserId());
    }
}
