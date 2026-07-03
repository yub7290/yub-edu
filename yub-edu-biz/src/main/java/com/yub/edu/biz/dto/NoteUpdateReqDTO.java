package com.yub.edu.biz.dto;

import lombok.Data;

/**
 * 更新笔记请求 DTO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-03
 * @Description: 更新笔记请求参数
 * @Version: 1.0.0
 */
@Data
public class NoteUpdateReqDTO {

    /** 笔记ID */
    private Long noteId;

    /** 笔记内容 */
    private String noteContent;
}
