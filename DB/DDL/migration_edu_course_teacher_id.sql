-- 为 edu_course 表新增 teacher_id 字段，用于关联教师
ALTER TABLE edu_course
    ADD COLUMN teacher_id BIGINT DEFAULT NULL COMMENT '教师ID' AFTER teacher;
