package com.yub.edu.biz.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.annotation.Log;
import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduPointsProduct;
import com.yub.edu.biz.entity.EduStudyCard;
import com.yub.edu.biz.mapper.EduPointsProductMapper;
import com.yub.edu.biz.mapper.EduStudyCardInstanceMapper;
import com.yub.edu.biz.mapper.EduStudyCardMapper;
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
import java.util.Map;

/**
 * 积分商品 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分商品管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/points-product")
@RequiredArgsConstructor
public class EduPointsProductController {

    private final EduPointsProductMapper eduPointsProductMapper;
    private final EduStudyCardMapper eduStudyCardMapper;
    private final EduStudyCardInstanceMapper eduStudyCardInstanceMapper;

    /**
     * 分页查询积分商品
     *
     * @param query 查询条件（含分页参数）
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<EduPointsProduct>> page(@RequestBody PageQuery<Map<String, Object>> query) {
        Map<String, Object> param = query.getQueryParam();
        String name = param != null ? (String) param.get("name") : null;
        Integer status = param != null ? (Integer) param.get("status") : null;
        PageHelper.startPage(query.getPageParam().getPageNum(), query.getPageParam().getPageSize());
        List<EduPointsProduct> list = eduPointsProductMapper.selectPage(name, status);
        PageInfo<EduPointsProduct> pageInfo = new PageInfo<>(list);
        fillStudyCardStock(list);
        return Response.success(PageResult.of(list, pageInfo.getTotal()));
    }

    /**
     * 获取积分商品详情
     *
     * @param id 积分商品ID
     * @return 积分商品实体
     */
    @GetMapping("/{id}")
    public Response<EduPointsProduct> getDetail(@PathVariable("id") Long id) {
        EduPointsProduct product = eduPointsProductMapper.selectById(id);
        if (product != null && Integer.valueOf(2).equals(product.getProductType())
                && product.getStudyCardId() != null) {
            product.setStockCount(
                    eduStudyCardInstanceMapper.countAvailableByCardId(product.getStudyCardId()));
        }
        return Response.success(product);
    }

    /**
     * 新增积分商品（默认启用）
     *
     * @param product 积分商品实体
     * @return 响应
     */
    @Log(value = "新增积分商品", type = "CREATE")
    @PostMapping
    public Response<Void> create(@RequestBody EduPointsProduct product) {
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        eduPointsProductMapper.insert(product);
        return Response.success();
    }

    /**
     * 编辑积分商品
     *
     * @param product 积分商品实体
     * @return 响应
     */
    @Log(value = "编辑积分商品", type = "UPDATE")
    @PutMapping
    public Response<Void> update(@RequestBody EduPointsProduct product) {
        eduPointsProductMapper.updateById(product);
        return Response.success();
    }

    /**
     * 删除积分商品（逻辑删除）
     *
     * @param id 积分商品ID
     * @return 响应
     */
    @Log(value = "删除积分商品", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        eduPointsProductMapper.deleteById(id);
        return Response.success();
    }

    /**
     * 切换商品状态
     *
     * @param id   商品ID
     * @param body 包含 status 的请求体
     * @return 响应
     */
    @Log(value = "切换商品状态", type = "UPDATE")
    @PutMapping("/{id}/status")
    public Response<Void> changeStatus(@PathVariable("id") Long id, @RequestBody Map<String, Integer> body) {
        eduPointsProductMapper.updateStatus(id, body.get("status"));
        return Response.success();
    }

    /**
     * 获取学习卡下拉列表（用于选择关联学习卡）
     *
     * @return 学习卡列表（仅 id + title）
     */
    @GetMapping("/study-cards")
    public Response<List<EduStudyCard>> getStudyCardOptions() {
        return Response.success(eduStudyCardMapper.selectSimpleList());
    }

    /**
     * 为学习卡类型商品填充实际可用库存
     *
     * @param list 商品列表
     */
    private void fillStudyCardStock(List<EduPointsProduct> list) {
        for (EduPointsProduct product : list) {
            if (Integer.valueOf(2).equals(product.getProductType()) && product.getStudyCardId() != null) {
                try {
                    int available = eduStudyCardInstanceMapper.countAvailableByCardId(product.getStudyCardId());
                    product.setStockCount(available);
                } catch (Exception e) {
                    // 查询失败时保持原值
                }
            }
        }
    }
}
