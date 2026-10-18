package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterItemRespVO {
    private Long id;
    private String chapterName;
    /** 子章节；空列表表示可直接学习的叶子章节。 */
    private List<ChapterItemRespVO> children;
}
