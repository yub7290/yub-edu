package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.StatusReqDTO;
import com.yub.edu.biz.dto.StudentCreateReqDTO;
import com.yub.edu.biz.dto.StudentQueryDTO;
import com.yub.edu.biz.dto.StudentUpdateReqDTO;
import com.yub.edu.biz.service.StudentService;
import com.yub.edu.biz.vo.StudentDetailRespVO;
import com.yub.edu.biz.vo.StudentPageRespVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学员信息 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 学员信息接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * 分页查询学员列表
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<StudentPageRespVO>> page(@RequestBody PageQuery<StudentQueryDTO> pageQuery) {
        return Response.success(studentService.page(pageQuery));
    }

    /**
     * 获取学员详情
     *
     * @param id 学员ID
     * @return 学员详情
     */
    @GetMapping("/{id}")
    public Response<StudentDetailRespVO> getDetail(@PathVariable Long id) {
        return Response.success(studentService.getDetail(id));
    }

    /**
     * 新增学员
     *
     * @param dto 新增参数
     * @return 学员ID
     */
    @Log(value = "新增学员", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody StudentCreateReqDTO dto) {
        return Response.success(studentService.create(dto));
    }

    /**
     * 编辑学员
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @Log(value = "编辑学员", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody StudentUpdateReqDTO dto) {
        studentService.update(dto);
        return Response.success();
    }

    /**
     * 删除学员
     *
     * @param id 学员ID
     * @return 响应
     */
    @Log(value = "删除学员", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return Response.success();
    }

    /**
     * 批量删除学员
     *
     * @param ids 学员ID列表
     * @return 响应
     */
    @Log(value = "批量删除学员", type = "DELETE")
    @DeleteMapping("/batch")
    public Response<Void> deleteBatch(@RequestBody List<Long> ids) {
        studentService.deleteBatch(ids);
        return Response.success();
    }

    /**
     * 切换学员状态
     *
     * @param id  学员ID
     * @param dto 状态参数
     * @return 响应
     */
    @Log(value = "切换学员状态", type = "UPDATE")
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable Long id, @Valid @RequestBody StatusReqDTO dto) {
        studentService.changeStatus(id, dto.getStatus());
        return Response.success();
    }

    /**
     * 批量禁用学员
     *
     * @param ids 学员ID列表
     * @return 响应
     */
    @Log(value = "批量禁用学员", type = "UPDATE")
    @PutMapping("/batch-disable")
    public Response<Void> batchDisable(@RequestBody List<Long> ids) {
        studentService.batchDisable(ids);
        return Response.success();
    }

    /**
     * 重置密码
     *
     * @param id 学员ID
     * @return 响应
     */
    @Log(value = "重置学员密码", type = "UPDATE")
    @PutMapping("/{id}/reset-password")
    public Response<Void> resetPassword(@PathVariable Long id) {
        studentService.resetPassword(id);
        return Response.success();
    }
}
