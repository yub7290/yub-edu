CREATE TABLE `edu_study_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_id` bigint NOT NULL COMMENT '学员ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `chapter_id` bigint NOT NULL COMMENT '章节ID',
  `play_second` int NOT NULL DEFAULT '0' COMMENT '播放进度（秒）',
  `total_study_second` int NOT NULL DEFAULT '0' COMMENT '累计学习时长（秒）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_chapter` (`student_id`, `course_id`, `chapter_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学员学习记录表';
