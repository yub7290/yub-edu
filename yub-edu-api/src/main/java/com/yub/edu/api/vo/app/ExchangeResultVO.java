package com.yub.edu.api.vo.app;

import lombok.Data;

/**
 * 积分兑换结果VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 积分兑换结果，实物返回兑换码，学习卡返回卡号密钥
 * @Version: 1.0
 */
@Data
public class ExchangeResultVO {

    /** 兑换码（实物商品时返回） */
    private String exchangeCode;

    /** 学习卡卡号（学习卡商品时返回） */
    private String cardNo;

    /** 学习卡密钥（学习卡商品时返回） */
    private String cardSecret;

    /** 订单编号 */
    private String orderNo;
}
