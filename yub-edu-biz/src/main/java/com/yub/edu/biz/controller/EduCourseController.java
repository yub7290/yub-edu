package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.CourseCreateReqDTO;
import com.yub.edu.biz.dto.CourseQueryDTO;
import com.yub.edu.biz.dto.CourseUpdateReqDTO;
import com.yub.edu.biz.dto.StatusReqDTO;
import com.yub.edu.biz.service.EduCourseService;
import com.yub.edu.biz.vo.CourseDetailRespVO;
import com.yub.edu.biz.vo.CourseOverviewRespVO;
import com.yub.edu.biz.vo.CoursePageRespVO;
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
 * 课程管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 课程管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/course")
@RequiredArgsConstructor
public class EduCourseController {

    private final EduCourseService eduCourseService;

    /**
     * 分页查询课程
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<CoursePageRespVO>> page(@RequestBody PageQuery<CourseQueryDTO> pageQuery) {
        return Response.success(eduCourseService.page(pageQuery));
    }

    /**
     * 获取课程详情
     *
     * @param id 课程ID
     * @return 课程详情
     */
    @GetMapping("/{id}")
    public Response<CourseDetailRespVO> getDetail(@PathVariable Long id) {
        return Response.success(eduCourseService.getDetail(id));
    }

    /**
     * 获取课程综述
     *
     * @param id 课程ID
     * @return 课程综述
     */
    @GetMapping("/{id}/overview")
    public Response<CourseOverviewRespVO> getOverview(@PathVariable Long id) {
        return Response.success(eduCourseService.getOverview(id));
    }

    /**
     * 新增课程
     *
     * @param dto 新增参数
     * @return 课程ID
     */
    @Log(value = "新增课程", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody CourseCreateReqDTO dto) {
        return Response.success(eduCourseService.create(dto));
    }

    /**
     * 编辑课程
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @Log(value = "编辑课程", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody CourseUpdateReqDTO dto) {
        eduCourseService.update(dto);
        return Response.success();
    }

    /**
     * 删除课程
     *
     * @param id 课程ID
     * @return 响应
     */
    @Log(value = "删除课程", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        eduCourseService.delete(id);
        return Response.success();
    }

    /**
     * 切换课程状态
     *
     * @param id  课程ID
     * @param dto 状态参数
     * @return 响应
     */
    @Log(value = "切换课程状态", type = "UPDATE")
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable Long id, @Valid @RequestBody StatusReqDTO dto) {
        eduCourseService.changeStatus(id, dto.getStatus());
        return Response.success();
    }

    /**
     * 设置推荐课程
     *
     * @param id          课程ID
     * @param recommended 是否推荐
     * @return 响应
     */
    @Log(value = "设置课程推荐状态", type = "UPDATE")
    @PutMapping("/{id}/recommended")
    public Response<Void> setRecommended(@PathVariable Long id, @Valid @RequestBody StatusReqDTO recommended) {
        eduCourseService.setRecommended(id, recommended.getStatus());
        return Response.success();
    }
}
