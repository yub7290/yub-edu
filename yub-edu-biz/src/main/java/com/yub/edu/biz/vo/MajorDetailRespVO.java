package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 专业详情响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 专业详情响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MajorDetailRespVO {

    /** 主键 */
    private Long id;

    /** 上级专业ID */
    private Long parentId;

    /** 上级专业名称 */
    private String parentName;

    /** 专业名称 */
    private String name;

    /** 别名 */
    private String alias;

    /** 状态 1:启用 0:禁用 */
    private Integer status;

    /** 推荐 1:是 0:否 */
    private Integer recommended;

    /** 说明 */
    private String description;

    /** 展示图片URL */
    private String imageUrl;

    /** 详情（富文本） */
    private String detail;

    /** 排序 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人 */
    private Long createBy;

    /** 更新人 */
    private Long updateBy;
}
