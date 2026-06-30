package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduAiConfig;
import com.yub.edu.biz.mapper.EduAiConfigMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * AI助教配置管理 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 后台管理AI助教配置接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/ai/config")
@RequiredArgsConstructor
public class EduAiConfigController {

    private final EduAiConfigMapper aiConfigMapper;

    /**
     * 获取课程AI助教配置
     *
     * @param courseId 课程ID
     * @return AI助教配置
     */
    @GetMapping("/{courseId}")
    public Response<EduAiConfig> getConfig(@PathVariable Long courseId) {
        EduAiConfig config = aiConfigMapper.selectByCourseId(courseId);
        if (config == null) {
            // 返回默认配置
            config = new EduAiConfig();
            config.setCourseId(courseId);
            config.setEnabled(0);
            config.setModel("deepseek-v4-flash");
            config.setDailyLimit(100);
            config.setTemperature(new BigDecimal("0.70"));
            config.setMaxTokens(2000);
            config.setSystemPrompt("你是一个专业的课程AI助教，专门帮助学生解答关于课程的问题。请用简洁、专业的语言回答学生的问题。");
        }
        return Response.success(config);
    }

    /**
     * 保存或更新AI助教配置
     *
     * @param dto 配置参数
     * @return 响应
     */
    @Log(value = "配置AI助教", type = "UPDATE")
    @PostMapping
    public Response<Void> saveConfig(@Valid @RequestBody AiConfigSaveDTO dto) {
        EduAiConfig existing = aiConfigMapper.selectByCourseId(dto.getCourseId());
        if (existing != null) {
            existing.setEnabled(dto.getEnabled());
            existing.setSystemPrompt(dto.getSystemPrompt());
            existing.setModel(dto.getModel());
            existing.setDailyLimit(dto.getDailyLimit());
            existing.setTemperature(dto.getTemperature());
            existing.setMaxTokens(dto.getMaxTokens());
            aiConfigMapper.update(existing);
        } else {
            EduAiConfig config = new EduAiConfig();
            config.setCourseId(dto.getCourseId());
            config.setEnabled(dto.getEnabled());
            config.setSystemPrompt(dto.getSystemPrompt());
            config.setModel(dto.getModel());
            config.setDailyLimit(dto.getDailyLimit());
            config.setTemperature(dto.getTemperature());
            config.setMaxTokens(dto.getMaxTokens());
            aiConfigMapper.insert(config);
        }
        return Response.success();
    }
}

@Data
class AiConfigSaveDTO {
    @NotNull(message = "课程ID不能为空")
    private Long courseId;
    @NotNull(message = "启用状态不能为空")
    private Integer enabled;
    private String systemPrompt;
    private String model;
    private Integer dailyLimit;
    private BigDecimal temperature;
    private Integer maxTokens;
}
