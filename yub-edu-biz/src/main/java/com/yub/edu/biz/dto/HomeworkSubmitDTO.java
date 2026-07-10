package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 作业提交请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业提交请求参数
 * @Version: 1.0.0
 */
@Data
public class HomeworkSubmitDTO {

    /** 课程ID */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /** 图片URL列表 */
    @NotEmpty(message = "图片不能为空")
    @Size(min = 1, max = 9, message = "图片数量需在1-9张之间")
    private List<String> imageUrls;
}
