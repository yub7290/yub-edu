package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.StatusReqDTO;
import com.yub.edu.biz.dto.TeacherCreateReqDTO;
import com.yub.edu.biz.dto.TeacherQueryDTO;
import com.yub.edu.biz.dto.TeacherUpdateReqDTO;
import com.yub.edu.biz.service.TeacherService;
import com.yub.edu.biz.entity.EduTeacher;
import com.yub.edu.biz.vo.TeacherDetailRespVO;
import com.yub.edu.biz.vo.TeacherOptionVO;
import com.yub.edu.biz.vo.TeacherPageRespVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师信息 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师信息接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    /**
     * 分页查询教师列表
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<TeacherPageRespVO>> page(@RequestBody PageQuery<TeacherQueryDTO> pageQuery) {
        return Response.success(teacherService.page(pageQuery));
    }

    /**
     * 获取教师详情
     *
     * @param id 教师ID
     * @return 教师详情
     */
    @GetMapping("/{id}")
    public Response<TeacherDetailRespVO> getDetail(@PathVariable("id") Long id) {
        return Response.success(teacherService.getDetail(id));
    }

    /**
     * 新增教师
     *
     * @param dto 新增参数
     * @return 教师ID
     */
    @Log(value = "新增教师", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody TeacherCreateReqDTO dto) {
        return Response.success(teacherService.create(dto));
    }

    /**
     * 编辑教师
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @Log(value = "编辑教师", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody TeacherUpdateReqDTO dto) {
        teacherService.update(dto);
        return Response.success();
    }

    /**
     * 删除教师
     *
     * @param id 教师ID
     * @return 响应
     */
    @Log(value = "删除教师", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        teacherService.delete(id);
        return Response.success();
    }

    /**
     * 批量删除教师
     *
     * @param ids 教师ID列表
     * @return 响应
     */
    @Log(value = "批量删除教师", type = "DELETE")
    @DeleteMapping("/batch")
    public Response<Void> deleteBatch(@RequestBody List<Long> ids) {
        teacherService.deleteBatch(ids);
        return Response.success();
    }

    /**
     * 重置密码
     *
     * @param id 教师ID
     * @return 响应
     */
    @Log(value = "重置教师密码", type = "UPDATE")
    @PutMapping("/{id}/password")
    public Response<Void> resetPassword(@PathVariable("id") Long id) {
        teacherService.resetPassword(id);
        return Response.success();
    }

    /**
     * 切换教师状态
     *
     * @param id  教师ID
     * @param dto 状态参数
     * @return 响应
     */
    @Log(value = "切换教师状态", type = "UPDATE")
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable("id") Long id, @Valid @RequestBody StatusReqDTO dto) {
        teacherService.changeStatus(id, dto.getStatus());
        return Response.success();
    }

    /**
     * 获取所有启用的教师列表（下拉框用）
     */
    @GetMapping("/options")
    public Response<List<TeacherOptionVO>> options() {
        List<EduTeacher> teachers = teacherService.selectAllEnabled();
        List<TeacherOptionVO> voList = teachers.stream()
                .map(t -> TeacherOptionVO.builder().id(t.getId()).name(t.getName()).build())
                .toList();
        return Response.success(voList);
    }

    /**
     * 设置教师推荐状态
     *
     * @param id          教师ID
     * @param recommended 推荐状态（1=推荐 0=不推荐）
     * @return 响应
     */
    @Log(value = "设置教师推荐状态", type = "UPDATE")
    @PutMapping("/{id}/recommended")
    public Response<Void> setRecommended(@PathVariable("id") Long id, @RequestParam Integer recommended) {
        teacherService.setRecommended(id, recommended);
        return Response.success();
    }
}
