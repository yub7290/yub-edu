package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学习卡实例实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学习卡实例实体
 * @Version: 1.0.0
 */
@Data
public class EduStudyCardInstance {

    /** 主键 */
    private Long id;

    /** 学习卡模板ID */
    private Long cardId;

    /** 卡号 */
    private String cardNo;

    /** 密钥 */
    private String secretCode;

    /** 状态 0:未使用 1:已使用 2:已回滚 3:已禁用 */
    private Integer status;

    /** 使用时间 */
    private LocalDateTime useTime;

    /** 使用人ID */
    private Long userId;

    /** 使用人账号 */
    private String userAccount;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
