-- 学习卡实例表
CREATE TABLE IF NOT EXISTS `edu_study_card_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `card_id` bigint NOT NULL COMMENT '学习卡模板ID',
  `card_no` varchar(50) NOT NULL COMMENT '卡号',
  `secret_code` varchar(20) DEFAULT NULL COMMENT '密钥',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态 0:未使用 1:已使用 2:已回滚 3:已禁用',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `user_id` bigint DEFAULT NULL COMMENT '使用人ID',
  `user_account` varchar(50) DEFAULT NULL COMMENT '使用人账号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常 1:已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_card_no` (`card_no`),
  KEY `idx_card_id` (`card_id`),
  KEY `idx_status` (`status`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学习卡实例表';
