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
    /** 考试已被禁用 */
    EXAM_DISABLED(200063, "考试已被禁用"),
    /** 考试中存在未作答题目 */
    EXAM_ANSWER_EMPTY(200064, "存在未作答题目"),
    /** 题库题目数量不足 */
    EXAM_QUESTION_NOT_ENOUGH(200065, "题库题目数量不足，无法生成试卷"),
    /** 提交答案与试卷规则不匹配 */
    EXAM_ANSWER_INVALID(200066, "提交答案与试卷规则不匹配"),

    /** 达到最大参考次数 */
    EXAM_MAX_ATTEMPTS_REACHED(200067, "已达到最大参考次数，无法继续考试"),
    /** 章节完成率不足 */
    EXAM_CHAPTER_PROGRESS_NOT_ENOUGH(200068, "章节学习进度不足，无法参加结课考试"),
    /** 存在进行中的考试记录 */
    EXAM_HAS_IN_PROGRESS_RECORD(200069, "存在进行中的考试，请先完成"),
    /** 考试记录不存在 */
    EXAM_RECORD_NOT_FOUND(200070, "考试记录不存在"),
    /** 考试已提交 */
    EXAM_ALREADY_SUBMITTED(200071, "考试已提交，无法重复提交"),

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
    STUDENT_ACCOUNT_EXISTS(200102, "学员账号已存在"),

    /** 验证码错误 */
    CAPTCHA_ERROR(200201, "验证码错误"),
    /** 账号或密码错误 */
    STUDENT_PASSWORD_ERROR(200202, "账号或密码错误"),
    /** 账号已被禁用 */
    STUDENT_ACCOUNT_DISABLED(200203, "账号已被禁用"),
    /** Token已过期 */
    TOKEN_EXPIRED(200204, "Token已过期"),
    /** Token无效 */
    TOKEN_INVALID(200205, "Token无效"),

    /** 学员未学习该课程 */
    COURSE_NOT_STUDIED(200301, "请先学习该课程后再使用AI助教"),
    /** AI服务调用异常 */
    AI_SERVICE_ERROR(200302, "AI服务暂时不可用，请稍后重试"),
    /** AI会话不存在 */
    AI_CONVERSATION_NOT_FOUND(200303, "会话不存在"),
    /** 今日对话次数已达上限 */
    AI_DAILY_LIMIT_EXCEEDED(200304, "今日对话次数已达上限，请明天再试"),

    /** 练习记录不存在 */
    PRACTICE_RECORD_NOT_FOUND(200401, "练习记录不存在"),
    /** 笔记不存在 */
    NOTE_NOT_FOUND(200402, "笔记不存在"),
    /** 没有更多题目 */
    NO_MORE_QUESTIONS(200403, "没有更多题目"),
    /** 当前章节没有试题 */
    CHAPTER_NO_QUESTIONS(200404, "当前章节没有试题");

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
