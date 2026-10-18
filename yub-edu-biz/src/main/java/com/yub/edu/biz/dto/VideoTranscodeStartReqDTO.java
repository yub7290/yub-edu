package com.yub.edu.biz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class VideoTranscodeStartReqDTO {
    @NotBlank(message = "源文件Key不能为空")
    private String sourceKey;

    @NotBlank(message = "原文件名不能为空")
    private String originalName;

    @NotNull(message = "文件大小不能为空")
    @Positive(message = "文件大小必须大于0")
    private Long fileSize;
}
