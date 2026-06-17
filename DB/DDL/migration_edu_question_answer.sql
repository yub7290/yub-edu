-- 试题表添加 answer 列（判断/简答用）
ALTER TABLE `edu_question`
ADD COLUMN `answer` text COMMENT '答案（判断题存 true/false，简答题存文本）'
AFTER `knowledge_points`;
