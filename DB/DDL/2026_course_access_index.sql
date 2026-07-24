-- 课程学习权限控制：补充索引（性能）
-- 关联 PRD：移动端课程学习权限控制
-- 说明：新建索引以支持 canAccess / batchAccessible / myCourse 的权限判定查询，避免全表扫描。
-- 执行前请确认索引不存在；如已存在可忽略重复创建报错。

-- 1) 课程订单：按 (用户, 课程, 状态) 判定"已支付"
--    覆盖 countPaidOrderByUserAndCourse 与 myCourse 中的 EXISTS 子查询
CREATE INDEX idx_course_order_user_course_status
    ON edu_course_order (user_id, course_id, status);

-- 2) 学员-组成员：按 student_id 反查所属组（组绑定判定）
--    覆盖 edu_student_group_member WHERE student_id = ?（uk_group_student 为 (group_id, student_id)，需学生维度前导索引）
CREATE INDEX idx_group_member_student
    ON edu_student_group_member (student_id);
