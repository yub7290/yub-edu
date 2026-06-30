-- ============================================================
-- 考试记录表新增 attempt_no 字段
-- 用于记录第几次考试，方便重考次数统计
-- ============================================================

ALTER TABLE `edu_exam_record`
  ADD COLUMN `attempt_no` int NOT NULL DEFAULT '1' COMMENT '第几次考试'
  AFTER `exam_id`;
