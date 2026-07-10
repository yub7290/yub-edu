package com.yub.edu.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.model.PageParam;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.NoticeCreateReqDTO;
import com.yub.edu.biz.dto.NoticeQueryDTO;
import com.yub.edu.biz.dto.NoticeUpdateReqDTO;
import com.yub.edu.biz.entity.EduNotice;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduNoticeMapper;
import com.yub.edu.biz.service.EduNoticeService;
import com.yub.edu.biz.vo.NoticeStatsVO;
import com.yub.edu.biz.vo.NoticeVO;
import com.yub.framework.security.JwtProvider;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程通知服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 课程通知服务实现（通知与课程关联，仅发给绑定了该课程的学员）
 * @Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduNoticeServiceImpl implements EduNoticeService {

    private final EduNoticeMapper mapper;

    @Override
    public PageResult<NoticeVO> page(PageQuery<NoticeQueryDTO> pageQuery) {
        NoticeQueryDTO queryParam = pageQuery.getQueryParam();
        PageParam pageParam = pageQuery.getPageParam();

        // 教师身份自动注入 teacherId 过滤，仅返回该教师归属课程的通知；管理员无此限制
        if (JwtProvider.USER_TYPE_TEACHER.equals(SecurityUtils.getCurrentUserType())) {
            queryParam.setTeacherId(SecurityUtils.getCurrentUserId());
        }

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<NoticeVO> list = mapper.selectPage(
                queryParam.getCourseId(),
                queryParam.getTitle(),
                queryParam.getStatus(),
                queryParam.getType(),
                queryParam.getTeacherId()
        );
        PageInfo<NoticeVO> pageInfo = new PageInfo<>(list);
        return PageResult.of(fillTimeStr(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public NoticeVO getDetail(Long id) {
        NoticeVO vo = mapper.selectById(id);
        if (vo == null) {
            throw new EduException(EduErrorCode.NOTICE_NOT_FOUND);
        }
        fillTimeStr(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(NoticeCreateReqDTO dto) {
        EduNotice entity = new EduNotice();
        entity.setCourseId(dto.getCourseId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setType(dto.getType() != null ? dto.getType() : 1);
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        entity.setCreateBy(currentUserId);
        entity.setUpdateBy(currentUserId);
        mapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NoticeUpdateReqDTO dto) {
        NoticeVO exist = mapper.selectById(dto.getId());
        if (exist == null) {
            throw new EduException(EduErrorCode.NOTICE_NOT_FOUND);
        }
        EduNotice entity = new EduNotice();
        entity.setId(dto.getId());
        entity.setCourseId(dto.getCourseId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setType(dto.getType() != null ? dto.getType() : 1);
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        entity.setUpdateBy(SecurityUtils.getCurrentUserId());
        mapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        NoticeVO exist = mapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.NOTICE_NOT_FOUND);
        }
        mapper.deleteById(id);
    }

    @Override
    public NoticeStatsVO stats(Long id) {
        NoticeVO exist = mapper.selectById(id);
        if (exist == null) {
            throw new EduException(EduErrorCode.NOTICE_NOT_FOUND);
        }
        int receivers = mapper.countReceivers(exist.getCourseId());
        int readCount = mapper.countRead(id);
        int unreadCount = Math.max(receivers - readCount, 0);
        return NoticeStatsVO.builder()
                .receivers(receivers)
                .readCount(readCount)
                .unreadCount(unreadCount)
                .build();
    }

    @Override
    public PageResult<NoticeVO> appPage(Long studentId, Integer type, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<NoticeVO> list = mapper.selectStudentPage(studentId, type);
        PageInfo<NoticeVO> pageInfo = new PageInfo<>(list);
        return PageResult.of(fillTimeStr(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NoticeVO appDetail(Long studentId, Long id) {
        NoticeVO vo = mapper.selectById(id);
        if (vo == null) {
            throw new EduException(EduErrorCode.NOTICE_NOT_FOUND);
        }
        // 打开即标记已读（已读则忽略，依赖唯一索引）
        mapper.markRead(id, studentId);
        vo.setReadFlag(1);
        fillTimeStr(vo);
        return vo;
    }

    @Override
    public int unreadCount(Long studentId) {
        return mapper.countStudentUnread(studentId);
    }

    private List<NoticeVO> fillTimeStr(List<NoticeVO> list) {
        return list.stream().peek(this::fillTimeStr).collect(Collectors.toList());
    }

    private void fillTimeStr(NoticeVO vo) {
        if (vo.getCreateTime() != null) {
            vo.setCreateTimeStr(vo.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
}
