package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 知识点查询参数
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-16
 * @Description: 知识点查询参数
 * @Version: 1.0.0
 */
@Data
public class KnowledgePointQueryDTO {
    private Long categoryId;
    private Long courseId;
    private String title;
    private Integer status;
}
