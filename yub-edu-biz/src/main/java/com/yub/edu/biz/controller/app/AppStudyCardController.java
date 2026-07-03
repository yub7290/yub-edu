package com.yub.edu.biz.controller.app;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.StudyCardUseReqDTO;
import com.yub.edu.biz.service.EduStudyCardService;
import com.yub.edu.biz.vo.StudyCardMyVO;
import com.yub.edu.biz.vo.StudyCardSummaryVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP-学习卡 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: APP端学习卡接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/app/study-card")
@RequiredArgsConstructor
public class AppStudyCardController {

    private final EduStudyCardService eduStudyCardService;

    /**
     * 使用学习卡
     *
     * @param dto 卡号参数
     * @return 响应
     */
    @PostMapping("/use")
    public Response<Void> useStudyCard(@Valid @RequestBody StudyCardUseReqDTO dto) {
        eduStudyCardService.useCard(dto);
        return Response.success();
    }

    /**
     * 暂存学习卡
     *
     * @param dto 卡号参数
     * @return 响应
     */
    @PostMapping("/save")
    public Response<Void> saveStudyCard(@Valid @RequestBody StudyCardUseReqDTO dto) {
        eduStudyCardService.saveCard(dto);
        return Response.success();
    }

    /**
     * 查询我的学习卡（分页）
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/my")
    public Response<PageResult<StudyCardMyVO>> getMyCards(@RequestBody PageQuery<Void> pageQuery) {
        return Response.success(eduStudyCardService.getMyCards(pageQuery));
    }

    /**
     * 查询学习卡汇总信息
     *
     * @return 汇总（总数、已使用数）
     */
    @GetMapping("/summary")
    public Response<StudyCardSummaryVO> getSummary() {
        return Response.success(eduStudyCardService.getSummary());
    }
}
