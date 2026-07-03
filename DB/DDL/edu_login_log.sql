-- 学员登录日志表
DROP TABLE IF EXISTS `edu_login_log`;
CREATE TABLE `edu_login_log` (
  `id`         bigint    NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_id` bigint    NOT NULL                COMMENT '学员ID',
  `ip`         varchar(64)   DEFAULT NULL        COMMENT '登录IP',
  `user_agent` varchar(500)  DEFAULT NULL        COMMENT 'User-Agent',
  `status`     tinyint  NOT NULL DEFAULT 1       COMMENT '状态(1=成功 0=失败)',
  `error_msg`  varchar(200)  DEFAULT NULL        COMMENT '失败原因',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员登录日志';
