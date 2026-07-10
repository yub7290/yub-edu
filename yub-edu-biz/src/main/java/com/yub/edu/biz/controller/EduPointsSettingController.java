package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.PointsSettingReqDTO;
import com.yub.edu.biz.vo.PointsSettingRespVO;
import com.yub.system.entity.param.SysParam;
import com.yub.system.service.param.SysParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 积分设置
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分设置管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/points-setting")
@RequiredArgsConstructor
public class EduPointsSettingController {

    // TODO: 架构治理 - 跨模块依赖: yub-edu 不应直接依赖 yub-system 的 Service
    private final SysParamService sysParamService;

    /**
     * 获取积分设置
     *
     * @return 积分设置
     */
    @GetMapping
    public Response<PointsSettingRespVO> getSettings() {
        List<SysParam> list = sysParamService.getListByGroupCode("points_setting");
        PointsSettingRespVO vo = new PointsSettingRespVO();
        for (SysParam param : list) {
            switch (param.getCode()) {
                case "points_register" -> vo.setPointsRegister(param.getValue());
                case "points_login" -> vo.setPointsLogin(param.getValue());
                case "points_login_daily_max" -> vo.setPointsLoginDailyMax(param.getValue());
                case "points_share" -> vo.setPointsShare(param.getValue());
                case "points_share_daily_max" -> vo.setPointsShareDailyMax(param.getValue());
                case "points_share_register" -> vo.setPointsShareRegister(param.getValue());
                case "points_share_register_daily_max" -> vo.setPointsShareRegisterDailyMax(param.getValue());
                case "points_exchange_rate" -> vo.setPointsExchangeRate(param.getValue());
                default -> { /* ignore unknown codes */ }
            }
        }
        return Response.success(vo);
    }

    /**
     * 保存积分设置
     *
     * @param dto 积分设置
     * @return 响应
     */
    @Log(value = "保存积分设置", type = "UPDATE")
    @PutMapping
    public Response<Void> saveSettings(@RequestBody PointsSettingReqDTO dto) {
        sysParamService.saveValue("points_register", dto.getPointsRegister());
        sysParamService.saveValue("points_login", dto.getPointsLogin());
        sysParamService.saveValue("points_login_daily_max", dto.getPointsLoginDailyMax());
        sysParamService.saveValue("points_share", dto.getPointsShare());
        sysParamService.saveValue("points_share_daily_max", dto.getPointsShareDailyMax());
        sysParamService.saveValue("points_share_register", dto.getPointsShareRegister());
        sysParamService.saveValue("points_share_register_daily_max", dto.getPointsShareRegisterDailyMax());
        sysParamService.saveValue("points_exchange_rate", dto.getPointsExchangeRate());
        return Response.success();
    }
}
