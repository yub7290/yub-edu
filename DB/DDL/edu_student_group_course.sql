-- 学员组 关联课程 中间表
CREATE TABLE `edu_student_group_course` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `group_id` bigint NOT NULL COMMENT '学员组ID',
    `course_id` bigint NOT NULL COMMENT '课程ID',
    `sort_order` int DEFAULT 0 COMMENT '排序',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` bigint DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_group_course` (`group_id`, `course_id`),
    KEY `idx_group_id` (`group_id`),
    KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学员组关联课程中间表';
