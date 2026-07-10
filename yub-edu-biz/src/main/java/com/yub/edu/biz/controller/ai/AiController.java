package com.yub.edu.biz.controller.ai;

import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.service.HomeworkCorrectionService;
import com.yub.edu.biz.vo.HomeworkCorrectionVO;
import com.yub.edu.biz.vo.HomeworkPageVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI统一入口 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: AI相关功能统一入口，作业批改已迁移到 StudentHomeworkController
 * @Version: 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final HomeworkCorrectionService homeworkCorrectionService;

    /**
     * 查询作业批改记录列表（兼容旧路径）
     *
     * @param courseId  课程ID（可选）
     * @param pageNum   页码
     * @param pageSize  每页条数
     * @return 分页结果
     */
    @GetMapping("/homework/list")
    public Response<PageResult<HomeworkPageVO>> homeworkList(
            @RequestParam(name = "courseId", required = false) Long courseId,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        Long studentId = SecurityUtils.getCurrentUserId();
        PageResult<HomeworkPageVO> result = homeworkCorrectionService.listByStudent(studentId, courseId, pageNum, pageSize);
        return Response.success(result);
    }

    /**
     * 查询作业批改详情（兼容旧路径）
     *
     * @param id 批改记录ID
     * @return 批改详情
     */
    @GetMapping("/homework/detail/{id}")
    public Response<HomeworkCorrectionVO> homeworkDetail(@PathVariable("id") Long id) {
        Long studentId = SecurityUtils.getCurrentUserId();
        HomeworkCorrectionVO vo = homeworkCorrectionService.getDetail(studentId, id);
        return Response.success(vo);
    }
}
