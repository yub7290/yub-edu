package com.yub.edu.biz.controller;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.api.vo.app.FundSummaryVO;
import com.yub.edu.api.vo.app.FundTransactionVO;
import com.yub.edu.biz.service.fund.FundService;
import com.yub.edu.biz.service.fund.FundTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * 管理端资金 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 管理端资金管理接口（资金列表、资金详情、交易记录）
 * @Version: 1.0
 */
@RestController
@RequestMapping("/edu/fund")
@RequiredArgsConstructor
public class EduFundController {

    private final FundService fundService;
    private final FundTransactionService fundTransactionService;

    /**
     * 分页查询用户资金列表
     *
     * @param query 查询条件（含分页参数）
     * @return 分页资金列表
     */
    @PostMapping("/page")
    public Response<PageResult<FundSummaryVO>> page(@RequestBody PageQuery<Map<String, Object>> query) {
        // TODO: 实现用户资金列表查询（需联表查询用户名）
        return Response.success(PageResult.of(Collections.emptyList(), 0));
    }

    /**
     * 用户资金详情
     *
     * @param userId 用户ID
     * @return 资金详情
     */
    @GetMapping("/{userId}")
    public Response<FundSummaryVO> detail(@PathVariable("userId") Long userId) {
        return Response.success(fundService.getSummary(userId));
    }

    /**
     * 分页查询交易记录（管理员视角，支持多条件筛选）
     *
     * @param query 查询条件（含 userId、userName、transactionType、startTime、endTime 及分页参数）
     * @return 分页交易记录
     */
    @PostMapping("/transaction/page")
    public Response<PageResult<FundTransactionVO>> transactionPage(@RequestBody PageQuery<Map<String, Object>> query) {
        Map<String, Object> params = query.getQueryParam();
        Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : null;
        String userName = (String) params.get("userName");
        String transactionType = (String) params.get("transactionType");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        int pageNum = query.getPageParam().getPageNum();
        int pageSize = query.getPageParam().getPageSize();
        return Response.success(fundTransactionService.allPage(userId, userName, transactionType, startTime, endTime, pageNum, pageSize));
    }
}
