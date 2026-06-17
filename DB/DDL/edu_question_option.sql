CREATE TABLE `edu_question_option` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `question_id` bigint NOT NULL COMMENT '试题ID',
  `label` varchar(10) NOT NULL COMMENT '选项标签（A/B/C/D）',
  `content` text NOT NULL COMMENT '选项内容（富文本）',
  `is_correct` tinyint NOT NULL DEFAULT '0' COMMENT '是否为正确答案 1:是 0:否',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试题选项表';

-- 给 edu_question 添加 answer 字段（判断/简答用）
ALTER TABLE `edu_question` ADD COLUMN `answer` text COMMENT '答案（判断题存 true/false，简答题存文本）' AFTER `knowledge_points`;
