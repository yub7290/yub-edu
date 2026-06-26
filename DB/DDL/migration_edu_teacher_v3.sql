ALTER TABLE `edu_teacher`
    ADD COLUMN `rating` tinyint DEFAULT 5
        COMMENT '评分（1-5分，整数）' AFTER `recommended`;
