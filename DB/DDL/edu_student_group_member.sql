-- 学员组 关联学员 中间表
CREATE TABLE `edu_student_group_member` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `group_id` bigint NOT NULL COMMENT '学员组ID',
    `student_id` bigint NOT NULL COMMENT '学员ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` bigint DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_group_student` (`group_id`, `student_id`),
    KEY `idx_group_id` (`group_id`),
    KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学员组关联学员中间表';
