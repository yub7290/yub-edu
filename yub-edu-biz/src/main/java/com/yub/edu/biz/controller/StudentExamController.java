package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.ExamSubmitReqDTO;
import com.yub.edu.biz.service.StudentExamService;
import com.yub.edu.biz.vo.CourseFinalExamRespVO;
import com.yub.edu.biz.vo.ExamInfoRespVO;
import com.yub.edu.biz.vo.ExamListRespVO;
import com.yub.edu.biz.vo.ExamQuestionRespVO;
import com.yub.edu.biz.vo.ExamResultRespVO;
import com.yub.edu.biz.vo.ExamStartRespVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学生端考试 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-18
 * @Description: 学生端考试接口
 * @Version: 2.0.0
 */
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentExamController {

    private final StudentExamService studentExamService;

    /**
     * 考试列表
     *
     * @param courseId 课程ID
     * @param keyword  关键词
     * @param page     页码
     * @param pageSize 每页条数
     * @return 考试列表
     */
    @GetMapping("/exam/list")
    public Response<ExamListRespVO> list(
            @RequestParam(name = "courseId", required = false) Long courseId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return Response.success(studentExamService.list(courseId, keyword, page, pageSize));
    }

    /**
     * 考试详情（含历史成绩）
     *
     * @param id 试卷ID
     * @return 考试详情
     */
    @GetMapping("/exam/info/{id}")
    public Response<ExamInfoRespVO> info(@PathVariable("id") Long id) {
        return Response.success(studentExamService.info(id));
    }

    /**
     * 获取考试题目（不含正确答案）
     *
     * @param examId 试卷ID
     * @return 题目列表
     */
    @GetMapping("/exam/questions")
    public Response<List<ExamQuestionRespVO>> questions(@RequestParam Long examId) {
        return Response.success(studentExamService.questions(examId));
    }

    /**
     * 提交考试
     *
     * @param dto 提交请求（新流程传 recordId，旧流程传 examId）
     * @return 判分结果
     */
    @Log(value = "提交考试答案", type = "CREATE")
    @PostMapping("/exam/submit")
    public Response<ExamResultRespVO> submit(@RequestBody ExamSubmitReqDTO dto) {
        return Response.success(studentExamService.submit(dto));
    }

    /**
     * 清空当前用户指定考试的历史成绩
     *
     * @param examId 试卷ID
     * @return 响应
     * @deprecated 不再维护，后续删除；请使用 startExam + submit 新流程
     */
    @Deprecated
    @Log(value = "清空考试历史成绩", type = "DELETE")
    @DeleteMapping("/exam/history/{examId}")
    public Response<Void> clearHistory(@PathVariable("examId") Long examId) {
        studentExamService.clearHistory(examId);
        return Response.success();
    }

    /**
     * 获取课程结课考试信息
     *
     * @param courseId 课程ID
     * @return 结课考试信息
     */
    @GetMapping("/course/{courseId}/final-exam")
    public Response<CourseFinalExamRespVO> getCourseFinalExam(@PathVariable("courseId") Long courseId) {
        return Response.success(studentExamService.getCourseFinalExam(courseId));
    }

    /**
     * 开始考试（创建考试记录、抽题）
     *
     * @param examId 试卷ID
     * @return 考试记录ID和题目列表
     */
    @Log(value = "开始考试", type = "CREATE")
    @PostMapping("/exam/{examId}/start")
    public Response<ExamStartRespVO> startExam(@PathVariable("examId") Long examId) {
        return Response.success(studentExamService.startExam(examId));
    }

    /**
     * 考试心跳
     *
     * @param recordId 考试记录ID
     * @return 响应
     */
    @Log(value = "考试心跳更新", type = "UPDATE")
    @PutMapping("/exam/record/{recordId}/heartbeat")
    public Response<Void> heartbeat(@PathVariable("recordId") Long recordId) {
        studentExamService.heartbeat(recordId);
        return Response.success();
    }

    /**
     * 查询考试结果
     *
     * @param recordId 考试记录ID
     * @return 考试结果
     */
    @GetMapping("/exam/record/{recordId}/result")
    public Response<ExamResultRespVO> getExamResult(@PathVariable("recordId") Long recordId) {
        return Response.success(studentExamService.getExamResult(recordId));
    }
}
