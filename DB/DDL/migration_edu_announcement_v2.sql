-- 公告表新增字段
ALTER TABLE `edu_announcement`
  ADD COLUMN `long_title` varchar(500) DEFAULT NULL COMMENT '长标题（展示详情时优先显示）' AFTER `title`,
  ADD COLUMN `category` varchar(100) DEFAULT NULL COMMENT '分类' AFTER `long_title`,
  ADD COLUMN `summary` varchar(500) DEFAULT NULL COMMENT '简述' AFTER `category`,
  ADD COLUMN `source` varchar(200) DEFAULT NULL COMMENT '来源' AFTER `summary`,
  ADD COLUMN `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 1:启用 0:禁用' AFTER `source`;
