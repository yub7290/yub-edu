package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学员登录日志实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学员登录日志
 * @Version: 1.0.0
 */
@Data
public class EduLoginLog {
    private Long id;
    private Long studentId;
    private String ip;
    private String userAgent;
    private Integer status;
    private String errorMsg;
    private LocalDateTime createTime;
}
