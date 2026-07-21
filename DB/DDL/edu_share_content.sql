CREATE TABLE `edu_share_content` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` VARCHAR(100) NOT NULL COMMENT '分享标题',
  `description` VARCHAR(500) COMMENT '分享描述',
  `image_url` VARCHAR(500) COMMENT '分享图片URL',
  `content` TEXT COMMENT '分享内容',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-禁用，1-启用',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `create_by` BIGINT COMMENT '创建人',
  `update_by` BIGINT COMMENT '更新人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分享内容表';