-- 试卷表新增字段
ALTER TABLE `edu_exam`
  ADD COLUMN `introduction` text COMMENT '简介（富文本）' AFTER `pass_score`,
  ADD COLUMN `notes` text COMMENT '注意事项（富文本）' AFTER `introduction`,
  ADD COLUMN `examiner` varchar(100) DEFAULT NULL COMMENT '出卷人' AFTER `notes`,
  ADD COLUMN `logo` varchar(500) DEFAULT NULL COMMENT '试卷logo' AFTER `examiner`;

-- 试卷试题类型配置表（记录每种题型选了多少道、每道多少分）
CREATE TABLE `edu_exam_question_type_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exam_id` bigint NOT NULL COMMENT '试卷ID',
  `question_type` tinyint NOT NULL COMMENT '试题类型 0:单选 1:多选 2:判断 3:简答 4:填空',
  `question_count` int NOT NULL DEFAULT '0' COMMENT '抽取题数',
  `score_per_question` int NOT NULL DEFAULT '0' COMMENT '每题分值',
  PRIMARY KEY (`id`),
  KEY `idx_exam_id` (`exam_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试卷试题类型配置表';

-- 试卷章节出题配置表（按章节出题时，每章节各题型抽取数量）
CREATE TABLE `edu_exam_chapter_question_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exam_id` bigint NOT NULL COMMENT '试卷ID',
  `chapter_id` bigint NOT NULL COMMENT '章节ID',
  `question_type` tinyint NOT NULL COMMENT '试题类型 0:单选 1:多选 2:判断 3:简答 4:填空',
  `question_count` int NOT NULL DEFAULT '0' COMMENT '抽取题数',
  `score_per_question` int NOT NULL DEFAULT '0' COMMENT '每题分值',
  PRIMARY KEY (`id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_chapter_id` (`chapter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试卷章节出题配置表';
