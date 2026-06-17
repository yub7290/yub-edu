package com.yub.edu.biz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 文件上传资源映射
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 映射本地上传目录为静态资源
 * @Version: 1.0.0
 */
@Configuration
public class UploadWebMvcConfig implements WebMvcConfigurer {

    @Value("${local.upload-path:${user.dir}/uploads}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
