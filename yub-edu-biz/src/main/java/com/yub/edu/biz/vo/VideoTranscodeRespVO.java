package com.yub.edu.biz.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoTranscodeRespVO {
    private Long assetId;
    private String status;
    private String playbackUrl;
    private String errorMessage;
}
