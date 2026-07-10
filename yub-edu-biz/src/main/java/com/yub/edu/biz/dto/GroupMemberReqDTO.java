package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 学员组成员请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组成员操作请求参数
 * @Version: 1.0.0
 */
@Data
public class GroupMemberReqDTO {

    /** 学员ID */
    @NotNull(message = "学员ID不能为空")
    private Long studentId;
}
