CREATE TABLE `edu_chapter_video` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chapter_id` bigint NOT NULL COMMENT '章节ID',
  `video_name` varchar(200) NOT NULL COMMENT '视频名称',
  `video_url` varchar(500) NOT NULL COMMENT '视频URL',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小（字节）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常 1:已删除',
  PRIMARY KEY (`id`),
  KEY `idx_chapter_id` (`chapter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='章节视频表';
