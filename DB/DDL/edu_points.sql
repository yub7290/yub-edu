-- 积分账户表
CREATE TABLE edu_points_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    available_points INT NOT NULL DEFAULT 0 COMMENT '可用积分',
    total_points INT NOT NULL DEFAULT 0 COMMENT '累计获得积分',
    version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='积分账户表';

-- 积分记录表
CREATE TABLE edu_points_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    points INT NOT NULL COMMENT '变动积分（正数为获得，负数为消耗）',
    change_type TINYINT NOT NULL COMMENT '变动类型: 1 获得, 2 消耗',
    description VARCHAR(255) NOT NULL COMMENT '变动描述',
    biz_id VARCHAR(64) DEFAULT NULL COMMENT '关联业务ID（如课程ID、考试ID等）',
    biz_type VARCHAR(32) DEFAULT NULL COMMENT '关联业务类型',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='积分记录表';

-- 积分商品表
CREATE TABLE edu_points_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    product_type TINYINT NOT NULL DEFAULT 1 COMMENT '商品类型: 1=实物商品 2=学习卡',
    study_card_id BIGINT DEFAULT NULL COMMENT '关联学习卡ID（product_type=2时有效）',
    image_url VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
    required_points INT NOT NULL COMMENT '所需积分',
    stock_count INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0 下架, 1 上架',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除（0=正常 1=已删除）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='积分商品表';
