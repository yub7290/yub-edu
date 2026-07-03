CREATE TABLE IF NOT EXISTS edu_growth_week_plan
(
    id                bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    student_id        bigint       NOT NULL COMMENT '学员ID',
    week_start_date   date         NOT NULL COMMENT '周开始日期',
    week_end_date     date         NOT NULL COMMENT '周结束日期',
    status            tinyint      NOT NULL DEFAULT 1 COMMENT '状态：1进行中 2已完成 3历史',
    weak_points       varchar(500) NOT NULL DEFAULT '' COMMENT '薄弱知识点',
    advantage_summary varchar(1000) NOT NULL DEFAULT '' COMMENT '优势总结',
    weak_summary      varchar(1000) NOT NULL DEFAULT '' COMMENT '薄弱总结',
    study_suggestion  varchar(1000) NOT NULL DEFAULT '' COMMENT '学习建议',
    duration_target   int          NOT NULL DEFAULT 0 COMMENT '学习时长目标（分钟）',
    question_target   int          NOT NULL DEFAULT 0 COMMENT '练习题目目标',
    knowledge_target  int          NOT NULL DEFAULT 0 COMMENT '知识点目标',
    create_time       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_week (student_id, week_start_date),
    KEY idx_week_status (week_start_date, status),
    KEY idx_student_status (student_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生成长档案周计划';

CREATE TABLE IF NOT EXISTS edu_growth_plan_task
(
    id                 bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    plan_id            bigint       NOT NULL COMMENT '周计划ID',
    student_id         bigint       NOT NULL COMMENT '学员ID',
    plan_date          date         NOT NULL COMMENT '计划日期',
    task_type          varchar(32)  NOT NULL DEFAULT 'learn' COMMENT '任务类型',
    jump_type          varchar(32)  NOT NULL DEFAULT 'course' COMMENT '跳转类型',
    title              varchar(200) NOT NULL DEFAULT '' COMMENT '任务标题',
    knowledge_point_id bigint       DEFAULT NULL COMMENT '知识点ID',
    knowledge_name     varchar(200) NOT NULL DEFAULT '' COMMENT '知识点名称',
    target_id          bigint       DEFAULT NULL COMMENT '跳转目标ID',
    duration_target    int          NOT NULL DEFAULT 0 COMMENT '学习时长目标（分钟）',
    question_target    int          NOT NULL DEFAULT 0 COMMENT '题目目标',
    completed          tinyint      NOT NULL DEFAULT 0 COMMENT '是否完成：1是 0否',
    complete_time      datetime     DEFAULT NULL COMMENT '完成时间',
    create_time        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_plan_date (plan_id, plan_date),
    KEY idx_student_date (student_id, plan_date),
    KEY idx_student_completed (student_id, completed),
    KEY idx_knowledge_point (knowledge_point_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生成长档案计划任务';
