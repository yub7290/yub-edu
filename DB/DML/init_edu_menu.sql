-- 教育模块菜单初始化
-- 注意：ID 从 100 开始，避免与系统管理模块冲突

-- 1. 顶级目录：课程
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, status, create_by)
VALUES (100, 0, '课程', '/edu', 'Layout', 'Reading', 2, 0, 1, 1);

-- 2. 学习内容（目录）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, status, create_by)
VALUES (110, 100, '学习内容', '/edu/content', 'Layout', 'Notebook', 1, 0, 1, 1);

-- 2.1 课程菜单
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (111, 110, '课程', '/edu/course', 'edu/course/index', 'List', 1, 1, 'edu:course:query', 1, 1);

-- 2.2 专业菜单
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (112, 110, '专业', '/edu/major', 'edu/major/index', 'Collection', 2, 1, 'edu:major:query', 1, 1);

-- 2.3 试题库菜单
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (113, 110, '试题库', '/edu/question', 'edu/question/index', 'Document', 3, 1, 'edu:question:query', 1, 1);

-- 2.4 知识点菜单
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (114, 110, '知识点', '/edu/knowledge', 'edu/knowledge/index', 'Reading', 4, 1, 'edu:knowledge:query', 1, 1);

-- 3. 学习证明（菜单）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (120, 100, '学习证明', '/edu/certificate', 'edu/certificate/index', 'Trophy', 2, 1, 'edu:certificate:query', 1, 1);

-- 4. 学习权重（菜单）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (130, 100, '学习权重', '/edu/weight', 'edu/weight/index', 'ScaleToOriginal', 3, 1, 'edu:weight:query', 1, 1);

-- 5. 回收站（目录）
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, status, create_by)
VALUES (140, 100, '回收站', '/edu/recycle', 'Layout', 'Delete', 4, 0, 1, 1);

-- 5.1 专业回收
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (141, 140, '专业回收', '/edu/recycle/major', 'edu/recycle/major', 'Delete', 1, 1, 'edu:recycle:major:query', 1, 1);

-- 5.2 课程回收
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (142, 140, '课程回收', '/edu/recycle/course', 'edu/recycle/course', 'Delete', 2, 1, 'edu:recycle:course:query', 1, 1);

-- 5.3 试题回收
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (143, 140, '试题回收', '/edu/recycle/question', 'edu/recycle/question', 'Delete', 3, 1, 'edu:recycle:question:query', 1, 1);

-- 5.4 试卷回收
INSERT IGNORE INTO sys_menu (id, parent_id, name, path, component, icon, sort, menu_type, permission, status, create_by)
VALUES (144, 140, '试卷回收', '/edu/recycle/exam', 'edu/recycle/exam', 'Delete', 4, 1, 'edu:recycle:exam:query', 1, 1);

-- ==================== 按钮权限 ====================

-- 课程按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1111, 111, '查询课程', 'edu:course:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1112, 111, '新增课程', 'edu:course:add', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1113, 111, '编辑课程', 'edu:course:edit', 3, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1114, 111, '删除课程', 'edu:course:delete', 4, 2, 1, 1);

-- 专业按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1121, 112, '查询专业', 'edu:major:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1122, 112, '新增专业', 'edu:major:add', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1123, 112, '编辑专业', 'edu:major:edit', 3, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1124, 112, '删除专业', 'edu:major:delete', 4, 2, 1, 1);

-- 试题库按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1131, 113, '查询试题', 'edu:question:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1132, 113, '新增试题', 'edu:question:add', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1133, 113, '编辑试题', 'edu:question:edit', 3, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1134, 113, '删除试题', 'edu:question:delete', 4, 2, 1, 1);

-- 知识点按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1141, 114, '查询知识点', 'edu:knowledge:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1142, 114, '新增知识点', 'edu:knowledge:add', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1143, 114, '编辑知识点', 'edu:knowledge:edit', 3, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1144, 114, '删除知识点', 'edu:knowledge:delete', 4, 2, 1, 1);

-- 回收站按钮权限
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1411, 141, '查询专业回收', 'edu:recycle:major:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1412, 141, '恢复专业', 'edu:recycle:major:restore', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1413, 141, '彻底删除专业', 'edu:recycle:major:delete', 3, 2, 1, 1);

INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1421, 142, '查询课程回收', 'edu:recycle:course:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1422, 142, '恢复课程', 'edu:recycle:course:restore', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1423, 142, '彻底删除课程', 'edu:recycle:course:delete', 3, 2, 1, 1);

INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1431, 143, '查询试题回收', 'edu:recycle:question:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1432, 143, '恢复试题', 'edu:recycle:question:restore', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1433, 143, '彻底删除试题', 'edu:recycle:question:delete', 3, 2, 1, 1);

INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1441, 144, '查询试卷回收', 'edu:recycle:exam:query', 1, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1442, 144, '恢复试卷', 'edu:recycle:exam:restore', 2, 2, 1, 1);
INSERT IGNORE INTO sys_menu (id, parent_id, name, permission, sort, menu_type, status, create_by)
VALUES (1443, 144, '彻底删除试卷', 'edu:recycle:exam:delete', 3, 2, 1, 1);
