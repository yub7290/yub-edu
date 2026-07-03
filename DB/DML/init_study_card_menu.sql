-- 学习卡菜单初始化
-- 已存在于 sys_menu: id=400, parent_id=200(教务), sort=5
-- 按钮权限: id=4001~4005

-- 1. 学习卡菜单（教务目录下，与教师管理sort=1、学员管理sort=2同级）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (400, 200, '学习卡', '/edu/study-card', 'edu/study-card/index', 'Ticket', 5, 1, 'edu:study-card:query', 1, 1);

-- ==================== 按钮权限 ====================

-- 学习卡按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (4001, 400, '查询学习卡', 'edu:study-card:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (4002, 400, '新增学习卡', 'edu:study-card:add', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (4003, 400, '编辑学习卡', 'edu:study-card:edit', 3, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (4004, 400, '删除学习卡', 'edu:study-card:delete', 4, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (4005, 400, '卡号管理', 'edu:study-card:instance', 5, 2, 1, 1);
