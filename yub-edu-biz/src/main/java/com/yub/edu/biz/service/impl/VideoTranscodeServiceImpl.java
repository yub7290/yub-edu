package com.yub.edu.biz.service.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.processing.OperationManager;
import com.qiniu.processing.OperationStatus;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import com.yub.common.enums.ResponseCode;
import com.yub.common.exception.BaseException;
import com.yub.edu.biz.config.QiniuConfig;
import com.yub.edu.biz.dto.VideoTranscodeStartReqDTO;
import com.yub.edu.biz.entity.EduVideoAsset;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.mapper.EduChapterVideoMapper;
import com.yub.edu.biz.mapper.EduVideoAssetMapper;
import com.yub.edu.biz.service.VideoTranscodeService;
import com.yub.edu.biz.vo.VideoTranscodeRespVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoTranscodeServiceImpl implements VideoTranscodeService {
    private static final String STATUS_PROCESSING = "PROCESSING";
    private static final String STATUS_READY = "READY";
    private static final String STATUS_FAILED = "FAILED";
    private static final long MAX_VIDEO_SIZE = 500L * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("mp4", "mov");

    private final EduVideoAssetMapper videoAssetMapper;
    private final EduChapterVideoMapper chapterVideoMapper;
    private final QiniuConfig qiniuConfig;

    @Override
    public VideoTranscodeRespVO start(VideoTranscodeStartReqDTO request, Long userId) {
        validateConfig();
        validateRequest(request);
        EduVideoAsset existing = videoAssetMapper.selectBySourceKey(request.getSourceKey());
        if (existing != null) {
            checkOwner(existing, userId);
            return toResponse(existing);
        }

        String taskToken = UUID.randomUUID().toString().replace("-", "");
        String hlsDirectory = trimSlashes(qiniuConfig.getVideoHlsPrefix()) + "/" + taskToken;
        String hlsKey = hlsDirectory + "/master.m3u8";
        EduVideoAsset asset = new EduVideoAsset();
        asset.setOriginalName(request.getOriginalName());
        asset.setFileSize(request.getFileSize());
        asset.setSourceKey(request.getSourceKey());
        asset.setSourceUrl(toCdnUrl(request.getSourceKey()));
        asset.setHlsKey(hlsKey);
        asset.setHlsUrl(toCdnUrl(hlsKey));
        asset.setStatus(STATUS_PROCESSING);
        asset.setCreateBy(userId);
        videoAssetMapper.insert(asset);

        try {
            String persistentId = submitPfop(request.getSourceKey(), hlsDirectory, hlsKey);
            asset.setPersistentId(persistentId);
            videoAssetMapper.updateTask(asset);
            log.info("video transcode submitted, assetId={}, sourceKey={}, persistentId={}",
                    asset.getId(), request.getSourceKey(), persistentId);
            return toResponse(asset);
        } catch (QiniuException e) {
            asset.setStatus(STATUS_FAILED);
            asset.setErrorMessage(safeMessage(e.response != null ? e.response.error : e.getMessage()));
            videoAssetMapper.updateTask(asset);
            log.error("video transcode submit failed, assetId={}, sourceKey={}", asset.getId(), request.getSourceKey(), e);
            throw new BaseException(ResponseCode.SERVICE_UNAVAILABLE, e);
        }
    }

    @Override
    public VideoTranscodeRespVO refresh(Long assetId, Long userId) {
        EduVideoAsset asset = requireAsset(assetId);
        checkOwner(asset, userId);
        if (!STATUS_PROCESSING.equals(asset.getStatus()) || asset.getPersistentId() == null) {
            return toResponse(asset);
        }

        try {
            OperationStatus operation = operationManager().prefop(asset.getPersistentId());
            if (operation.code == 0) {
                boolean allSucceeded = operation.items != null && operation.items.length > 0
                        && Arrays.stream(operation.items).allMatch(item -> item.code == 0);
                if (allSucceeded) {
                    asset.setStatus(STATUS_READY);
                    asset.setErrorMessage(null);
                } else {
                    markFailed(asset, operation);
                }
                videoAssetMapper.updateTask(asset);
            } else if (operation.code >= 3) {
                markFailed(asset, operation);
                videoAssetMapper.updateTask(asset);
            }
            return toResponse(asset);
        } catch (QiniuException e) {
            log.warn("video transcode status query failed, assetId={}, persistentId={}",
                    assetId, asset.getPersistentId(), e);
            throw new BaseException(ResponseCode.SERVICE_UNAVAILABLE, e);
        }
    }

    @Override
    public EduVideoAsset getReadyAsset(Long assetId, Long userId) {
        EduVideoAsset asset = requireAsset(assetId);
        // 章节维护允许其他有管理权限的操作员复用已就绪资源；接口权限由管理端安全配置控制。
        if (!STATUS_READY.equals(asset.getStatus()) || asset.getHlsUrl() == null || asset.getHlsUrl().isBlank()) {
            throw new BaseException(EduErrorCode.VIDEO_TRANSCODE_NOT_READY);
        }
        return asset;
    }

    @Override
    public void delete(Long assetId, Long userId) {
        EduVideoAsset asset = requireAsset(assetId);
        checkOwner(asset, userId);
        if (chapterVideoMapper.countByAssetId(assetId) > 0) {
            throw new BaseException(EduErrorCode.VIDEO_ASSET_IN_USE);
        }
        deleteAssetFilesAndMarkDeleted(asset);
    }

    @Override
    public void deleteIfOrphaned(Long assetId) {
        EduVideoAsset asset = videoAssetMapper.selectById(assetId);
        if (asset == null || chapterVideoMapper.countByAssetId(assetId) > 0) {
            return;
        }
        deleteAssetFilesAndMarkDeleted(asset);
    }

    /** 删除源视频及其 HLS 目录，云端删除成功后再软删资产记录。 */
    private void deleteAssetFilesAndMarkDeleted(EduVideoAsset asset) {
        try {
            deleteQiniuFiles(asset);
            int affectedRows = videoAssetMapper.markDeleted(asset.getId());
            if (affectedRows != 1) {
                throw new BaseException(EduErrorCode.VIDEO_ASSET_NOT_FOUND);
            }
        } catch (QiniuException e) {
            log.error("video asset delete failed, assetId={}, sourceKey={}, hlsKey={}",
                    asset.getId(), asset.getSourceKey(), asset.getHlsKey(), e);
            throw new BaseException(ResponseCode.SERVICE_UNAVAILABLE, e);
        }
    }

    private void deleteQiniuFiles(EduVideoAsset asset) throws QiniuException {
        validateConfig();
        String sourceKey = validateSourceKey(asset.getSourceKey());
        String hlsPrefix = validateHlsPrefix(asset.getHlsKey());

        BucketManager bucketManager = new BucketManager(
                Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey()),
                new Configuration(Region.autoRegion()));
        LinkedHashSet<String> keys = new LinkedHashSet<>();
        keys.add(sourceKey);

        BucketManager.FileListIterator iterator = bucketManager.createFileListIterator(
                qiniuConfig.getBucket(), hlsPrefix);
        while (iterator.hasNext()) {
            FileInfo[] files = iterator.next();
            if (files == null) continue;
            for (FileInfo file : files) {
                if (file != null && file.key != null && !file.key.isBlank()) {
                    keys.add(file.key);
                }
            }
        }
        if (iterator.error() != null) {
            throw iterator.error();
        }
        deleteInBatches(bucketManager, new ArrayList<>(keys));
    }

    private void deleteInBatches(BucketManager bucketManager, List<String> keys) throws QiniuException {
        final int batchSize = 1000;
        for (int start = 0; start < keys.size(); start += batchSize) {
            int end = Math.min(start + batchSize, keys.size());
            BucketManager.BatchOperations operations = new BucketManager.BatchOperations();
            for (int i = start; i < end; i++) {
                operations.addDeleteOp(qiniuConfig.getBucket(), keys.get(i));
            }
            com.qiniu.http.Response response = bucketManager.batch(operations);
            if (!response.isOK()) {
                throw new QiniuException(response);
            }
            BatchStatus[] statuses = response.jsonToObject(BatchStatus[].class);
            if (statuses == null) continue;
            for (BatchStatus status : statuses) {
                // 612 表示对象已经不存在，按幂等删除处理。
                if (status != null && status.code != 200 && status.code != 612) {
                    String detail = status.data == null ? "七牛对象删除失败" : status.data.error;
                    throw new QiniuException(new IOException(detail == null ? "七牛对象删除失败" : detail));
                }
            }
        }
    }

    private String validateSourceKey(String sourceKey) {
        String prefix = trimSlashes(qiniuConfig.getVideoSourcePrefix()) + "/";
        if (sourceKey == null || !sourceKey.startsWith(prefix) || sourceKey.contains("..")) {
            throw new BaseException(EduErrorCode.PARAM_INVALID);
        }
        return sourceKey;
    }

    private String validateHlsPrefix(String hlsKey) {
        String prefix = trimSlashes(qiniuConfig.getVideoHlsPrefix()) + "/";
        if (hlsKey == null || !hlsKey.startsWith(prefix) || hlsKey.contains("..")) {
            throw new BaseException(EduErrorCode.PARAM_INVALID);
        }
        int lastSlash = hlsKey.lastIndexOf('/');
        if (lastSlash < prefix.length()) {
            throw new BaseException(EduErrorCode.PARAM_INVALID);
        }
        return hlsKey.substring(0, lastSlash + 1);
    }

    private String submitPfop(String sourceKey, String hlsDirectory, String hlsKey) throws QiniuException {
        String[] prefixes = {"360p", "480p", "720p"};
        String multiPrefix = Arrays.stream(prefixes)
                .map(name -> UrlSafeBase64.encodeToString(hlsDirectory + "/" + name))
                .collect(Collectors.joining(","));
        int segmentSeconds = Math.max(2, Math.min(60,
                qiniuConfig.getHlsSegmentSeconds() == null ? 6 : qiniuConfig.getHlsSegmentSeconds()));
        String fop = "adapt/m3u8"
                + "/envBandWidth/600000,1200000,2500000"
                + "/multiAb/64k,96k,128k"
                + "/multiVb/500k,900k,2000k"
                + "/multiResolution/640:360,854:480,1280:720"
                + "/multiPrefix/" + multiPrefix
                + "/hlstime/" + segmentSeconds
                + "|saveas/" + UrlSafeBase64.encodeToString(qiniuConfig.getBucket() + ":" + hlsKey);
        String pipeline = qiniuConfig.getTranscodePipeline();
        if (pipeline == null || pipeline.isBlank()) {
            return operationManager().pfop(qiniuConfig.getBucket(), sourceKey, fop);
        }
        return operationManager().pfop(qiniuConfig.getBucket(), sourceKey, fop, pipeline, true);
    }

    private OperationManager operationManager() {
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        return new OperationManager(auth, new Configuration(Region.autoRegion()));
    }

    private void validateRequest(VideoTranscodeStartReqDTO request) {
        if (request.getFileSize() <= 0 || request.getFileSize() > MAX_VIDEO_SIZE) {
            throw new BaseException(ResponseCode.FILE_SIZE_EXCEEDED);
        }
        String sourcePrefix = trimSlashes(qiniuConfig.getVideoSourcePrefix()) + "/";
        if (!request.getSourceKey().startsWith(sourcePrefix) || request.getSourceKey().contains("..")) {
            throw new BaseException(EduErrorCode.PARAM_INVALID);
        }
        String extension = extension(request.getSourceKey());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BaseException(ResponseCode.FILE_TYPE_NOT_ALLOWED);
        }
    }

    private void validateConfig() {
        if (!StringUtils.hasText(qiniuConfig.getAccessKey())
                || !StringUtils.hasText(qiniuConfig.getSecretKey())
                || !StringUtils.hasText(qiniuConfig.getBucket())
                || !StringUtils.hasText(qiniuConfig.getDomain())
                || !StringUtils.hasText(qiniuConfig.getVideoSourcePrefix())
                || !StringUtils.hasText(qiniuConfig.getVideoHlsPrefix())) {
            throw new BaseException(ResponseCode.SERVICE_UNAVAILABLE);
        }
    }

    private void markFailed(EduVideoAsset asset, OperationStatus operation) {
        asset.setStatus(STATUS_FAILED);
        String detail = operation.desc;
        if (operation.items != null) {
            detail = Arrays.stream(operation.items)
                    .filter(item -> item.code != 0)
                    .map(item -> item.error == null || item.error.isBlank() ? item.desc : item.error)
                    .filter(message -> message != null && !message.isBlank())
                    .findFirst().orElse(detail);
        }
        asset.setErrorMessage(safeMessage(detail));
        log.warn("video transcode failed, assetId={}, persistentId={}, reason={}",
                asset.getId(), asset.getPersistentId(), asset.getErrorMessage());
    }

    private EduVideoAsset requireAsset(Long assetId) {
        EduVideoAsset asset = videoAssetMapper.selectById(assetId);
        if (asset == null) throw new BaseException(EduErrorCode.VIDEO_ASSET_NOT_FOUND);
        return asset;
    }

    private void checkOwner(EduVideoAsset asset, Long userId) {
        if (asset.getCreateBy() != null && !asset.getCreateBy().equals(userId)) {
            throw new BaseException(EduErrorCode.VIDEO_ASSET_FORBIDDEN);
        }
    }

    private VideoTranscodeRespVO toResponse(EduVideoAsset asset) {
        return VideoTranscodeRespVO.builder()
                .assetId(asset.getId())
                .status(asset.getStatus())
                .playbackUrl(STATUS_READY.equals(asset.getStatus()) ? asset.getHlsUrl() : null)
                .errorMessage(asset.getErrorMessage())
                .build();
    }

    private String toCdnUrl(String key) {
        return qiniuConfig.getDomain().replaceAll("/+$", "") + "/" + key;
    }

    private String extension(String key) {
        int index = key.lastIndexOf('.');
        return index < 0 ? "" : key.substring(index + 1).toLowerCase(Locale.ROOT);
    }

    private String trimSlashes(String value) {
        return value == null ? "" : value.replaceAll("^/+|/+$", "");
    }

    private String safeMessage(String value) {
        if (value == null || value.isBlank()) return "七牛转码失败";
        return value.length() > 500 ? value.substring(0, 500) : value;
    }
}
