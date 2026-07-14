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
    /** 不能移动到自身的子级下（循环引用） */
    MAJOR_CYCLE(200005, "不能将分类移动到其子级下"),

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
    /** 知识点关系不存在 */
    KNOWLEDGE_RELATION_NOT_FOUND(200052, "知识点关系不存在"),
    /** 知识点关系已存在 */
    KNOWLEDGE_RELATION_EXISTS(200053, "该知识点关系已存在"),
    /** 不能创建自关联 */
    KNOWLEDGE_RELATION_SELF(200054, "不能创建知识点自关联"),

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

    /** 好友关系不存在 */
    FRIEND_NOT_FOUND(200821, "好友关系不存在"),
    /** 该好友已添加 */
    FRIEND_ALREADY(200822, "该好友已添加"),
    /** 不能添加自己为好友 */
    FRIEND_SELF(200823, "不能添加自己为好友"),

    /** 验证码错误 */
    CAPTCHA_ERROR(200201, "验证码错误"),
    /** 账号或密码错误 */
    STUDENT_PASSWORD_ERROR(200202, "账号或密码错误"),
    /** 账号已被禁用 */
    STUDENT_ACCOUNT_DISABLED(200203, "账号已被禁用"),
    /** 原密码错误 */
    OLD_PASSWORD_ERROR(200206, "原密码错误"),
    /** 密码强度不足 */
    PASSWORD_STRENGTH_ERROR(200207, "密码强度不足，需包含大写字母、小写字母、数字、符号中至少三类"),
    /** Token已过期 */
    TOKEN_EXPIRED(200204, "Token已过期"),
    /** Token无效 */
    TOKEN_INVALID(200205, "Token无效"),

    /** 教师账号或密码错误 */
    TEACHER_PASSWORD_ERROR(200211, "账号或密码错误"),
    /** 教师账号已被禁用 */
    TEACHER_ACCOUNT_DISABLED(200212, "账号已被禁用"),

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
    CHAPTER_NO_QUESTIONS(200404, "当前章节没有试题"),

    /** 学习卡不存在 */
    STUDY_CARD_NOT_FOUND(200501, "学习卡不存在"),
    /** 学习卡实例不存在 */
    STUDY_CARD_INSTANCE_NOT_FOUND(200502, "学习卡实例不存在"),
    /** 学习卡已使用 */
    STUDY_CARD_INSTANCE_USED(200503, "学习卡已使用"),
    /** 学习卡已回滚 */
    STUDY_CARD_INSTANCE_ROLLBACK(200504, "学习卡已回滚"),
    /** 学习卡已禁用 */
    STUDY_CARD_INSTANCE_DISABLED(200505, "学习卡已禁用"),
    /** 学习卡已过期 */
    STUDY_CARD_NO_EXPIRED(200506, "学习卡已过期"),
    /** 学习卡号不存在 */
    STUDY_CARD_NO_NOT_EXIST(200507, "学习卡号不存在"),

    /** 积分余额不足 */
    POINTS_INSUFFICIENT(200601, "积分余额不足"),
    /** 积分获取失败（乐观锁冲突） */
    POINTS_CONFLICT(200602, "积分操作冲突，请重试"),

    /** 订单不存在 */
    ORDER_NOT_FOUND(200701, "订单不存在"),
    /** 订单已处理 */
    ORDER_ALREADY_PROCESSED(200702, "订单已处理，无法重复操作"),
    /** 订单已取消 */
    ORDER_ALREADY_CANCELLED(200703, "订单已取消"),

    /** 地址不存在 */
    ADDRESS_NOT_FOUND(200801, "地址不存在"),

    /** 参数错误 */
    PARAM_INVALID(200901, "参数错误"),

    // ========== 资金/支付 20091x ==========
    /** 资金账户不存在 */
    FUND_ACCOUNT_NOT_FOUND(200910, "资金账户不存在"),
    /** 余额不足 */
    BALANCE_INSUFFICIENT(200911, "余额不足"),
    /** 支付订单不存在 */
    PAYMENT_ORDER_NOT_FOUND(200912, "支付订单不存在"),
    /** 支付订单已过期 */
    PAYMENT_ORDER_EXPIRED(200913, "支付订单已过期"),
    /** 支付金额与订单不一致 */
    PAYMENT_AMOUNT_MISMATCH(200914, "支付金额与订单不一致"),
    /** 课程订单不存在 */
    COURSE_ORDER_NOT_FOUND(200915, "课程订单不存在"),
    /** 重复支付 */
    DUPLICATE_PAYMENT(200916, "重复支付"),
    /** 退款失败 */
    REFUND_FAILED(200917, "退款失败"),

    // ========== OAuth 20095x ==========
    /** 不支持的OAuth平台 */
    OAUTH_PLATFORM_NOT_SUPPORTED(200950, "不支持的OAuth平台"),
    /** OAuth授权码已过期或无效 */
    OAUTH_CODE_INVALID(200951, "授权码已过期或无效"),
    /** 该社交账号已被其他用户绑定 */
    OAUTH_ALREADY_BOUND_OTHER(200952, "该社交账号已被其他用户绑定"),
    /** 该平台已绑定其他账号，请先解绑 */
    OAUTH_PLATFORM_ALREADY_BOUND(200953, "该平台已绑定其他账号，请先解绑"),
    /** OAuth state无效或已过期 */
    OAUTH_STATE_INVALID(200954, "授权状态无效或已过期，请重试"),
    /** 未绑定该平台 */
    OAUTH_NOT_BOUND(200955, "未绑定该平台"),
    /** OAuth配置缺失 */
    OAUTH_CONFIG_MISSING(200956, "OAuth配置缺失，请联系管理员"),
    // ========== 作业批改 20080x ==========
    /** 作业批改记录不存在 */
    HOMEWORK_CORRECTION_NOT_FOUND(200801, "作业批改记录不存在"),
    /** 图片数量超出限制 */
    HOMEWORK_IMAGE_LIMIT(200802, "图片数量需在1-9张之间"),
    /** 作业批改提交失败 */
    HOMEWORK_SUBMIT_FAILED(200803, "作业批改提交失败，请稍后重试"),
    /** 作业题目不存在 */
    HOMEWORK_QUESTION_NOT_FOUND(200804, "作业题目不存在"),

    /** 获取第三方用户信息失败 */
    OAUTH_USERINFO_FAILED(200957, "获取第三方用户信息失败"),

    // ========== 学员组 20100x ==========
    /** 学员组不存在 */
    STUDENT_GROUP_NOT_FOUND(201001, "学员组不存在"),
    /** 学员组名称已存在 */
    STUDENT_GROUP_NAME_EXISTS(201002, "学员组名称已存在"),
    /** 学员组下存在关联学员，无法删除 */
    STUDENT_GROUP_HAS_MEMBERS(201003, "该学员组下存在关联学员，无法删除"),
    /** 学员组下存在关联课程，无法删除 */
    STUDENT_GROUP_HAS_COURSES(201004, "该学员组下存在关联课程，无法删除"),

    // ========== 通知 20110x ==========
    /** 通知不存在 */
    NOTICE_NOT_FOUND(201101, "通知不存在"),

    // ========== 新闻资讯 / 分类 20120x ==========
    /** 资讯不存在 */
    NEWS_NOT_FOUND(201201, "资讯不存在"),
    /** 资讯分类不存在 */
    NEWS_CATEGORY_NOT_FOUND(201202, "资讯分类不存在"),
    /** 资讯分类名称已存在 */
    NEWS_CATEGORY_NAME_EXISTS(201203, "资讯分类名称已存在"),
    /** 资讯分类下存在资讯，无法删除 */
    NEWS_CATEGORY_HAS_NEWS(201204, "该分类下存在资讯，无法删除");


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
