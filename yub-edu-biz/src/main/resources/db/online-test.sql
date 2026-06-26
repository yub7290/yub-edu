-- 在线测试考试记录表
CREATE TABLE IF NOT EXISTS edu_exam_record
(
    id          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id     bigint   NOT NULL COMMENT '用户ID',
    exam_id     bigint   NOT NULL COMMENT '试卷ID',
    score       int      NOT NULL DEFAULT 0 COMMENT '实际得分',
    total_score int      NOT NULL DEFAULT 0 COMMENT '试卷满分',
    pass_score  int      NOT NULL DEFAULT 0 COMMENT '及格分',
    is_pass     tinyint  NOT NULL DEFAULT 0 COMMENT '是否及格 1:是 0:否',
    duration    int      NOT NULL DEFAULT 0 COMMENT '答题用时（秒）',
    submit_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     tinyint  NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常 1:已删除',
    PRIMARY KEY (id),
    KEY idx_user_exam (user_id, exam_id),
    KEY idx_exam_id (exam_id),
    KEY idx_submit_time (submit_time)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '考试记录表';

-- 在线测试考试答题明细表
CREATE TABLE IF NOT EXISTS edu_exam_record_detail
(
    id             bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    record_id      bigint   NOT NULL COMMENT '考试记录ID',
    exam_id        bigint   NOT NULL COMMENT '试卷ID',
    question_id    bigint   NOT NULL COMMENT '试题ID',
    user_answer    text     NULL COMMENT '用户答案',
    correct_answer text     NULL COMMENT '正确答案',
    is_correct     tinyint  NOT NULL DEFAULT 0 COMMENT '是否正确 1:是 0:否',
    score          int      NOT NULL DEFAULT 0 COMMENT '本题得分',
    create_time    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_record_id (record_id),
    KEY idx_exam_question (exam_id, question_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '考试答题明细表';
