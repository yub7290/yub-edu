-- 作业批改记录表：新增「人工复查状态」列（三态）
-- 0: 未复查  1: 复查中（部分题目已复查）  2: 已复查（全部题目复查完成）
-- 该状态由 HomeworkCorrectionServiceImpl.recalculateCorrectionStats 按题目复查完成度自动重算。
ALTER TABLE edu_homework_correction
    ADD COLUMN review_status tinyint NOT NULL DEFAULT 0 COMMENT '人工复查状态：0-未复查，1-复查中，2-已复查'
    AFTER status;
