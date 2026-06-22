package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.mapper.StudyRecordMapper;
import com.yub.edu.biz.entity.EduStudyRecord;
import com.yub.edu.biz.vo.StudyStatsRespVO;
import com.yub.framework.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 学生端学习记录 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端学习记录接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/student/study")
@RequiredArgsConstructor
public class StudyRecordController {

    private final StudyRecordMapper studyRecordMapper;
    private final JwtProvider jwtProvider;

    /**
     * 保存学习记录
     */
    @PostMapping("/saveRecord")
    public Response<Void> saveRecord(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long studentId = getStudentId(request);
        Long courseId = Long.valueOf(params.get("courseId").toString());
        Long chapterId = Long.valueOf(params.get("chapterId").toString());
        Integer playSecond = Integer.valueOf(params.get("playSecond").toString());
        Integer totalStudySecond = Integer.valueOf(params.get("totalStudySecond").toString());

        EduStudyRecord exist = studyRecordMapper.selectByStudentAndChapter(studentId, courseId, chapterId);
        if (exist != null) {
            // 更新：取较大的播放进度和学习时长
            exist.setPlaySecond(Math.max(exist.getPlaySecond(), playSecond));
            exist.setTotalStudySecond(Math.max(exist.getTotalStudySecond(), totalStudySecond));
            studyRecordMapper.updateById(exist);
        } else {
            // 新增
            EduStudyRecord record = new EduStudyRecord();
            record.setStudentId(studentId);
            record.setCourseId(courseId);
            record.setChapterId(chapterId);
            record.setPlaySecond(playSecond);
            record.setTotalStudySecond(totalStudySecond);
            studyRecordMapper.insert(record);
        }

        return Response.success();
    }

    /**
     * 批量上传学习记录
     */
    @PostMapping("/batchUpload")
    public Response<Void> batchUpload(@RequestBody List<Map<String, Object>> records, HttpServletRequest request) {
        for (Map<String, Object> params : records) {
            saveRecord(params, request);
        }
        return Response.success();
    }

    /**
     * 学习统计
     */
    @GetMapping("/stats")
    public Response<StudyStatsRespVO> stats(HttpServletRequest request) {
        Long studentId = getStudentId(request);

        Integer totalStudySecond = studyRecordMapper.selectTotalStudySecond(studentId);
        Integer courseCount = studyRecordMapper.selectCourseCount(studentId);

        return Response.success(StudyStatsRespVO.builder()
                .courseCount(courseCount != null ? courseCount : 0)
                .studyHours(totalStudySecond != null ? totalStudySecond / 3600 : 0)
                .certCount(0)
                .build());
    }

    private Long getStudentId(HttpServletRequest request) {
        String token = jwtProvider.getToken(request);
        return Long.valueOf(jwtProvider.getUserId(token));
    }
}
