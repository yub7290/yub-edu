package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.NoticeCreateReqDTO;
import com.yub.edu.biz.dto.NoticeQueryDTO;
import com.yub.edu.biz.dto.NoticeUpdateReqDTO;
import com.yub.edu.biz.service.EduNoticeService;
import com.yub.edu.biz.vo.NoticeStatsVO;
import com.yub.edu.biz.vo.NoticeVO;
import com.yub.framework.security.JwtProvider;
import com.yub.framework.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程通知管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 课程通知管理接口（通知与课程关联，仅发给绑定了该课程的学员）
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/notice")
@RequiredArgsConstructor
public class EduNoticeController {

    private final EduNoticeService eduNoticeService;

    /**
     * 分页查询通知
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<NoticeVO>> page(@RequestBody PageQuery<NoticeQueryDTO> pageQuery) {
        // 教师身份自动注入 teacherId 过滤，仅返回该教师归属课程的通知；管理员无此限制
        if (JwtProvider.USER_TYPE_TEACHER.equals(SecurityUtils.getCurrentUserType())) {
            pageQuery.getQueryParam().setTeacherId(SecurityUtils.getCurrentUserId());
        }
        return Response.success(eduNoticeService.page(pageQuery));
    }

    /**
     * 获取通知详情
     *
     * @param id 通知ID
     * @return 通知详情
     */
    @GetMapping("/{id}")
    public Response<NoticeVO> getDetail(@PathVariable("id") Long id) {
        return Response.success(eduNoticeService.getDetail(id));
    }

    /**
     * 获取通知阅读统计
     *
     * @param id 通知ID
     * @return 阅读统计
     */
    @GetMapping("/{id}/stats")
    public Response<NoticeStatsVO> stats(@PathVariable("id") Long id) {
        return Response.success(eduNoticeService.stats(id));
    }

    /**
     * 新增通知
     *
     * @param dto 新增参数
     * @return 通知ID
     */
    @Log(value = "新增通知", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody NoticeCreateReqDTO dto) {
        return Response.success(eduNoticeService.create(dto));
    }

    /**
     * 编辑通知
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @Log(value = "编辑通知", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody NoticeUpdateReqDTO dto) {
        eduNoticeService.update(dto);
        return Response.success();
    }

    /**
     * 删除通知
     *
     * @param id 通知ID
     * @return 响应
     */
    @Log(value = "删除通知", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        eduNoticeService.delete(id);
        return Response.success();
    }
}
