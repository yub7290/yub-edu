package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 笔记列表响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 笔记列表项
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteRespVO {

    /** 笔记ID */
    private Long id;

    /** 题目ID */
    private Long questionId;

    /** 课程ID */
    private Long courseId;

    /** 笔记内容 */
    private String noteContent;

    /** 题目类型 */
    private Integer questionType;

    /** 题目内容 */
    private String questionContent;

    /** 创建时间 */
    private LocalDateTime createTime;
}
