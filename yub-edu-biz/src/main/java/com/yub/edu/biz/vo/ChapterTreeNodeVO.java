package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 章节树响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 练习首页章节树节点
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterTreeNodeVO {

    /** 章节ID */
    private Long id;

    /** 章节名称 */
    private String name;

    /** 父章节ID */
    private Long parentId;

    /** 排序 */
    private Integer sort;

    /** 是否有子章节 */
    private Boolean hasChildren;
}
