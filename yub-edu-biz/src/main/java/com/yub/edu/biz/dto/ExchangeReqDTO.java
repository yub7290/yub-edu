package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 积分兑换请求DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: APP端积分兑换请求参数
 * @Version: 1.0
 */
@Data
public class ExchangeReqDTO {

    /** 商品ID */
    private Long productId;

    /** 兑换方式: 1=线下自取 2=邮寄到家 */
    private Integer exchangeType;

    /** 收货人姓名（邮寄时必填） */
    private String receiverName;

    /** 收货人电话（邮寄时必填） */
    private String receiverPhone;

    /** 收货地址（邮寄时必填） */
    private String receiverAddress;

    /** 地址ID（选择已有地址时传入） */
    private Long addressId;
}
