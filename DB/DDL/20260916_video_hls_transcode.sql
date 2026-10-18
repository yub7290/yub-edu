-- 新视频先写入独立资产表，七牛 adapt 转码 READY 后才能关联章节。
CREATE TABLE `edu_video_asset` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `original_name` varchar(255) NOT NULL COMMENT '上传时原文件名',
  `file_size` bigint NOT NULL COMMENT '源文件大小（字节）',
  `source_key` varchar(500) NOT NULL COMMENT '七牛源文件Key',
  `source_url` varchar(1000) NOT NULL COMMENT '七牛源文件URL',
  `hls_key` varchar(500) NOT NULL COMMENT '自适应HLS主播放列表Key',
  `hls_url` varchar(1000) NOT NULL COMMENT '自适应HLS主播放列表URL',
  `persistent_id` varchar(100) DEFAULT NULL COMMENT '七牛pfop任务ID',
  `status` varchar(20) NOT NULL COMMENT 'PROCESSING/READY/FAILED',
  `error_message` varchar(500) DEFAULT NULL COMMENT '转码失败原因',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常 1:已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_source_key` (`source_key`),
  KEY `idx_persistent_id` (`persistent_id`),
  KEY `idx_status_update_time` (`status`, `update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频转码资产表';

ALTER TABLE `edu_chapter_video`
  ADD COLUMN `asset_id` bigint DEFAULT NULL COMMENT '视频转码资产ID；存量直链为空' AFTER `chapter_id`,
  ADD KEY `idx_asset_id` (`asset_id`);
