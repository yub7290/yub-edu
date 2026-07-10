-- 好友关系表（双向存储：A 的好友含 B、B 的好友也含 A，各一条记录）
CREATE TABLE edu_friend (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    student_id BIGINT NOT NULL COMMENT '关系归属学员（拥有该好友的人）',
    friend_id BIGINT NOT NULL COMMENT '好友学员ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '成为好友时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常 1:已删除',
    INDEX idx_student_id (student_id),
    INDEX idx_friend_id (friend_id),
    UNIQUE KEY uk_pair (student_id, friend_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员好友关系表';
