package com.yub.edu.biz.controller;

import com.yub.common.annotation.Log;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.AnswerSubmitReqDTO;
import com.yub.edu.biz.dto.ContinuePracticeReqDTO;
import com.yub.edu.biz.dto.FavoriteToggleReqDTO;
import com.yub.edu.biz.dto.NoteCreateReqDTO;
import com.yub.edu.biz.service.PracticeService;
import com.yub.edu.biz.vo.ChapterTreeNodeVO;
import com.yub.edu.biz.vo.FavoriteToggleRespVO;
import com.yub.edu.biz.vo.NoteRespVO;
import com.yub.edu.biz.vo.PracticeOverviewRespVO;
import com.yub.edu.biz.vo.PracticeQuestionRespVO;
import com.yub.edu.biz.vo.PracticeQuestionSimpleVO;
import com.yub.edu.biz.vo.PracticeSessionRespVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生端练习 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 学生端练习相关接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/student/practice")
@RequiredArgsConstructor
public class PracticeController {

    private final PracticeService practiceService;

    /**
     * 练习概览
     */
    @GetMapping("/overview")
    public Response<PracticeOverviewRespVO> overview(@RequestParam Long courseId) {
        return Response.success(practiceService.getPracticeOverview(courseId));
    }

    /**
     * 章节树
     */
    @GetMapping("/chapter-tree")
    public Response<List<ChapterTreeNodeVO>> chapterTree(@RequestParam Long courseId) {
        return Response.success(practiceService.getChapterTree(courseId));
    }

    /**
     * 继续练习
     */
    @PostMapping("/continue")
    public Response<PracticeSessionRespVO> continuePractice(@RequestBody ContinuePracticeReqDTO dto) {
        return Response.success(practiceService.continuePractice(dto.getCourseId()));
    }

    /**
     * 获取题目列表
     *
     * @param courseId     课程ID
     * @param chapterId    章节ID（可选，为空时表示整门课程）
     * @param practiceMode 练习模式（1=章节练习，2=模拟考试，3=错题重练）
     */
    @GetMapping("/questions")
    public Response<List<PracticeQuestionRespVO>> questions(
            @RequestParam Long courseId,
            @RequestParam(required = false) Long chapterId,
            @RequestParam(defaultValue = "1") Integer practiceMode) {
        return Response.success(practiceService.getQuestions(courseId, chapterId, practiceMode));
    }

    /**
     * 提交答案
     */
    @PostMapping("/answer")
    public Response<Void> submitAnswer(@RequestBody AnswerSubmitReqDTO dto) {
        practiceService.submitAnswer(dto);
        return Response.success();
    }

    /**
     * 错题列表
     */
    @GetMapping("/wrong-questions")
    public Response<List<PracticeQuestionSimpleVO>> wrongQuestions(@RequestParam Long courseId) {
        return Response.success(practiceService.getWrongQuestions(courseId));
    }

    /**
     * 收藏列表
     */
    @GetMapping("/favorites")
    public Response<List<PracticeQuestionSimpleVO>> favorites(@RequestParam Long courseId) {
        return Response.success(practiceService.getFavorites(courseId));
    }

    /**
     * 切换收藏状态
     */
    @PostMapping("/favorites/toggle")
    public Response<FavoriteToggleRespVO> toggleFavorite(@RequestBody FavoriteToggleReqDTO dto) {
        return Response.success(practiceService.toggleFavorite(dto));
    }

    /**
     * 笔记列表
     */
    @GetMapping("/notes")
    public Response<List<NoteRespVO>> notes(@RequestParam Long courseId) {
        return Response.success(practiceService.getNotes(courseId));
    }

    /**
     * 创建笔记
     */
    @Log(value = "创建笔记", type = "CREATE")
    @PostMapping("/notes")
    public Response<NoteRespVO> createNote(@RequestBody NoteCreateReqDTO dto) {
        return Response.success(practiceService.createNote(dto));
    }

    /**
     * 删除笔记
     */
    @Log(value = "删除笔记", type = "DELETE")
    @DeleteMapping("/notes/{noteId}")
    public Response<Void> deleteNote(@PathVariable Long noteId) {
        practiceService.deleteNote(noteId);
        return Response.success();
    }

    /**
     * 高频错题
     */
    @GetMapping("/high-freq-wrong")
    public Response<List<PracticeQuestionSimpleVO>> highFreqWrong(@RequestParam Long courseId) {
        return Response.success(practiceService.getHighFreqWrong(courseId));
    }
}
