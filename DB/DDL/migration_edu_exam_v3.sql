-- 试卷表新增出题范围字段（修复 BadSqlGrammarException: Unknown column 'question_range_type'）
ALTER TABLE `edu_exam`
  ADD COLUMN `question_range_type` tinyint NOT NULL DEFAULT '0' COMMENT '出题范围 0:当前课程所有试题 1:按章节出题'
  AFTER `logo`;
