package com.yub.edu.biz.vo;

import com.yub.edu.biz.entity.EduChapterVideo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDetailRespVO {
    private Long id;
    private String mediaSrc;
    private String mediaType;
    private String article;
    private List<?> attachList;
    private List<EduChapterVideo> videoList;
}
