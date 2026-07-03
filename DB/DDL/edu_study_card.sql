-- 学习卡模板表
CREATE TABLE IF NOT EXISTS `edu_study_card` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(200) NOT NULL COMMENT '主题',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '面额(元)',
  `coupon_deductible` tinyint DEFAULT '0' COMMENT '可抵扣卡券 1:是 0:否',
  `quantity` int NOT NULL DEFAULT '0' COMMENT '生成数量',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 1:启用 0:禁用',
  `valid_start_date` date DEFAULT NULL COMMENT '有效期开始日期',
  `valid_end_date` date DEFAULT NULL COMMENT '有效期结束日期',
  `study_duration` int DEFAULT '1' COMMENT '学习时长数值',
  `study_duration_unit` varchar(10) DEFAULT '月' COMMENT '学习时长单位(月/天)',
  `description` text COMMENT '说明',
  `card_code_length` int DEFAULT '16' COMMENT '学习码长度',
  `secret_code_length` int DEFAULT '3' COMMENT '密钥长度',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常 1:已删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学习卡模板表';
