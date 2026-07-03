package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户地址实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 用户地址簿实体
 * @Version: 1.0
 */
@Data
public class EduAddress {

    /** 主键 */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 收货人姓名 */
    private String name;

    /** 联系电话 */
    private String phone;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String district;

    /** 详细地址 */
    private String detail;

    /** 是否默认: 1=是 0=否 */
    private Integer isDefault;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
