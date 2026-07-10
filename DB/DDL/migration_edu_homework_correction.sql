-- 作业批改记录表
CREATE TABLE IF NOT EXISTS edu_homework_correction (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  course_id bigint NOT NULL COMMENT '课程ID',
  student_id bigint NOT NULL COMMENT '学生ID',
  images text NOT NULL COMMENT '图片URL列表(JSON数组)',
  total_questions int DEFAULT 0 COMMENT '识别题目总数',
  correct_count int DEFAULT 0 COMMENT '正确题数',
  score decimal(5,1) COMMENT '得分(0-100)',
  status tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-待批改，1-批改中，2-已完成，3-批改失败',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-正常，1-已删除',
  PRIMARY KEY (id),
  KEY idx_course_student (course_id, student_id),
  KEY idx_student_time (student_id, create_time DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业批改记录';

-- 作业批改题目明细表
CREATE TABLE IF NOT EXISTS edu_homework_question (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  correction_id bigint NOT NULL COMMENT '关联批改记录ID',
  question_no int NOT NULL COMMENT '题号(从1开始)',
  question_content text COMMENT '题目内容(AI识别)',
  student_answer text COMMENT '学生答案(AI识别)',
  correct_answer varchar(500) COMMENT '正确答案(AI识别)',
  is_correct tinyint COMMENT '是否正确：1-正确，0-错误，NULL-无法判断',
  analysis text COMMENT 'AI解析',
  source_image varchar(500) COMMENT '来源图片URL',
  review_status tinyint NOT NULL DEFAULT 0 COMMENT '复查状态：0-未复查，1-已复查',
  review_result tinyint COMMENT '复查结果：1-正确，0-错误(需修正)',
  review_answer varchar(500) COMMENT '复查后正确答案',
  review_analysis text COMMENT '复查后解析',
  reviewed_by bigint COMMENT '复查人ID',
  review_time datetime COMMENT '复查时间',
  sort int NOT NULL DEFAULT 0 COMMENT '排序',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_correction (correction_id),
  KEY idx_correction_no (correction_id, question_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业批改题目明细';
