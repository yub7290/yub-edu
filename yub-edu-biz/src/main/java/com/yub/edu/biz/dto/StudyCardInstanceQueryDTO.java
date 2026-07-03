package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 学习卡实例查询参数 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡实例分页查询参数
 * @Version: 1.0.0
 */
@Data
public class StudyCardInstanceQueryDTO {

    /** 状态（0:未使用 1:已使用 2:已回滚 3:已禁用） */
    private Integer status;

    /** 卡号（模糊搜索） */
    private String cardNo;

    /** 用户账号（模糊搜索） */
    private String userAccount;
}
