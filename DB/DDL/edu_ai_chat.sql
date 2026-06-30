-- AI助教会话表
CREATE TABLE `edu_ai_conversation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `title` varchar(100) DEFAULT '新对话' COMMENT '会话标题',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_course` (`student_id`, `course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI助教会话表';

-- AI助教消息表
CREATE TABLE `edu_ai_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `conversation_id` bigint NOT NULL COMMENT '会话ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `role` varchar(20) NOT NULL COMMENT '角色：user-学生，assistant-AI助教',
  `content` text COMMENT '文本消息内容',
  `media_type` varchar(20) DEFAULT NULL COMMENT '媒体类型：image-图片，video-视频',
  `media_url` varchar(500) DEFAULT NULL COMMENT '媒体文件URL',
  `media_base64` longtext COMMENT '媒体文件Base64编码（临时存储）',
  `token_count` int DEFAULT 0 COMMENT 'Token消耗量',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_conversation` (`conversation_id`),
  KEY `idx_student_course` (`student_id`, `course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI助教消息表';

-- AI助教配置表（每个课程可配置独立的AI助教）
CREATE TABLE `edu_ai_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `enabled` tinyint DEFAULT 0 COMMENT '是否启用AI助教：0-禁用，1-启用',
  `system_prompt` text COMMENT '系统提示词（AI助教角色设定）',
  `model` varchar(100) DEFAULT 'deepseek-v4-flash' COMMENT '模型名称',
  `daily_limit` int DEFAULT 100 COMMENT '每日对话次数限制',
  `temperature` decimal(3,2) DEFAULT 0.70 COMMENT '温度参数',
  `max_tokens` int DEFAULT 2000 COMMENT '最大Token数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI助教配置表';
