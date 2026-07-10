-- 新闻资讯分类表（后台可配置）
CREATE TABLE `edu_news_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序（从小到大）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 1:启用 0:禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除 0:正常 1:已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='新闻资讯分类表';

-- 新闻资讯表
CREATE TABLE `edu_news` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(200) NOT NULL COMMENT '资讯标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '摘要',
  `cover_url` varchar(500) DEFAULT NULL COMMENT '封面图URL',
  `content` text COMMENT '资讯内容（富文本）',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `source` varchar(200) DEFAULT NULL COMMENT '来源',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态 0:草稿 1:已发布',
  `views` int NOT NULL DEFAULT '0' COMMENT '阅读量',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除 0:正常 1:已删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='新闻资讯表';
