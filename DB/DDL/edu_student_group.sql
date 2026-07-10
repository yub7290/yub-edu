-- 学员组主表
CREATE TABLE `edu_student_group` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(100) NOT NULL COMMENT '组名称',
    `description` varchar(500) DEFAULT NULL COMMENT '组说明',
    `sort_order` int DEFAULT 0 COMMENT '排序',
    `status` tinyint DEFAULT 1 COMMENT '状态(1=启用, 0=禁用)',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` bigint DEFAULT NULL COMMENT '创建人',
    `update_by` bigint DEFAULT NULL COMMENT '更新人',
    `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除(0=正常, 1=已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学员组';
