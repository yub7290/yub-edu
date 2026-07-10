package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学员端未读通知数 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 学员端未读通知数
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeUnreadVO {

    /** 未读通知数 */
    private Integer count;
}
