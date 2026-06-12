package com.yub.edu.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学习系统启动类
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-10
 * @Description: 学习系统启动入口
 * @Version: 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.yub")
@MapperScan("com.yub.**.mapper")
public class EduApplication {

    /**
     * 学习系统启动入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
