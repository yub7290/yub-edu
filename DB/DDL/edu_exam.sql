-- ============================================================
-- 试卷表（完整 DDL，含 question_range_type）
-- 首次部署执行此文件；已有表执行 migration_edu_exam_v2.sql
-- ============================================================

-- 如果表已存在（旧版），先删除旧的外键依赖表再重建
DROP TABLE IF EXISTS `edu_exam_chapter_question_config`;
DROP TABLE IF EXISTS `edu_exam_question_type_config`;

CREATE TABLE `edu_exam` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(200) NOT NULL COMMENT '试卷标题',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '副标题',
  `major_id` bigint DEFAULT NULL COMMENT '所属专业ID',
  `course_id` bigint NOT NULL COMMENT '所属课程ID',
  `is_final_exam` tinyint NOT NULL DEFAULT '0' COMMENT '是否结课考试 1:是 0:否',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 1:启用 0:禁用',
  `recommended` tinyint NOT NULL DEFAULT '0' COMMENT '推荐 1:是 0:否',
  `difficulty` tinyint NOT NULL DEFAULT '3' COMMENT '难度 1-5',
  `duration` int NOT NULL DEFAULT '60' COMMENT '考试时长（分钟）',
  `total_score` int NOT NULL DEFAULT '100' COMMENT '满分',
  `pass_score` int NOT NULL DEFAULT '60' COMMENT '及格分',
  `introduction` text COMMENT '简介（富文本）',
  `notes` text COMMENT '注意事项（富文本）',
  `examiner` varchar(100) DEFAULT NULL COMMENT '出卷人',
  `logo` varchar(500) DEFAULT NULL COMMENT '试卷logo',
  `question_range_type` tinyint NOT NULL DEFAULT '0' COMMENT '出题范围 0:当前课程所有试题 1:按章节出题',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常 1:已删除',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试卷表';

CREATE TABLE `edu_exam_question_type_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exam_id` bigint NOT NULL COMMENT '试卷ID',
  `question_type` tinyint NOT NULL COMMENT '试题类型 0:单选 1:多选 2:判断 3:简答 4:填空',
  `question_count` int NOT NULL DEFAULT '0' COMMENT '抽取题数',
  `score_per_question` int NOT NULL DEFAULT '0' COMMENT '每题分值',
  PRIMARY KEY (`id`),
  KEY `idx_exam_id` (`exam_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试卷试题类型配置表';

CREATE TABLE `edu_exam_chapter_question_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exam_id` bigint NOT NULL COMMENT '试卷ID',
  `chapter_id` bigint NOT NULL COMMENT '章节ID',
  `question_type` tinyint NOT NULL COMMENT '试题类型 0:单选 1:多选 2:判断 3:简答 4:填空',
  `question_count` int NOT NULL DEFAULT '0' COMMENT '抽取题数',
  `score_per_question` int NOT NULL DEFAULT '0' COMMENT '每题分值',
  PRIMARY KEY (`id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_chapter_id` (`chapter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试卷章节出题配置表';
