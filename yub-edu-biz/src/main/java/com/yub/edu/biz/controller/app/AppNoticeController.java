package com.yub.edu.biz.controller.app;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.NoticeQueryDTO;
import com.yub.edu.biz.service.EduNoticeService;
import com.yub.edu.biz.vo.NoticeUnreadVO;
import com.yub.edu.biz.vo.NoticeVO;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学员端通知 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 学员端通知接口（仅返回所绑定课程下已发布的通知）
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/app/notice")
@RequiredArgsConstructor
public class AppNoticeController {

    private final EduNoticeService eduNoticeService;

    /**
     * 学员端通知列表（分页，含是否已读标记）
     *
     * @param body 分页与查询参数（queryParam.type 可选）
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<NoticeVO>> page(@RequestBody PageQuery<NoticeQueryDTO> pageQuery) {
        NoticeQueryDTO queryParam = pageQuery.getQueryParam();
        Integer type = queryParam != null ? queryParam.getType() : null;
        Long studentId = SecurityUtils.getCurrentUserId();
        return Response.success(eduNoticeService.appPage(
                studentId, type,
                pageQuery.getPageParam().getPageNum(),
                pageQuery.getPageParam().getPageSize()));
    }

    /**
     * 学员端通知详情（打开即标记已读）
     *
     * @param id 通知ID
     * @return 通知详情
     */
    @GetMapping("/{id}")
    public Response<NoticeVO> getDetail(@PathVariable("id") Long id) {
        return Response.success(eduNoticeService.appDetail(SecurityUtils.getCurrentUserId(), id));
    }

    /**
     * 学员端未读通知数
     *
     * @return 未读数
     */
    @GetMapping("/unread/count")
    public Response<NoticeUnreadVO> unreadCount() {
        return Response.success(NoticeUnreadVO.builder()
                .count(eduNoticeService.unreadCount(SecurityUtils.getCurrentUserId()))
                .build());
    }
}
