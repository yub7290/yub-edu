CREATE TABLE `edu_knowledge_relation` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `source_id` bigint NOT NULL COMMENT '源知识点ID（前置知识点）',
    `target_id` bigint NOT NULL COMMENT '目标知识点ID（后续知识点）',
    `relation_type` tinyint NOT NULL DEFAULT 1 COMMENT '关系类型：1-前置依赖，2-进阶延伸，3-关联参考，4-对比对照',
    `description` varchar(500) DEFAULT NULL COMMENT '关系描述',
    `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` bigint DEFAULT NULL COMMENT '创建人',
    `update_by` bigint DEFAULT NULL COMMENT '更新人',
    `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_source_target` (`source_id`, `target_id`, `relation_type`),
    KEY `idx_source_id` (`source_id`),
    KEY `idx_target_id` (`target_id`),
    KEY `idx_status` (`status`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识点关联关系表';

INSERT INTO `edu_knowledge_relation` (`source_id`, `target_id`, `relation_type`, `description`, `sort`, `status`) VALUES
(1, 2, 1, '学习本章前需要先掌握基础概念', 0, 1),
(2, 3, 1, '进阶内容依赖于基础知识', 0, 1),
(1, 3, 2, '基础概念的进阶应用', 1, 1),
(3, 4, 1, '高级技巧需要掌握进阶知识', 0, 1),
(2, 4, 3, '进阶知识与高级技巧相关联', 0, 1),
(3, 5, 4, '两种方法可以对比学习', 0, 1);