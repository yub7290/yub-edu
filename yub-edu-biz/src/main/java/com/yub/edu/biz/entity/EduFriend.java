package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学员好友关系实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 学员好友关系（双向存储，A 与 B 互为好友时各一条记录）
 * @Version: 1.0.0
 */
@Data
public class EduFriend {

    /** 主键 */
    private Long id;

    /** 关系归属学员（拥有该好友的人） */
    private Long studentId;

    /** 好友学员ID */
    private Long friendId;

    /** 成为好友时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
