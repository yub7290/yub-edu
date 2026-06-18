package com.yub.edu.biz.exception;

import com.yub.common.exception.ErrorCode;
import lombok.AllArgsConstructor;

/**
 * 教育模块异常枚举
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教育模块异常码定义
 * @Version: 1.0.0
 */
@AllArgsConstructor
public enum EduErrorCode implements ErrorCode {

    /** 专业不存在 */
    MAJOR_NOT_FOUND(200001, "专业不存在"),
    /** 同级专业名称已存在 */
    MAJOR_NAME_EXISTS(200002, "同级专业下名称已存在"),
    /** 专业存在子专业无法删除 */
    MAJOR_HAS_CHILDREN(200003, "该专业下存在子专业，无法删除"),
    /** 专业下存在课程无法删除 */
    MAJOR_HAS_COURSES(200004, "该专业下存在课程，无法删除"),

    /** 课程不存在 */
    COURSE_NOT_FOUND(200011, "课程不存在"),
    /** 课程名称已存在 */
    COURSE_NAME_EXISTS(200012, "课程名称已存在"),

    /** 试题不存在 */
    QUESTION_NOT_FOUND(200021, "试题不存在"),

    /** 试卷不存在 */
    EXAM_NOT_FOUND(200061, "试卷不存在"),
    /** 一个课程最多只能有一个结课考试 */
    EXAM_FINAL_EXAM_EXISTS(200032, "该课程已设置结课考试"),
    /** 试卷标题已存在 */
    EXAM_NAME_EXISTS(200062, "试卷标题已存在"),

    /** 章节不存在 */
    CHAPTER_NOT_FOUND(200041, "章节不存在"),

    /** 章节下存在子章节 */
    CHAPTER_HAS_CHILDREN(200042, "该章节下存在子章节，无法删除"),

    /** 知识点不存在 */
    KNOWLEDGE_POINT_NOT_FOUND(200051, "知识点不存在"),

    /** 公告不存在 */
    ANNOUNCEMENT_NOT_FOUND(200071, "公告不存在"),

    /** 留言不存在 */
    MESSAGE_NOT_FOUND(200072, "留言不存在"),

    /** 教师职称不存在 */
    TEACHER_TITLE_NOT_FOUND(200081, "教师职称不存在"),
    /** 教师职称下存在教师无法删除 */
    TEACHER_TITLE_HAS_TEACHERS(200082, "该职称下存在教师，无法删除"),

    /** 教师不存在 */
    TEACHER_NOT_FOUND(200091, "教师不存在"),
    /** 教师账号已存在 */
    TEACHER_ACCOUNT_EXISTS(200092, "教师账号已存在"),

    /** 学员不存在 */
    STUDENT_NOT_FOUND(200101, "学员不存在"),
    /** 学员账号已存在 */
    STUDENT_ACCOUNT_EXISTS(200102, "学员账号已存在");

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
