package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.util.SM3Utils;
import com.yub.edu.api.dto.app.ProfileInfoUpdateDTO;
import com.yub.edu.api.vo.app.ProfileInfoRespVO;
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
import com.yub.framework.security.SecurityUtils;
import com.yub.framework.util.BeanUtils;
import com.yub.system.mapper.param.SysParamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import static com.yub.system.constants.param.ParamConstants.SYS_DEFAULT_PASSWORD;
import static com.yub.system.constants.param.ParamKeyConstants.USER_DEFAULT_PASSWORD;

/**
 * 学员 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 学员业务实现
 * @Version: 1.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final EduStudentMapper eduStudentMapper;
    private final PasswordEncoder passwordEncoder;
    // TODO: 架构治理 - 跨模块依赖: yub-edu 不应直接依赖 yub-system 的 Mapper
    private final SysParamMapper sysParamMapper;

    /**
     * 从 sys_param 获取默认密码 SM3 哈希，配置缺失时兜底 123456
     */
    private String getDefaultPasswordSm3() {
        String value = sysParamMapper.selectValueByCode(USER_DEFAULT_PASSWORD);
        return StringUtils.isNotBlank(value) ? value : SM3Utils.hash(SYS_DEFAULT_PASSWORD);
    }

    @Override
    public PageResult<StudentPageRespVO> page(PageQuery<StudentQueryDTO> pageQuery) {
        PageHelper.startPage(pageQuery.getPageParam().getPageNum(), pageQuery.getPageParam().getPageSize());
        List<EduStudent> list = eduStudentMapper.selectPage(pageQuery.getQueryParam());
        PageInfo<EduStudent> pageInfo = new PageInfo<>(list);
        log.debug("学员分页: pageNum={}, pageSize={}, total={}, listSize={}",
                pageQuery.getPageParam().getPageNum(),
                pageQuery.getPageParam().getPageSize(),
                pageInfo.getTotal(), list.size());

        List<StudentPageRespVO> records = list.stream()
                .map(this::convertToPageVO)
                .collect(Collectors.toList());

        return PageResult.of(records, pageInfo.getTotal());
    }

    private StudentPageRespVO convertToPageVO(EduStudent student) {
        StudentPageRespVO vo = BeanUtils.copy(student, StudentPageRespVO.class);
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
                        ? req.getPassword() : getDefaultPasswordSm3()));
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
        eduStudentMapper.updatePassword(id, passwordEncoder.encode(getDefaultPasswordSm3()));
        log.info("学员 {} 密码已重置为默认密码，操作者={}", id, SecurityUtils.getCurrentUserId());
    }

    @Override
    public ProfileInfoRespVO getProfileInfo(Long id) {
        EduStudent student = eduStudentMapper.selectById(id);
        if (student == null) {
            throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
        }
        return ProfileInfoRespVO.builder()
                .id(student.getId())
                .nickname(student.getNickName())
                .avatarUrl(student.getAvatarUrl())
                .realName(student.getName())
                .phone(student.getPhone())
                .email(student.getEmail())
                .gender(student.getGender())
                .birthday(student.getBirthDate())
                .schoolName(student.getSchool())
                .build();
    }

    @Override
    public void updateProfileInfo(Long id, ProfileInfoUpdateDTO req) {
        EduStudent student = eduStudentMapper.selectById(id);
        if (student == null) {
            throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
        }
        student.setAvatarUrl(req.getAvatarUrl());
        student.setNickName(req.getNickname());
        student.setPhone(req.getPhone());
        student.setEmail(req.getEmail());
        student.setGender(req.getGender());
        if (req.getBirthday() != null) {
            student.setBirthDate(req.getBirthday());
        }
        student.setSchool(req.getSchoolName());
        eduStudentMapper.updateById(student);
    }

    @Override
    public void changePassword(Long id, String oldPasswordSm3, String newPasswordSm3) {
        EduStudent student = eduStudentMapper.selectById(id);
        if (student == null) {
            throw new EduException(EduErrorCode.STUDENT_NOT_FOUND);
        }
        if (!passwordEncoder.matches(oldPasswordSm3, student.getPassword())) {
            throw new EduException(EduErrorCode.OLD_PASSWORD_ERROR);
        }
        int strongCount = (newPasswordSm3.matches(".*[A-Z].*") ? 1 : 0)
                + (newPasswordSm3.matches(".*[a-z].*") ? 1 : 0)
                + (newPasswordSm3.matches(".*\\d.*") ? 1 : 0)
                + (newPasswordSm3.matches(".*[^A-Za-z0-9].*") ? 1 : 0);
        if (strongCount < 3) {
            throw new EduException(EduErrorCode.PASSWORD_STRENGTH_ERROR);
        }
        eduStudentMapper.updatePassword(id, passwordEncoder.encode(newPasswordSm3));
    }
}
