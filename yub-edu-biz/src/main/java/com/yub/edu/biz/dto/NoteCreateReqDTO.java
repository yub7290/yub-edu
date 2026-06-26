package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 创建笔记请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 创建笔记请求参数
 * @Version: 1.0.0
 */
@Data
public class NoteCreateReqDTO {

    /** 课程ID */
    private Long courseId;

    /** 题目ID */
    private Long questionId;

    /** 笔记内容 */
    private String noteContent;
}
