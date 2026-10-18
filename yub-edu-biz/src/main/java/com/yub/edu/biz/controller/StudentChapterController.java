package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduChapterAttachment;
import com.yub.edu.biz.entity.EduChapterVideo;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.service.ChapterService;
import com.yub.edu.biz.service.CourseAccessService;
import com.yub.edu.biz.vo.ChapterDetailRespVO;
import com.yub.edu.biz.vo.ChapterItemRespVO;
import com.yub.edu.biz.vo.ChapterListRespVO;
import com.yub.framework.security.SecurityUtils;
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
    private final CourseAccessService courseAccessService;

    /**
     * 章节详情
     */
    @GetMapping("/detail")
    public Response<ChapterDetailRespVO> detail(@RequestParam Long chId, @RequestParam Long cid) {
        // 学习权限校验：仅免费课 / 已购买 / 所属组已绑定课程可访问，避免视频/附件 URL 越权泄露
        if (!courseAccessService.canAccess(SecurityUtils.getCurrentUserId(), cid)) {
            throw new EduException(EduErrorCode.COURSE_NO_ACCESS);
        }
        EduChapter chapter = chapterService.getDetail(chId);
        // cid 不能只用于权限判断；必须同时校验章节归属，避免拿有权限的课程ID读取其他课程章节。
        if (!cid.equals(chapter.getCourseId())) {
            throw new EduException(EduErrorCode.COURSE_NO_ACCESS);
        }

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
        // 学习权限校验：章节大纲同样受课程访问权限约束
        if (!courseAccessService.canAccess(SecurityUtils.getCurrentUserId(), cid)) {
            throw new EduException(EduErrorCode.COURSE_NO_ACCESS);
        }
        List<EduChapter> chapters = chapterService.getTree(cid);
        // 保留完整树结构。旧实现只映射根节点，丢弃 children，学习页只能显示一层且容易打开无视频的父章节。
        List<ChapterItemRespVO> list = chapters.stream().map(this::toChapterItem).toList();

        return Response.success(ChapterListRespVO.builder().list(list).build());
    }

    private ChapterItemRespVO toChapterItem(EduChapter chapter) {
        List<ChapterItemRespVO> children = chapter.getChildren() == null
                ? Collections.emptyList()
                : chapter.getChildren().stream().map(this::toChapterItem).toList();
        return ChapterItemRespVO.builder()
                .id(chapter.getId())
                .chapterName(chapter.getName())
                .children(children)
                .build();
    }
}
