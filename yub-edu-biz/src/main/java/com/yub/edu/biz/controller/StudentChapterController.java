package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.mapper.EduChapterMapper;
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
    public Response<Map<String, Object>> detail(@RequestParam Long chId, @RequestParam Long cid) {
        EduChapter chapter = eduChapterMapper.selectById(chId);
        if (chapter == null) {
            return Response.success(null);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", chapter.getId());
        data.put("mediaSrc", "");
        data.put("mediaType", chapter.getIsLive() != null && chapter.getIsLive() == 1 ? "live" : "video");
        data.put("article", chapter.getContentText() != null ? chapter.getContentText() : "");
        data.put("attachList", new ArrayList<>());
        return Response.success(data);
    }

    /**
     * 章节列表（按课程）
     */
    @GetMapping("/list")
    public Response<Map<String, Object>> list(@RequestParam Long cid) {
        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(cid);
        List<Map<String, Object>> list = new ArrayList<>();
        for (EduChapter ch : chapters) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", ch.getId());
            item.put("chapterName", ch.getName());
            list.add(item);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        return Response.success(data);
    }
}
