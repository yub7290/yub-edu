package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.VideoTranscodeStartReqDTO;
import com.yub.edu.biz.entity.EduVideoAsset;
import com.yub.edu.biz.vo.VideoTranscodeRespVO;

public interface VideoTranscodeService {
    VideoTranscodeRespVO start(VideoTranscodeStartReqDTO request, Long userId);

    VideoTranscodeRespVO refresh(Long assetId, Long userId);

    EduVideoAsset getReadyAsset(Long assetId, Long userId);

    /** 删除当前用户上传且尚未被章节引用的视频资产。 */
    void delete(Long assetId, Long userId);

    /** 章节解除关联后，删除没有任何章节引用的资产。 */
    void deleteIfOrphaned(Long assetId);
}
