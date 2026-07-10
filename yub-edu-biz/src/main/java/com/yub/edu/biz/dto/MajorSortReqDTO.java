package com.yub.edu.biz.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 专业拖拽排序请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-10
 * @Description: 批量更新专业层级与排序（拖拽排序使用）
 * @Version: 1.0.0
 */
@Data
public class MajorSortReqDTO {

    /** 排序项集合（整棵树的最新顺序） */
    @NotNull(message = "排序数据不能为空")
    @Valid
    private List<MajorSortItem> items;

    /** 单个排序项 */
    @Data
    public static class MajorSortItem {

        /** 专业ID */
        @NotNull(message = "专业ID不能为空")
        private Long id;

        /** 上级专业ID（0=顶级） */
        @NotNull(message = "上级专业不能为空")
        private Long parentId;

        /** 排序值（同级内的序号） */
        @NotNull(message = "排序值不能为空")
        private Integer sort;
    }
}
