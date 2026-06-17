package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.framework.security.SecurityUtils;
import com.yub.framework.util.BeanUtils;
import com.yub.edu.biz.dto.TeacherTitleCreateReqDTO;
import com.yub.edu.biz.dto.TeacherTitleQueryDTO;
import com.yub.edu.biz.dto.TeacherTitleUpdateReqDTO;
import com.yub.edu.biz.entity.EduTeacherTitle;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduTeacherTitleMapper;
import com.yub.edu.biz.service.TeacherTitleService;
import com.yub.edu.biz.vo.TeacherTitleDetailRespVO;
import com.yub.edu.biz.vo.TeacherTitlePageRespVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 教师职称 Service 实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师职称业务实现
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherTitleServiceImpl implements TeacherTitleService {

    private final EduTeacherTitleMapper eduTeacherTitleMapper;

    @Override
    public PageResult<TeacherTitlePageRespVO> page(PageQuery<TeacherTitleQueryDTO> pageQuery) {
        PageHelper.startPage(pageQuery.getPageParam().getPageNum(), pageQuery.getPageParam().getPageSize());
        List<EduTeacherTitle> list = eduTeacherTitleMapper.selectPage(pageQuery.getQueryParam());
        PageInfo<EduTeacherTitle> pageInfo = new PageInfo<>(list);

        List<TeacherTitlePageRespVO> records = BeanUtils.copyList(list, TeacherTitlePageRespVO.class);

        // 填充教师数量和课程数
        for (TeacherTitlePageRespVO vo : records) {
            int teacherCount = eduTeacherTitleMapper.countTeachersByTitleId(vo.getId());
            vo.setTeacherCount(teacherCount);
            vo.setCourseCount(0); // 课程数暂为0，后续可通过课程表关联统计
        }

        return PageResult.of(records, pageInfo.getTotal());
    }

    @Override
    public TeacherTitleDetailRespVO getDetail(Long id) {
        EduTeacherTitle title = eduTeacherTitleMapper.selectById(id);
        if (title == null) {
            throw new EduException(EduErrorCode.TEACHER_TITLE_NOT_FOUND);
        }
        return TeacherTitleDetailRespVO.builder()
                .id(title.getId())
                .name(title.getName())
                .remark(title.getRemark())
                .status(title.getStatus())
                .createTime(title.getCreateTime())
                .updateTime(title.getUpdateTime())
                .build();
    }

    @Override
    @Transactional
    public Long create(TeacherTitleCreateReqDTO req) {
        EduTeacherTitle title = new EduTeacherTitle();
        title.setName(req.getName());
        title.setRemark(req.getRemark());
        title.setStatus(req.getStatus());
        Long currentUserId = SecurityUtils.getCurrentUserId();
        title.setCreateBy(currentUserId);
        title.setUpdateBy(currentUserId);
        eduTeacherTitleMapper.insert(title);
        return title.getId();
    }

    @Override
    @Transactional
    public void update(TeacherTitleUpdateReqDTO req) {
        EduTeacherTitle exist = eduTeacherTitleMapper.selectById(req.getId());
        if (exist == null) {
            throw new EduException(EduErrorCode.TEACHER_TITLE_NOT_FOUND);
        }
        EduTeacherTitle title = new EduTeacherTitle();
        title.setId(req.getId());
        title.setName(req.getName());
        title.setRemark(req.getRemark());
        title.setStatus(req.getStatus());
        title.setUpdateBy(SecurityUtils.getCurrentUserId());
        eduTeacherTitleMapper.updateById(title);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EduTeacherTitle exist = eduTeacherTitleMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.TEACHER_TITLE_NOT_FOUND);
        }
        // 检查是否有教师关联
        int teacherCount = eduTeacherTitleMapper.countTeachersByTitleId(id);
        if (teacherCount > 0) {
            throw new EduException(EduErrorCode.TEACHER_TITLE_HAS_TEACHERS);
        }
        eduTeacherTitleMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, Integer status) {
        EduTeacherTitle exist = eduTeacherTitleMapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.TEACHER_TITLE_NOT_FOUND);
        }
        eduTeacherTitleMapper.updateStatus(id, status);
    }

    @Override
    public List<EduTeacherTitle> selectAllEnabled() {
        return eduTeacherTitleMapper.selectAllEnabled();
    }
}
