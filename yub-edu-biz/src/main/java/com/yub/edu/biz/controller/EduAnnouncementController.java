package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.AnnouncementCreateReqDTO;
import com.yub.edu.biz.dto.AnnouncementQueryDTO;
import com.yub.edu.biz.dto.AnnouncementUpdateReqDTO;
import com.yub.edu.biz.service.EduAnnouncementService;
import com.yub.edu.biz.vo.AnnouncementVO;
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
 * 公告管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 公告管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/announcement")
@RequiredArgsConstructor
public class EduAnnouncementController {

    private final EduAnnouncementService eduAnnouncementService;

    /**
     * 分页查询公告
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<AnnouncementVO>> page(@RequestBody PageQuery<AnnouncementQueryDTO> pageQuery) {
        // 教师身份自动注入 teacherId 过滤，仅返回该教师归属课程的公告；管理员无此限制
        if (JwtProvider.USER_TYPE_TEACHER.equals(SecurityUtils.getCurrentUserType())) {
            pageQuery.getQueryParam().setTeacherId(SecurityUtils.getCurrentUserId());
        }
        return Response.success(eduAnnouncementService.page(pageQuery));
    }

    /**
     * 获取公告详情
     *
     * @param id 公告ID
     * @return 公告详情
     */
    @GetMapping("/{id}")
    public Response<AnnouncementVO> getDetail(@PathVariable("id") Long id) {
        return Response.success(eduAnnouncementService.getDetail(id));
    }

    /**
     * 新增公告
     *
     * @param dto 新增参数
     * @return 公告ID
     */
    @Log(value = "新增公告", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody AnnouncementCreateReqDTO dto) {
        return Response.success(eduAnnouncementService.create(dto));
    }

    /**
     * 编辑公告
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @Log(value = "编辑公告", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody AnnouncementUpdateReqDTO dto) {
        eduAnnouncementService.update(dto);
        return Response.success();
    }

    /**
     * 删除公告
     *
     * @param id 公告ID
     * @return 响应
     */
    @Log(value = "删除公告", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        eduAnnouncementService.delete(id);
        return Response.success();
    }
}
