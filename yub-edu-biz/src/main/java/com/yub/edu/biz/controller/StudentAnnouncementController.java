package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.service.EduAnnouncementService;
import com.yub.edu.biz.vo.AnnouncementVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学生端公告 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 学生端课程公告接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/student/course")
@RequiredArgsConstructor
public class StudentAnnouncementController {

    private final EduAnnouncementService eduAnnouncementService;

    /**
     * 获取课程已启用的公告列表
     *
     * @param courseId 课程ID
     * @return 公告列表
     */
    @GetMapping("/{courseId}/announcement/list")
    public Response<List<AnnouncementVO>> list(@PathVariable Long courseId) {
        return Response.success(eduAnnouncementService.listByCourse(courseId));
    }

    /**
     * 获取公告详情（仅返回已启用的公告）
     *
     * @param id 公告ID
     * @return 公告详情
     */
    @GetMapping("/announcement/{id}")
    public Response<AnnouncementVO> getDetail(@PathVariable Long id) {
        return Response.success(eduAnnouncementService.getActiveDetail(id));
    }
}
