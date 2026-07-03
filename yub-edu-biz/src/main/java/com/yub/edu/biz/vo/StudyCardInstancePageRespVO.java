package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学习卡实例分页响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡实例分页列表响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyCardInstancePageRespVO {

    /** 实例ID */
    private Long id;

    /** 卡号 */
    private String cardNo;

    /** 状态（0:未使用 1:已使用 2:已回滚 3:已禁用） */
    private Integer status;

    /** 使用时间 */
    private LocalDateTime useTime;

    /** 用户账号 */
    private String userAccount;
}
