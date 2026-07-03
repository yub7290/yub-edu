package com.yub.edu.biz.controller.app;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduAddress;
import com.yub.edu.biz.mapper.EduAddressMapper;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * APP端地址管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: APP端用户地址簿管理接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/app/address")
@RequiredArgsConstructor
public class AppAddressController {

    private final EduAddressMapper addressMapper;

    /**
     * 获取用户地址列表
     *
     * @return 地址列表
     */
    @GetMapping("/list")
    public Response<List<EduAddress>> list() {
        Long userId = SecurityUtils.getCurrentUserId();
        return Response.success(addressMapper.selectByUserId(userId));
    }

    /**
     * 新增地址
     *
     * @param address 地址信息
     * @return 响应
     */
    @PostMapping
    public Response<Void> add(@RequestBody EduAddress address) {
        Long userId = SecurityUtils.getCurrentUserId();
        address.setUserId(userId);
        if (Integer.valueOf(1).equals(address.getIsDefault())) {
            addressMapper.clearDefault(userId);
        }
        addressMapper.insert(address);
        return Response.success();
    }

    /**
     * 更新地址
     *
     * @param address 地址信息
     * @return 响应
     */
    @PutMapping
    public Response<Void> update(@RequestBody EduAddress address) {
        Long userId = SecurityUtils.getCurrentUserId();
        address.setUserId(userId);
        if (Integer.valueOf(1).equals(address.getIsDefault())) {
            addressMapper.clearDefault(userId);
        }
        addressMapper.updateById(address);
        return Response.success();
    }

    /**
     * 删除地址
     *
     * @param id 地址ID
     * @return 响应
     */
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        addressMapper.deleteById(id, userId);
        return Response.success();
    }

    /**
     * 设置默认地址
     *
     * @param id 地址ID
     * @return 响应
     */
    @PutMapping("/{id}/default")
    public Response<Void> setDefault(@PathVariable("id") Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        EduAddress existing = addressMapper.selectById(id);
        if (existing == null || !userId.equals(existing.getUserId())) {
            throw new EduException(EduErrorCode.ADDRESS_NOT_FOUND);
        }
        addressMapper.clearDefault(userId);
        addressMapper.setDefault(id, userId);
        return Response.success();
    }
}
