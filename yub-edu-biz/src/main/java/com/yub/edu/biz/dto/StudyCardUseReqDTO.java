package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 学习卡使用请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: APP端学习卡使用/暂存请求参数
 * @Version: 1.0.0
 */
@Data
public class StudyCardUseReqDTO {

    /** 卡号 */
    @NotBlank(message = "卡号不能为空")
    private String cardNo;
}
