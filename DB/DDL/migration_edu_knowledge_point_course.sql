ALTER TABLE `edu_knowledge_point`
  ADD COLUMN `course_id` bigint DEFAULT NULL COMMENT '所属课程ID' AFTER `category_id`,
  ADD KEY `idx_course_id` (`course_id`);
