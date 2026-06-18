package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduExam;
import com.yub.edu.biz.mapper.EduExamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 学生端考试 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端考试接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/student/exam")
@RequiredArgsConstructor
public class StudentExamController {

    private final EduExamMapper eduExamMapper;

    /**
     * 考试列表
     */
    @GetMapping("/list")
    public Response<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> data = new HashMap<>();
        // TODO: 实现考试列表查询，当前返回空列表
        data.put("list", new ArrayList<>());
        return Response.success(data);
    }

    /**
     * 考试详情
     */
    @GetMapping("/info/{id}")
    public Response<Map<String, Object>> info(@PathVariable Long id) {
        EduExam exam = eduExamMapper.selectById(id);
        if (exam == null) {
            return Response.success(null);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", exam.getId());
        data.put("name", exam.getTitle());
        data.put("duration", exam.getDuration());
        data.put("totalScore", exam.getTotalScore());
        data.put("startTime", "");
        data.put("endTime", "");
        return Response.success(data);
    }

    /**
     * 上报异常行为
     */
    @PostMapping("/abnormal/add")
    public Response<Void> abnormalAdd(@RequestBody Map<String, Object> params) {
        // TODO: 保存异常行为记录
        return Response.success();
    }

    /**
     * 提交考试结果
     */
    @PostMapping("/result/submit")
    public Response<Void> resultSubmit(@RequestBody Map<String, Object> params) {
        // TODO: 保存考试结果
        return Response.success();
    }
}
