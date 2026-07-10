package com.yub.edu.biz.controller;

import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.HomeworkSubmitDTO;
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
     * 查询当前学生的批改记录列表
     *
     * @param courseId  课程ID（可选）
     * @param pageNum   页码
     * @param pageSize  每页条数
     * @return 分页结果
     */
    @GetMapping("/list")
    public Response<PageResult<HomeworkPageVO>> list(
            @RequestParam(name = "courseId", required = false) Long courseId,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        Long studentId = SecurityUtils.getCurrentUserId();
        PageResult<HomeworkPageVO> result = homeworkCorrectionService.listByStudent(studentId, courseId, pageNum, pageSize);
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
