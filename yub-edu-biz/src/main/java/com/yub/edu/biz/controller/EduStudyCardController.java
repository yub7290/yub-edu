package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.StatusReqDTO;
import com.yub.edu.biz.dto.StudyCardCreateReqDTO;
import com.yub.edu.biz.dto.StudyCardInstanceQueryDTO;
import com.yub.edu.biz.dto.StudyCardQueryDTO;
import com.yub.edu.biz.dto.StudyCardUpdateReqDTO;
import com.yub.edu.biz.service.EduStudyCardService;
import com.yub.edu.biz.vo.StudyCardDetailRespVO;
import com.yub.edu.biz.vo.StudyCardInstancePageRespVO;
import com.yub.edu.biz.vo.StudyCardPageRespVO;
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
 * 学习卡
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/study-card")
@RequiredArgsConstructor
public class EduStudyCardController {

    private final EduStudyCardService eduStudyCardService;

    /**
     * 分页查询学习卡
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<StudyCardPageRespVO>> page(@RequestBody PageQuery<StudyCardQueryDTO> pageQuery) {
        return Response.success(eduStudyCardService.page(pageQuery));
    }

    /**
     * 获取学习卡详情
     *
     * @param id 学习卡ID
     * @return 学习卡详情
     */
    @GetMapping("/{id}")
    public Response<StudyCardDetailRespVO> getDetail(@PathVariable("id") Long id) {
        return Response.success(eduStudyCardService.getDetail(id));
    }

    /**
     * 新增学习卡
     *
     * @param dto 新增参数
     * @return 学习卡ID
     */
    @Log(value = "新增学习卡", type = "CREATE")
    @PostMapping
    public Response<Long> create(@Valid @RequestBody StudyCardCreateReqDTO dto) {
        return Response.success(eduStudyCardService.create(dto));
    }

    /**
     * 编辑学习卡
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @Log(value = "编辑学习卡", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@Valid @RequestBody StudyCardUpdateReqDTO dto) {
        eduStudyCardService.update(dto);
        return Response.success();
    }

    /**
     * 删除学习卡
     *
     * @param id 学习卡ID
     * @return 响应
     */
    @Log(value = "删除学习卡", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        eduStudyCardService.delete(id);
        return Response.success();
    }

    /**
     * 切换学习卡状态
     *
     * @param id  学习卡ID
     * @param dto 状态参数
     * @return 响应
     */
    @Log(value = "切换学习卡状态", type = "UPDATE")
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable("id") Long id, @Valid @RequestBody StatusReqDTO dto) {
        eduStudyCardService.changeStatus(id, dto.getStatus());
        return Response.success();
    }

    /**
     * 分页查询学习卡实例
     *
     * @param id        学习卡模板ID
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/{id}/instances/page")
    public Response<PageResult<StudyCardInstancePageRespVO>> getInstancePage(
            @PathVariable("id") Long id,
            @RequestBody PageQuery<StudyCardInstanceQueryDTO> pageQuery) {
        return Response.success(eduStudyCardService.getInstancePage(id, pageQuery));
    }

    /**
     * 批量生成学习卡实例
     *
     * @param id    学习卡模板ID
     * @param count 生成数量
     * @return 响应
     */
    @Log(value = "批量生成学习卡", type = "CREATE")
    @PostMapping("/{id}/instances/generate")
    public Response<Void> batchGenerate(@PathVariable("id") Long id, @RequestBody int count) {
        eduStudyCardService.batchGenerate(id, count);
        return Response.success();
    }

    /**
     * 回滚学习卡实例
     *
     * @param instanceId 实例ID
     * @return 响应
     */
    @Log(value = "回滚学习卡实例", type = "UPDATE")
    @PutMapping("/instances/{instanceId}/rollback")
    public Response<Void> rollbackInstance(@PathVariable("instanceId") Long instanceId) {
        eduStudyCardService.rollbackInstance(instanceId);
        return Response.success();
    }

    /**
     * 切换学习卡实例状态
     *
     * @param instanceId 实例ID
     * @param dto        状态参数
     * @return 响应
     */
    @Log(value = "切换学习卡实例状态", type = "UPDATE")
    @PutMapping("/instances/{instanceId}/status")
    public Response<Void> toggleInstanceStatus(@PathVariable("instanceId") Long instanceId,
                                               @Valid @RequestBody StatusReqDTO dto) {
        eduStudyCardService.toggleInstanceStatus(instanceId, dto.getStatus());
        return Response.success();
    }
}
