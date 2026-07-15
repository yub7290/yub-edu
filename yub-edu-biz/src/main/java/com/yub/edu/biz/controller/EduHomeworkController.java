package com.yub.edu.biz.controller;

import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.HomeworkAutoReviewDTO;
import com.yub.edu.biz.dto.HomeworkBatchDeleteDTO;
import com.yub.edu.biz.dto.HomeworkReviewDTO;
import com.yub.edu.biz.service.HomeworkCorrectionService;
import com.yub.edu.biz.vo.HomeworkCorrectionVO;
import com.yub.edu.biz.vo.HomeworkPageVO;
import com.yub.framework.security.JwtProvider;
import com.yub.framework.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理端作业批改 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 管理端作业批改管理接口，支持 ADMIN/TEACHER 双身份数据权限
 * @Version: 1.1.0
 */
@Slf4j
@RestController
@RequestMapping("/edu/homework")
@RequiredArgsConstructor
public class EduHomeworkController {

    private final HomeworkCorrectionService homeworkCorrectionService;

    /**
     * 分页查询作业批改记录
     * <p>教师身份自动限制为该教师绑定课程下的作业
     *
     * @param body 查询参数
     * @return 分页结果
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/page")
    public Response<PageResult<HomeworkPageVO>> page(@RequestBody Map<String, Object> body) {
        Map<String, Object> queryParam = (Map<String, Object>) body.getOrDefault("queryParam", Map.of());
        Map<String, Object> pageParam = (Map<String, Object>) body.getOrDefault("pageParam", Map.of());
        int pageNum = pageParam.containsKey("pageNum") ? ((Number) pageParam.get("pageNum")).intValue() : 1;
        int pageSize = pageParam.containsKey("pageSize") ? ((Number) pageParam.get("pageSize")).intValue() : 10;

        // ponytail: 教师身份自动注入 teacherId 过滤，ADMIN 无此限制
        Map<String, Object> enrichedParam = new HashMap<>(queryParam);
        if (JwtProvider.USER_TYPE_TEACHER.equals(SecurityUtils.getCurrentUserType())) {
            enrichedParam.put("teacherId", SecurityUtils.getCurrentUserId());
        }

        PageResult<HomeworkPageVO> result = homeworkCorrectionService.page(enrichedParam, pageNum, pageSize);
        return Response.success(result);
    }

    /**
     * 查询作业批改详情
     *
     * @param id 批改记录ID
     * @return 批改详情
     */
    @GetMapping("/detail/{id}")
    public Response<HomeworkCorrectionVO> detail(@PathVariable("id") Long id) {
        HomeworkCorrectionVO vo = homeworkCorrectionService.getDetailForAdmin(id);
        return Response.success(vo);
    }

    /**
     * 复查题目
     *
     * @param dto 复查参数
     * @return 操作结果
     */
    @PostMapping("/review")
    public Response<Void> review(@RequestBody @Valid HomeworkReviewDTO dto) {
        Long adminId = SecurityUtils.getCurrentUserId();
        homeworkCorrectionService.reviewQuestion(adminId, dto);
        return Response.success(null);
    }

    /**
     * 一键复查：将「AI 判定正确」的题目批量标记为已复查
     * <p>采用 AI 判定答案，不写回教师答案；教师身份受课程归属隔离
     *
     * @param dto 批改记录ID
     * @return 已复查题数
     */
    @PostMapping("/review/auto")
    public Response<Integer> reviewAuto(@RequestBody @Valid HomeworkAutoReviewDTO dto) {
        Long operatorId = SecurityUtils.getCurrentUserId();
        int count = homeworkCorrectionService.autoReviewCorrectQuestions(operatorId, dto.getCorrectionId());
        return Response.success(count);
    }

    /**
     * 批量删除作业题目（物理删除，不可恢复）
     * <p>删除后由后端自动重算总题数/正确数/得分并写回；教师身份受课程归属隔离
     *
     * @param dto 题目ID列表
     * @return 操作结果
     */
    @PostMapping("/question/batch-delete")
    public Response<Void> batchDeleteQuestions(@RequestBody @Valid HomeworkBatchDeleteDTO dto) {
        Long operatorId = SecurityUtils.getCurrentUserId();
        homeworkCorrectionService.batchDeleteQuestions(operatorId, dto.getIds());
        return Response.success(null);
    }

    /**
     * 删除作业批改记录
     *
     * @param id 批改记录ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        homeworkCorrectionService.delete(id);
        return Response.success(null);
    }
}
