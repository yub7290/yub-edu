-- ============================================================
-- 考试记录表结构升级迁移脚本（v2 最终版）
-- 当前表结构实际缺少字段：status, start_time, heartbeat_time
-- 已存在字段：id, user_id, exam_id, attempt_no, score, total_score,
--            pass_score, is_pass, duration, submit_time, create_time,
--            update_time, deleted
-- ============================================================

-- 1. 新增缺失字段
ALTER TABLE `edu_exam_record`
  ADD COLUMN `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 0:进行中 1:已提交 2:超时交卷' AFTER `exam_id`,
  ADD COLUMN `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间' AFTER `duration`,
  ADD COLUMN `heartbeat_time` datetime DEFAULT NULL COMMENT '最后心跳时间' AFTER `start_time`;

-- 2. 新增索引
ALTER TABLE `edu_exam_record` ADD KEY `idx_status` (`status`);

-- 3. 旧数据兼容性处理
-- 旧记录都是已提交状态，status 默认 1 已经正确；
-- 将 start_time 回填为 create_time，避免定时任务按 start_time 判断超时出错
UPDATE `edu_exam_record` SET `start_time` = `create_time`;

-- 将 heartbeat_time 回填为 submit_time（旧记录已完成）
UPDATE `edu_exam_record` SET `heartbeat_time` = `submit_time` WHERE `heartbeat_time` IS NULL;
