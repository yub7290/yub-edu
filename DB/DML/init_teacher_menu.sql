-- 教务模块菜单初始化（教师管理）
-- 注意：ID 从 200 开始，避免与现有模块冲突

-- 1. 顶级目录：教务
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, status, create_by)
VALUES (200, 0, '教务', '/edu-teacher', 'Layout', 'UserFilled', 3, 0, 1, 1);

-- 2. 教师管理（目录）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, status, create_by)
VALUES (210, 200, '教师管理', '/edu/teacher', 'Layout', 'Avatar', 1, 0, 1, 1);

-- 2.1 教师信息（菜单）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (211, 210, '教师信息', '/edu/teacher', 'edu/teacher/index', 'User', 1, 1, 'edu:teacher:query', 1, 1);

-- 2.2 教师职称（菜单）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (212, 210, '教师职称', '/edu/teacher-title', 'edu/teacher-title/index', 'Collection', 2, 1, 'edu:teacherTitle:query', 1, 1);

-- ==================== 按钮权限 ====================

-- 教师信息按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2111, 211, '查询教师', 'edu:teacher:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2112, 211, '新增教师', 'edu:teacher:add', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2113, 211, '编辑教师', 'edu:teacher:edit', 3, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2114, 211, '删除教师', 'edu:teacher:delete', 4, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2115, 211, '导出教师', 'edu:teacher:export', 5, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2116, 211, '导入教师', 'edu:teacher:import', 6, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2117, 211, '重置密码', 'edu:teacher:resetPwd', 7, 2, 1, 1);

-- 教师职称按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2121, 212, '查询职称', 'edu:teacherTitle:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2122, 212, '新增职称', 'edu:teacherTitle:add', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2123, 212, '编辑职称', 'edu:teacherTitle:edit', 3, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (2124, 212, '删除职称', 'edu:teacherTitle:delete', 4, 2, 1, 1);
