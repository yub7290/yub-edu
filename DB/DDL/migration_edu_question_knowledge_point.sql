CREATE TABLE `edu_question_knowledge_point` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question_id` bigint NOT NULL COMMENT '试题ID',
  `knowledge_point_id` bigint NOT NULL COMMENT '知识点ID',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_question_kp` (`question_id`, `knowledge_point_id`),
  KEY `idx_knowledge_point_id` (`knowledge_point_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试题关联知识点';
