-- ============================================================
-- 考试记录表 DDL（完整版，含 status/start_time/heartbeat_time）
-- ============================================================

DROP TABLE IF EXISTS `edu_exam_record`;

CREATE TABLE `edu_exam_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `exam_id` bigint NOT NULL COMMENT '试卷ID',
  `attempt_no` int NOT NULL DEFAULT '1' COMMENT '第几次考试',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态 0:进行中 1:已提交 2:超时交卷',
  `score` int NOT NULL DEFAULT '0' COMMENT '实际得分',
  `total_score` int NOT NULL DEFAULT '0' COMMENT '试卷满分',
  `pass_score` int NOT NULL DEFAULT '0' COMMENT '及格分',
  `is_pass` tinyint NOT NULL DEFAULT '0' COMMENT '是否及格 1:是 0:否',
  `duration` int NOT NULL DEFAULT '0' COMMENT '答题用时（秒）',
  `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `heartbeat_time` datetime DEFAULT NULL COMMENT '最后心跳时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常 1:已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_exam` (`user_id`, `exam_id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_submit_time` (`submit_time`),
  KEY `idx_heartbeat_time` (`heartbeat_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考试记录表';
