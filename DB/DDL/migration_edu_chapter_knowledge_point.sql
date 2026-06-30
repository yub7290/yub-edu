CREATE TABLE `edu_chapter_knowledge_point` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chapter_id` bigint NOT NULL COMMENT '章节ID',
  `knowledge_point_id` bigint NOT NULL COMMENT '知识点ID',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_chapter_kp` (`chapter_id`, `knowledge_point_id`),
  KEY `idx_knowledge_point_id` (`knowledge_point_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='章节关联知识点';
