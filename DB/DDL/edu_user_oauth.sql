CREATE TABLE `edu_user_oauth` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `platform` varchar(20) NOT NULL COMMENT '平台标识: wechat/qq',
  `open_id` varchar(128) NOT NULL COMMENT '平台用户唯一标识',
  `union_id` varchar(128) DEFAULT NULL COMMENT '跨应用唯一标识',
  `nickname` varchar(64) DEFAULT NULL COMMENT '社交账号昵称',
  `avatar_url` varchar(256) DEFAULT NULL COMMENT '社交账号头像URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_platform` (`user_id`,`platform`),
  KEY `idx_open_id` (`open_id`),
  KEY `idx_deleted` (`deleted`),
  UNIQUE KEY `uk_platform_openid` (`platform`,`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户第三方账号绑定表';
