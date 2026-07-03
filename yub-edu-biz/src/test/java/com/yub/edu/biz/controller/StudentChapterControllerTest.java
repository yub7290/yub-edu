package com.yub.edu.biz.controller;

import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduChapterVideo;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.mapper.EduChapterVideoMapper;
import com.yub.edu.biz.vo.ChapterDetailRespVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 学生端章节详情 Controller 测试
 * @Version: 1.0.0
 */
class StudentChapterControllerTest {

    /** 章节 Mapper */
    private EduChapterMapper eduChapterMapper;

    /** 章节视频 Mapper */
    private EduChapterVideoMapper eduChapterVideoMapper;

    /** 被测 Controller */
    private StudentChapterController controller;

    @BeforeEach
    void setUp() {
        eduChapterMapper = mock(EduChapterMapper.class);
        eduChapterVideoMapper = mock(EduChapterVideoMapper.class);
        controller = new StudentChapterController(eduChapterMapper, eduChapterVideoMapper);
    }

    @Test
    void detailReturnsVideoMediaTypeWhenLiveChapterIsOutsideLiveWindow() {
        EduChapter chapter = liveChapter(LocalDateTime.now().minusHours(2), 30);
        EduChapterVideo video = video();
        when(eduChapterMapper.selectById(2L)).thenReturn(chapter);
        when(eduChapterVideoMapper.selectByChapterId(2L)).thenReturn(List.of(video));

        ChapterDetailRespVO detail = controller.detail(2L, 2L).getData();

        assertThat(detail.getMediaType()).isEqualTo("video");
        assertThat(detail.getMediaSrc()).isEqualTo("https://cdn.example.com/geometry.mp4");
        assertThat(detail.getVideoList()).hasSize(1);
    }

    @Test
    void detailReturnsLiveMediaTypeWhenCurrentTimeIsInsideLiveWindow() {
        EduChapter chapter = liveChapter(LocalDateTime.now().minusMinutes(10), 60);
        EduChapterVideo video = video();
        when(eduChapterMapper.selectById(2L)).thenReturn(chapter);
        when(eduChapterVideoMapper.selectByChapterId(2L)).thenReturn(List.of(video));

        ChapterDetailRespVO detail = controller.detail(2L, 2L).getData();

        assertThat(detail.getMediaType()).isEqualTo("live");
        assertThat(detail.getMediaSrc()).isEmpty();
        assertThat(detail.getVideoList()).hasSize(1);
    }

    private EduChapter liveChapter(LocalDateTime liveStartTime, Integer liveDuration) {
        EduChapter chapter = new EduChapter();
        chapter.setId(2L);
        chapter.setIsLive(1);
        chapter.setLiveStartTime(liveStartTime);
        chapter.setLiveDuration(liveDuration);
        return chapter;
    }

    private EduChapterVideo video() {
        EduChapterVideo video = new EduChapterVideo();
        video.setVideoName("几何图形.mp4");
        video.setVideoUrl("https://cdn.example.com/geometry.mp4");
        return video;
    }
}
