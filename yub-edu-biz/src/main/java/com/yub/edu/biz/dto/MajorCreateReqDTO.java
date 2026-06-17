package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增专业请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 新增专业请求参数
 * @Version: 1.0.0
 */
@Data
public class MajorCreateReqDTO {

    /** 上级专业ID（0=顶级） */
    @NotNull(message = "上级专业不能为空")
    private Long parentId;

    /** 专业名称 */
    @NotBlank(message = "专业名称不能为空")
    @Size(max = 100, message = "专业名称长度不能超过100个字符")
    private String name;

    /** 别名 */
    @Size(max = 100, message = "别名长度不能超过100个字符")
    private String alias;

    /** 状态 1:启用 0:禁用 */
    @NotNull(message = "状态不能为空")
    private Integer status = 1;

    /** 推荐 1:是 0:否 */
    private Integer recommended = 0;

    /** 说明 */
    @Size(max = 500, message = "说明长度不能超过500个字符")
    private String description;

    /** 展示图片URL */
    @Size(max = 200, message = "图片URL长度不能超过200个字符")
    private String imageUrl;

    /** 详情（富文本） */
    private String detail;

    /** 排序 */
    private Integer sort = 0;
}
