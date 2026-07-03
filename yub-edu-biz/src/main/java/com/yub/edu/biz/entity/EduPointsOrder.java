package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分兑换订单实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分兑换订单实体
 * @Version: 1.0
 */
@Data
public class EduPointsOrder {

    /** 主键 */
    private Long id;

    /** 订单编号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 商品ID */
    private Long productId;

    /** 商品名称 */
    private String productName;

    /** 商品类型: 1=实物 2=学习卡 */
    private Integer productType;

    /** 消耗积分 */
    private Integer requiredPoints;

    /** 兑换方式: 1=线下自取 2=邮寄到家 */
    private Integer exchangeType;

    /** 兑换码 */
    private String exchangeCode;

    /** 学习卡实例ID */
    private Long cardInstanceId;

    /** 学习卡卡号 */
    private String cardNo;

    /** 学习卡密钥 */
    private String cardSecret;

    /** 状态: 0=待处理 1=已发货 2=已签收 3=已核销 4=已取消 */
    private Integer status;

    /** 收货人姓名 */
    private String receiverName;

    /** 收货人电话 */
    private String receiverPhone;

    /** 收货地址 */
    private String receiverAddress;

    /** 快递公司 */
    private String expressCompany;

    /** 快递单号 */
    private String expressNo;

    /** 发货时间 */
    private LocalDateTime shipTime;

    /** 签收时间 */
    private LocalDateTime receiveTime;

    /** 核销时间 */
    private LocalDateTime verifyTime;

    /** 核销人ID */
    private Long verifyBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
