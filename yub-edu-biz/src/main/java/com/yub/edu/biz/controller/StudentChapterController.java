package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduChapterAttachment;
import com.yub.edu.biz.entity.EduChapterVideo;
import com.yub.edu.biz.service.ChapterService;
import com.yub.edu.biz.vo.ChapterDetailRespVO;
import com.yub.edu.biz.vo.ChapterItemRespVO;
import com.yub.edu.biz.vo.ChapterListRespVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    private final ChapterService chapterService;

    /**
     * 章节详情
     */
    @GetMapping("/detail")
    public Response<ChapterDetailRespVO> detail(@RequestParam Long chId, @RequestParam Long cid) {
        EduChapter chapter = chapterService.getDetail(chId);

        List<EduChapterVideo> videoList = chapter.getVideoList() != null ? chapter.getVideoList() : Collections.emptyList();
        List<EduChapterAttachment> attachmentList = chapter.getAttachmentList() != null ? chapter.getAttachmentList() : Collections.emptyList();
        boolean inLiveTime = isInLiveTime(chapter, LocalDateTime.now());
        String mediaType = inLiveTime ? "live" : "video";
        String mediaSrc = inLiveTime || videoList.isEmpty() ? "" : videoList.get(0).getVideoUrl();

        return Response.success(ChapterDetailRespVO.builder()
                .id(chapter.getId())
                .mediaSrc(mediaSrc)
                .mediaType(mediaType)
                .article(chapter.getContentText() != null ? chapter.getContentText() : "")
                .attachList(attachmentList)
                .videoList(videoList)
                .build());
    }

    private boolean isInLiveTime(EduChapter chapter, LocalDateTime now) {
        if (chapter.getIsLive() == null || chapter.getIsLive() != 1) {
            return false;
        }
        if (chapter.getLiveStartTime() == null || chapter.getLiveDuration() == null || chapter.getLiveDuration() <= 0) {
            return false;
        }
        LocalDateTime liveEndTime = chapter.getLiveStartTime().plusMinutes(chapter.getLiveDuration());
        return !now.isBefore(chapter.getLiveStartTime()) && now.isBefore(liveEndTime);
    }

    /**
     * 章节列表（按课程）
     */
    @GetMapping("/list")
    public Response<ChapterListRespVO> list(@RequestParam Long cid) {
        List<EduChapter> chapters = chapterService.getTree(cid);
        List<ChapterItemRespVO> list = chapters.stream().map(ch ->
            ChapterItemRespVO.builder()
                    .id(ch.getId())
                    .chapterName(ch.getName())
                    .build()
        ).toList();

        return Response.success(ChapterListRespVO.builder().list(list).build());
    }
}
