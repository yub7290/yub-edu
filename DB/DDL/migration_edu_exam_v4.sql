-- ============================================================
-- 试卷表新增字段：最大参考次数、章节完成率准入门槛
-- 执行此迁移前确保 edu_exam 表已存在
-- 说明：使用存储过程兼容低版本 MySQL，可重复执行
-- ============================================================

DELIMITER $$

CREATE PROCEDURE IF NOT EXISTS AddEduExamColumn(
    IN colName VARCHAR(64),
    IN colDef TEXT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = DATABASE()
          AND table_name = 'edu_exam'
          AND column_name = colName
    ) THEN
        SET @sql = CONCAT('ALTER TABLE edu_exam ADD COLUMN ', colName, ' ', colDef);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$

DELIMITER ;

CALL AddEduExamColumn('max_attempts', "int NOT NULL DEFAULT '0' COMMENT '最大参考次数（0=不限）' AFTER question_range_type");
CALL AddEduExamColumn('chapter_pass_rate', "tinyint NOT NULL DEFAULT '0' COMMENT '章节完成率准入门槛（%，0=不校验）' AFTER max_attempts");

DROP PROCEDURE IF EXISTS AddEduExamColumn;
