package com.yub.edu.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云配置
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 七牛云对象存储配置
 * @Version: 1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class QiniuConfig {

    /** 七牛云 AccessKey */
    private String accessKey;

    /** 七牛云 SecretKey */
    private String secretKey;

    /** 存储空间名称 */
    private String bucket;

    /** CDN 域名 */
    private String domain;

    /** 存储区域：z0=华东 z1=华北 z2=华南 na0=北美 as0=东南亚 */
    private String region = "z2";
}
