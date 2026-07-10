package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 编辑新闻资讯请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 编辑新闻资讯请求参数
 * @Version: 1.0.0
 */
@Data
public class NewsUpdateReqDTO {

    /** 资讯ID */
    @NotNull(message = "资讯ID不能为空")
    private Long id;

    /** 资讯标题 */
    @NotBlank(message = "资讯标题不能为空")
    @Size(max = 200, message = "资讯标题长度不能超过200个字符")
    private String title;

    /** 摘要 */
    @Size(max = 500, message = "摘要长度不能超过500个字符")
    private String summary;

    /** 封面图URL */
    private String coverUrl;

    /** 资讯内容（富文本） */
    @Size(max = 65535, message = "资讯内容长度不能超过65535个字符")
    private String content;

    /** 分类ID */
    private Long categoryId;

    /** 来源 */
    private String source;

    /** 作者 */
    private String author;

    /** 状态 0:草稿 1:已发布 */
    private Integer status = 1;
}
