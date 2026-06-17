-- 教务模块菜单初始化（学员管理）
-- 注意：ID 从 220 开始，避免与现有模块冲突

-- 1. 学员管理（目录）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, status, create_by)
VALUES (220, 200, '学员管理', '/edu/student', 'Layout', 'UserFilled', 2, 0, 1, 1);

-- 2. 学员信息（菜单）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (221, 220, '学员信息', '/edu/student', 'edu/student/index', 'UserFilled', 1, 1, 'edu:student:query', 1, 1);

-- ==================== 按钮权限 ====================

-- 学员信息按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2211, 221, '查询学员', 'edu:student:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2212, 221, '新增学员', 'edu:student:add', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2213, 221, '编辑学员', 'edu:student:edit', 3, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2214, 221, '删除学员', 'edu:student:delete', 4, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2215, 221, '导出学员', 'edu:student:export', 5, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2216, 221, '导入学员', 'edu:student:import', 6, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2217, 221, '重置密码', 'edu:student:resetPwd', 7, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2218, 221, '批量禁用', 'edu:student:batchDisable', 8, 2, 1, 1);
