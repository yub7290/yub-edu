package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分商品实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分商品实体
 * @Version: 1.0
 */
@Data
public class EduPointsProduct {

    /** 主键 */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 商品类型: 1=实物商品 2=学习卡 */
    private Integer productType;

    /** 关联学习卡ID（product_type=2时有效） */
    private Long studyCardId;

    /** 商品图片 */
    private String imageUrl;

    /** 所需积分 */
    private Integer requiredPoints;

    /** 库存数量 */
    private Integer stockCount;

    /** 状态 0:下架 1:上架 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;

    /** 关联学习卡标题（查询时由 JOIN 填充，不持久化） */
    private String studyCardTitle;
}
