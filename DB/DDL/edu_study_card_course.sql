-- 学习卡关联课程表
CREATE TABLE IF NOT EXISTS `edu_study_card_course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `card_id` bigint NOT NULL COMMENT '学习卡模板ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_card_course` (`card_id`, `course_id`),
  KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学习卡关联课程表';
