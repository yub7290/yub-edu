ALTER TABLE `edu_teacher` ADD COLUMN `recommended` tinyint DEFAULT 0 COMMENT '是否推荐（1是 0否）' AFTER `status`;
