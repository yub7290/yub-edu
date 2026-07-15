package com.yub.edu.biz.controller;

import com.yub.common.model.PageParam;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.HomeworkSubmitDTO;
import com.yub.edu.biz.dto.StudentHomeworkQueryDTO;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.service.HomeworkCorrectionService;
import com.yub.edu.biz.vo.HomeworkCorrectionVO;
import com.yub.edu.biz.vo.HomeworkPageVO;
import com.yub.framework.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生端作业批改 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 学生端作业批改接口
 * @Version: 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/student/homework")
@RequiredArgsConstructor
public class StudentHomeworkController {

    private final HomeworkCorrectionService homeworkCorrectionService;

    /**
     * 提交作业批改（同步等待AI完成）
     *
     * @param dto 提交参数
     * @return 批改结果详情
     */
    @PostMapping("/submit")
    public Response<HomeworkCorrectionVO> submit(@RequestBody @Valid HomeworkSubmitDTO dto) {
        Long studentId = SecurityUtils.getCurrentUserId();
        HomeworkCorrectionVO vo = homeworkCorrectionService.submit(studentId, dto);
        return Response.success(vo);
    }

    /**
     * 分页查询当前学生的批改记录列表
     * <p>遵循项目分页规范：POST + PageQuery 风格（queryParam 承载查询条件，pageParam 承载分页参数），
     * 与管理端 {@code EduHomeworkController#page} 保持一致的契约。
     *
     * @param pageQuery 查询体（queryParam.courseId 必填，pageParam 分页可选）
     * @return 分页结果（records / total）
     */
    @PostMapping("/list")
    public Response<PageResult<HomeworkPageVO>> list(@RequestBody @Valid PageQuery<StudentHomeworkQueryDTO> pageQuery) {
        StudentHomeworkQueryDTO queryParam = pageQuery.getQueryParam();
        if (queryParam == null || queryParam.getCourseId() == null) {
            throw new EduException(EduErrorCode.HOMEWORK_COURSE_REQUIRED);
        }
        PageParam pageParam = pageQuery.getPageParam();
        int pageNum = pageParam != null ? pageParam.getPageNum() : 1;
        int pageSize = pageParam != null ? pageParam.getPageSize() : 10;

        Long studentId = SecurityUtils.getCurrentUserId();
        PageResult<HomeworkPageVO> result = homeworkCorrectionService.listByStudent(studentId, queryParam.getCourseId(), pageNum, pageSize);
        return Response.success(result);
    }

    /**
     * 查询批改记录详情
     *
     * @param id 批改记录ID
     * @return 批改详情
     */
    @GetMapping("/detail/{id}")
    public Response<HomeworkCorrectionVO> detail(@PathVariable("id") Long id) {
        Long studentId = SecurityUtils.getCurrentUserId();
        HomeworkCorrectionVO vo = homeworkCorrectionService.getDetail(studentId, id);
        return Response.success(vo);
    }
}
