CREATE TABLE `edu_teacher_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `teacher_id` bigint NOT NULL COMMENT '教师ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teacher_role` (`teacher_id`, `role_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教师角色关联表';
