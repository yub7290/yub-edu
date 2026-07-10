package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.GroupCourseReqDTO;
import com.yub.edu.biz.dto.GroupMemberReqDTO;
import com.yub.edu.biz.dto.StatusReqDTO;
import com.yub.edu.biz.dto.StudentGroupCreateReqDTO;
import com.yub.edu.biz.dto.StudentGroupQueryDTO;
import com.yub.edu.biz.dto.StudentGroupUpdateReqDTO;
import com.yub.edu.biz.service.StudentGroupService;
import com.yub.edu.biz.vo.StudentGroupDetailRespVO;
import com.yub.edu.biz.vo.StudentGroupPageRespVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学员组 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/student-group")
@RequiredArgsConstructor
public class StudentGroupController {

    private final StudentGroupService studentGroupService;

    /**
     * 分页查询学员组列表
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<StudentGroupPageRespVO>> page(@RequestBody PageQuery<StudentGroupQueryDTO> pageQuery) {
        return Response.success(studentGroupService.page(pageQuery));
    }

    /**
     * 获取学员组详情
     *
     * @param id 学员组ID
     * @return 学员组详情
     */
    @GetMapping("/{id}")
    public Response<StudentGroupDetailRespVO> getDetail(@PathVariable("id") Long id) {
        return Response.success(studentGroupService.getDetail(id));
    }

    /**
     * 新增学员组
     *
     * @param dto 新增参数
     * @return 学员组ID
     */
    @Log(value = "新增学员组", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody StudentGroupCreateReqDTO dto) {
        return Response.success(studentGroupService.create(dto));
    }

    /**
     * 编辑学员组
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @Log(value = "编辑学员组", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody StudentGroupUpdateReqDTO dto) {
        studentGroupService.update(dto);
        return Response.success();
    }

    /**
     * 删除学员组
     *
     * @param id 学员组ID
     * @return 响应
     */
    @Log(value = "删除学员组", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        studentGroupService.delete(id);
        return Response.success();
    }

    /**
     * 切换学员组状态
     *
     * @param id  学员组ID
     * @param dto 状态参数
     * @return 响应
     */
    @Log(value = "切换学员组状态", type = "UPDATE")
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable("id") Long id, @Valid @RequestBody StatusReqDTO dto) {
        studentGroupService.changeStatus(id, dto.getStatus());
        return Response.success();
    }

    /**
     * 设置课程（批量添加）
     *
     * @param id      学员组ID
     * @param courses 课程列表
     * @return 响应
     */
    @Log(value = "设置学员组课程", type = "UPDATE")
    @PostMapping("/{id}/courses")
    public Response<Void> setCourses(@PathVariable("id") Long id, @Valid @RequestBody List<GroupCourseReqDTO> courses) {
        studentGroupService.setCourses(id, courses);
        return Response.success();
    }

    /**
     * 移除课程（批量）
     *
     * @param id        学员组ID
     * @param courseIds 课程ID列表
     * @return 响应
     */
    @Log(value = "移除学员组课程", type = "DELETE")
    @DeleteMapping("/{id}/courses")
    public Response<Void> removeCourses(@PathVariable("id") Long id, @RequestBody List<Long> courseIds) {
        studentGroupService.removeCourses(id, courseIds);
        return Response.success();
    }

    /**
     * 学员组课程排序
     *
     * @param id        学员组ID
     * @param courseIds 按新顺序排列的课程ID列表
     * @return 响应
     */
    @Log(value = "学员组课程排序", type = "UPDATE")
    @PutMapping("/{id}/courses/sort")
    public Response<Void> sortCourses(@PathVariable("id") Long id, @RequestBody List<Long> courseIds) {
        studentGroupService.sortCourses(id, courseIds);
        return Response.success();
    }

    /**
     * 添加学员（批量）
     *
     * @param id      学员组ID
     * @param members 学员列表
     * @return 响应
     */
    @Log(value = "添加学员组成员", type = "UPDATE")
    @PostMapping("/{id}/members")
    public Response<Void> addMembers(@PathVariable("id") Long id, @Valid @RequestBody List<GroupMemberReqDTO> members) {
        studentGroupService.addMembers(id, members);
        return Response.success();
    }

    /**
     * 移除学员（批量）
     *
     * @param id         学员组ID
     * @param studentIds 学员ID列表
     * @return 响应
     */
    @Log(value = "移除学员组成员", type = "DELETE")
    @DeleteMapping("/{id}/members")
    public Response<Void> removeMembers(@PathVariable("id") Long id, @RequestBody List<Long> studentIds) {
        studentGroupService.removeMembers(id, studentIds);
        return Response.success();
    }
}
