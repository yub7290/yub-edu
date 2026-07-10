-- 教务模块菜单初始化（学员组）
-- 在学员管理目录（parent_id=220）下新增学员组菜单

-- 1. 学员组（菜单）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (231, 220, '学员组', '/edu/student-group', 'edu/studentGroup/index', 'UserFilled', 2, 1, 'edu:student-group:query', 1, 1);

-- ==================== 按钮权限 ====================

-- 学员组按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2311, 231, '查询学员组', 'edu:student-group:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2312, 231, '新增学员组', 'edu:student-group:add', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2313, 231, '编辑学员组', 'edu:student-group:edit', 3, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2314, 231, '删除学员组', 'edu:student-group:delete', 4, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2315, 231, '设置课程', 'edu:student-group:setCourse', 5, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2316, 231, '设置学员', 'edu:student-group:setMember', 6, 2, 1, 1);
