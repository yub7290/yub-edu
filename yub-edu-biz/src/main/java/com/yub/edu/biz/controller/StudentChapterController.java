package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.vo.ChapterDetailRespVO;
import com.yub.edu.biz.vo.ChapterItemRespVO;
import com.yub.edu.biz.vo.ChapterListRespVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 学生端章节 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端章节接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/student/chapter")
@RequiredArgsConstructor
public class StudentChapterController {

    private final EduChapterMapper eduChapterMapper;

    /**
     * 章节详情
     */
    @GetMapping("/detail")
    public Response<ChapterDetailRespVO> detail(@RequestParam Long chId, @RequestParam Long cid) {
        EduChapter chapter = eduChapterMapper.selectById(chId);
        if (chapter == null) {
            return Response.success(null);
        }

        return Response.success(ChapterDetailRespVO.builder()
                .id(chapter.getId())
                .mediaSrc("")
                .mediaType(chapter.getIsLive() != null && chapter.getIsLive() == 1 ? "live" : "video")
                .article(chapter.getContentText() != null ? chapter.getContentText() : "")
                .attachList(new ArrayList<>())
                .build());
    }

    /**
     * 章节列表（按课程）
     */
    @GetMapping("/list")
    public Response<ChapterListRespVO> list(@RequestParam Long cid) {
        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(cid);
        List<ChapterItemRespVO> list = chapters.stream().map(ch ->
            ChapterItemRespVO.builder()
                    .id(ch.getId())
                    .chapterName(ch.getName())
                    .build()
        ).toList();

        return Response.success(ChapterListRespVO.builder().list(list).build());
    }
}
