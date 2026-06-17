package com.yub.edu.biz.controller;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.QuestionCreateReqDTO;
import com.yub.edu.biz.dto.QuestionQueryDTO;
import com.yub.edu.biz.dto.QuestionUpdateReqDTO;
import com.yub.edu.biz.dto.StatusReqDTO;
import com.yub.edu.biz.service.EduQuestionService;
import com.yub.edu.biz.vo.QuestionDetailRespVO;
import com.yub.edu.biz.vo.QuestionPageRespVO;
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
 * 试题管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 试题管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/question")
@RequiredArgsConstructor
public class EduQuestionController {

    private final EduQuestionService eduQuestionService;

    /**
     * 分页查询试题
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<QuestionPageRespVO>> page(@RequestBody PageQuery<QuestionQueryDTO> pageQuery) {
        return Response.success(eduQuestionService.page(pageQuery));
    }

    /**
     * 获取试题详情
     *
     * @param id 试题ID
     * @return 试题详情
     */
    @GetMapping("/{id}")
    public Response<QuestionDetailRespVO> getDetail(@PathVariable Long id) {
        return Response.success(eduQuestionService.getDetail(id));
    }

    /**
     * 新增试题
     *
     * @param dto 新增参数
     * @return 试题ID
     */
    @PostMapping
    public Response<Long> create(@Valid @RequestBody QuestionCreateReqDTO dto) {
        return Response.success(eduQuestionService.create(dto));
    }

    /**
     * 编辑试题
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @PutMapping
    public Response<Void> update(@Valid @RequestBody QuestionUpdateReqDTO dto) {
        eduQuestionService.update(dto);
        return Response.success();
    }

    /**
     * 删除试题
     *
     * @param id 试题ID
     * @return 响应
     */
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        eduQuestionService.delete(id);
        return Response.success();
    }

    /**
     * 切换试题状态
     *
     * @param id  试题ID
     * @param dto 状态参数
     * @return 响应
     */
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable Long id, @Valid @RequestBody StatusReqDTO dto) {
        eduQuestionService.changeStatus(id, dto.getStatus());
        return Response.success();
    }
}
