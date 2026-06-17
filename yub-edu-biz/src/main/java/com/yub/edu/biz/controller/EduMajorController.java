package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.dto.MajorCreateReqDTO;
import com.yub.edu.biz.dto.MajorQueryDTO;
import com.yub.edu.biz.dto.MajorUpdateReqDTO;
import com.yub.edu.biz.dto.StatusReqDTO;
import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.service.EduMajorService;
import com.yub.edu.biz.vo.MajorDetailRespVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 专业管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 专业管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/major")
@RequiredArgsConstructor
public class EduMajorController {

    private final EduMajorService eduMajorService;

    /**
     * 获取专业树
     *
     * @param name   专业名称（可选，模糊搜索）
     * @param status 状态（可选）
     * @return 专业树
     */
    @GetMapping("/tree")
    public Response<List<EduMajor>> tree(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "status", required = false) Integer status) {
        if ((name != null && !name.isEmpty()) || status != null) {
            MajorQueryDTO query = new MajorQueryDTO();
            query.setName(name);
            query.setStatus(status);
            return Response.success(eduMajorService.selectTreeByCondition(query));
        }
        return Response.success(eduMajorService.selectTree());
    }

    /**
     * 获取专业详情
     *
     * @param id 专业ID
     * @return 专业详情
     */
    @GetMapping("/{id}")
    public Response<MajorDetailRespVO> getDetail(@PathVariable Long id) {
        return Response.success(eduMajorService.getDetail(id));
    }

    /**
     * 新增专业
     *
     * @param dto 新增参数
     * @return 专业ID
     */
    @PostMapping
    public Response<Long> create(@Valid @RequestBody MajorCreateReqDTO dto) {
        return Response.success(eduMajorService.create(dto));
    }

    /**
     * 编辑专业
     *
     * @param dto 编辑参数
     * @return 响应
     */
    @PutMapping
    public Response<Void> update(@Valid @RequestBody MajorUpdateReqDTO dto) {
        eduMajorService.update(dto);
        return Response.success();
    }

    /**
     * 删除专业
     *
     * @param id 专业ID
     * @return 响应
     */
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        eduMajorService.delete(id);
        return Response.success();
    }

    /**
     * 切换专业状态
     *
     * @param id  专业ID
     * @param dto 状态参数
     * @return 响应
     */
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable Long id, @Valid @RequestBody StatusReqDTO dto) {
        eduMajorService.changeStatus(id, dto.getStatus());
        return Response.success();
    }

    /**
     * 切换推荐状态
     *
     * @param id  专业ID
     * @param dto 状态参数
     * @return 响应
     */
    @PutMapping("/{id}/recommended")
    public Response<Void> changeRecommended(@PathVariable Long id, @Valid @RequestBody StatusReqDTO dto) {
        eduMajorService.changeRecommended(id, dto.getStatus());
        return Response.success();
    }
}
