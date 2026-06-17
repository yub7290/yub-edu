package com.yub.edu.biz.controller;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.ExamCreateReqDTO;
import com.yub.edu.biz.dto.ExamQueryDTO;
import com.yub.edu.biz.dto.ExamUpdateReqDTO;
import com.yub.edu.biz.dto.StatusReqDTO;
import com.yub.edu.biz.service.EduExamService;
import com.yub.edu.biz.vo.ExamDetailRespVO;
import com.yub.edu.biz.vo.ExamPageRespVO;
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
 * 试卷管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 试卷管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/exam")
@RequiredArgsConstructor
public class EduExamController {

    private final EduExamService eduExamService;

    /**
     * 分页查询试卷
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<ExamPageRespVO>> page(@RequestBody PageQuery<ExamQueryDTO> pageQuery) {
        return Response.success(eduExamService.page(pageQuery));
    }

    /**
     * 获取试卷详情
     *
     * @param id 试卷ID
     * @return 试卷详情
     */
    @GetMapping("/{id}")
    public Response<ExamDetailRespVO> getDetail(@PathVariable Long id) {
        return Response.success(eduExamService.getDetail(id));
    }

    /**
     * 新增试卷
     *
     * @param dto 新增参数
     * @return 试卷ID
     */
    @PostMapping
    public Response<Long> create(@Valid @RequestBody ExamCreateReqDTO dto) {
        return Response.success(eduExamService.create(dto));
    }

    /**
     * 编辑试卷
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @PutMapping
    public Response<Void> update(@Valid @RequestBody ExamUpdateReqDTO dto) {
        eduExamService.update(dto);
        return Response.success();
    }

    /**
     * 删除试卷
     *
     * @param id 试卷ID
     * @return 响应
     */
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        eduExamService.delete(id);
        return Response.success();
    }

    /**
     * 切换试卷状态
     *
     * @param id  试卷ID
     * @param dto 状态参数
     * @return 响应
     */
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable Long id, @Valid @RequestBody StatusReqDTO dto) {
        eduExamService.changeStatus(id, dto.getStatus());
        return Response.success();
    }
}
