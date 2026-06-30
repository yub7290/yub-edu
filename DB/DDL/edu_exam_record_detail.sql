-- ============================================================
-- 考试答题明细表 DDL
-- ============================================================

DROP TABLE IF EXISTS `edu_exam_record_detail`;

CREATE TABLE `edu_exam_record_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `record_id` bigint NOT NULL COMMENT '考试记录ID',
  `exam_id` bigint NOT NULL COMMENT '试卷ID',
  `question_id` bigint NOT NULL COMMENT '试题ID',
  `user_answer` text COMMENT '用户答案',
  `correct_answer` text COMMENT '正确答案',
  `is_correct` tinyint NOT NULL DEFAULT '0' COMMENT '是否正确 1:是 0:否',
  `score` int NOT NULL DEFAULT '0' COMMENT '本题得分',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`),
  KEY `idx_exam_question` (`exam_id`, `question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考试答题明细表';
