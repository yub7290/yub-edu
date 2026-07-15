CREATE TABLE `edu_chapter_attachment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chapter_id` bigint NOT NULL COMMENT '章节ID',
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `file_url` varchar(500) NOT NULL COMMENT '文件URL',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` bigint DEFAULT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_chapter_id` (`chapter_id`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='章节附件';
