package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.TeacherTitleCreateReqDTO;
import com.yub.edu.biz.dto.TeacherTitleQueryDTO;
import com.yub.edu.biz.dto.TeacherTitleUpdateReqDTO;
import com.yub.edu.biz.dto.StatusReqDTO;
import com.yub.edu.biz.entity.EduTeacherTitle;
import com.yub.edu.biz.service.TeacherTitleService;
import com.yub.edu.biz.vo.TeacherTitleDetailRespVO;
import com.yub.edu.biz.vo.TeacherTitlePageRespVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师职称 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教师职称接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/teacher-title")
@RequiredArgsConstructor
public class TeacherTitleController {

    private final TeacherTitleService teacherTitleService;

    /**
     * 获取所有启用的教师职称（下拉框用）
     *
     * @return 职称列表
     */
    @GetMapping("/options")
    public Response<List<EduTeacherTitle>> options() {
        return Response.success(teacherTitleService.selectAllEnabled());
    }

    /**
     * 分页查询教师职称列表
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<TeacherTitlePageRespVO>> page(@RequestBody PageQuery<TeacherTitleQueryDTO> pageQuery) {
        return Response.success(teacherTitleService.page(pageQuery));
    }

    /**
     * 获取职称详情
     *
     * @param id 职称ID
     * @return 职称详情
     */
    @GetMapping("/{id}")
    public Response<TeacherTitleDetailRespVO> getDetail(@PathVariable("id") Long id) {
        return Response.success(teacherTitleService.getDetail(id));
    }

    /**
     * 新增职称
     *
     * @param dto 新增参数
     * @return 职称ID
     */
    @Log(value = "新增教师职称", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody TeacherTitleCreateReqDTO dto) {
        return Response.success(teacherTitleService.create(dto));
    }

    /**
     * 编辑职称
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @Log(value = "编辑教师职称", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody TeacherTitleUpdateReqDTO dto) {
        teacherTitleService.update(dto);
        return Response.success();
    }

    /**
     * 删除职称
     *
     * @param id 职称ID
     * @return 响应
     */
    @Log(value = "删除教师职称", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        teacherTitleService.delete(id);
        return Response.success();
    }

    /**
     * 切换职称状态
     *
     * @param id  职称ID
     * @param dto 状态参数
     * @return 响应
     */
    @Log(value = "切换教师职称状态", type = "UPDATE")
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable("id") Long id, @Valid @RequestBody StatusReqDTO dto) {
        teacherTitleService.changeStatus(id, dto.getStatus());
        return Response.success();
    }
}
