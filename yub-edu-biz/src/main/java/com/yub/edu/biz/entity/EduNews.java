package com.yub.edu.biz.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 新闻资讯实体
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 新闻资讯实体
 * @Version: 1.0.0
 */
@Data
public class EduNews {

    /** 主键 */
    private Long id;

    /** 资讯标题 */
    private String title;

    /** 摘要 */
    private String summary;

    /** 封面图URL */
    private String coverUrl;

    /** 资讯内容（富文本） */
    private String content;

    /** 分类ID */
    private Long categoryId;

    /** 来源 */
    private String source;

    /** 作者 */
    private String author;

    /** 状态 0:草稿 1:已发布 */
    private Integer status;

    /** 阅读量 */
    private Integer views;

    /** 发布时间 */
    private LocalDateTime publishTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人 */
    private Long createBy;

    /** 更新人 */
    private Long updateBy;

    /** 删除标记 0:正常 1:已删除 */
    private Integer deleted;
}
