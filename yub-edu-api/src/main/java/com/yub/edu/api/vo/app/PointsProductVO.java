package com.yub.edu.api.vo.app;

import lombok.Data;

/**
 * 积分商品VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: APP端积分商品展示
 * @Version: 1.0
 */
@Data
public class PointsProductVO {

    /** 商品ID */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 商品类型: 1=实物商品 2=学习卡 */
    private Integer productType;

    /** 商品图片 */
    private String imageUrl;

    /** 所需积分 */
    private Integer requiredPoints;

    /** 库存数量 */
    private Integer stockCount;
}
