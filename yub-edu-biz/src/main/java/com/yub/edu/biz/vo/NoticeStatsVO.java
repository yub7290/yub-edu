package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知阅读统计 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 通知阅读统计（管理端查看）
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeStatsVO {

    /** 接收人数（绑定该课程的学员数） */
    private Integer receivers;

    /** 已读人数 */
    private Integer readCount;

    /** 未读人数 */
    private Integer unreadCount;
}
