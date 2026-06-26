package com.yub.edu.biz.service;

import com.yub.edu.biz.dto.AnswerSubmitReqDTO;
import com.yub.edu.biz.dto.FavoriteToggleReqDTO;
import com.yub.edu.biz.dto.NoteCreateReqDTO;
import com.yub.edu.biz.vo.ChapterTreeNodeVO;
import com.yub.edu.biz.vo.FavoriteToggleRespVO;
import com.yub.edu.biz.vo.NoteRespVO;
import com.yub.edu.biz.vo.PracticeOverviewRespVO;
import com.yub.edu.biz.vo.PracticeQuestionRespVO;
import com.yub.edu.biz.vo.PracticeQuestionSimpleVO;
import com.yub.edu.biz.vo.PracticeSessionRespVO;

import java.util.List;

/**
 * 练习服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 课程练习相关服务（章节/错题/收藏/高频/继续练习）
 * @Version: 1.0.0
 */
public interface PracticeService {

    /**
     * 获取练习概览
     *
     * @param courseId 课程ID
     * @return 练习概览（总题量/已练习/通过率/章节进度）
     */
    PracticeOverviewRespVO getPracticeOverview(Long courseId);

    /**
     * 获取章节树
     *
     * @param courseId 课程ID
     * @return 章节树节点列表
     */
    List<ChapterTreeNodeVO> getChapterTree(Long courseId);

    /**
     * 获取单道练习题
     *
     * @param courseId     课程ID
     * @param chapterId    章节ID（章节练习时必传）
     * @param practiceMode 练习模式 1章节 2错题 3收藏 4高频 5继续
     * @param index        题目索引（从0开始）
     * @return 题目详情（不含答案）
     */
    PracticeQuestionRespVO getQuestion(Long courseId, Long chapterId, Integer practiceMode, Integer index);

    /**
     * 获取练习题列表
     *
     * @param courseId     课程ID
     * @param chapterId    章节ID
     * @param practiceMode 练习模式 1章节 2错题 3收藏 4高频 5继续
     * @return 题目列表
     */
    List<PracticeQuestionRespVO> getQuestions(Long courseId, Long chapterId, Integer practiceMode);

    /**
     * 提交答案
     *
     * @param dto 提交答案参数
     */
    void submitAnswer(AnswerSubmitReqDTO dto);

    /**
     * 获取错题列表
     *
     * @param courseId 课程ID
     * @return 错题目列表
     */
    List<PracticeQuestionSimpleVO> getWrongQuestions(Long courseId);

    /**
     * 获取收藏列表
     *
     * @param courseId 课程ID
     * @return 收藏题目列表
     */
    List<PracticeQuestionSimpleVO> getFavorites(Long courseId);

    /**
     * 切换收藏状态
     *
     * @param dto 切换收藏参数
     * @return 切换后的收藏状态
     */
    FavoriteToggleRespVO toggleFavorite(FavoriteToggleReqDTO dto);

    /**
     * 获取笔记列表
     *
     * @param courseId 课程ID
     * @return 笔记列表
     */
    List<NoteRespVO> getNotes(Long courseId);

    /**
     * 创建笔记
     *
     * @param dto 创建笔记参数
     * @return 创建的笔记
     */
    NoteRespVO createNote(NoteCreateReqDTO dto);

    /**
     * 删除笔记
     *
     * @param noteId 笔记ID
     */
    void deleteNote(Long noteId);

    /**
     * 获取高频错题
     *
     * @param courseId 课程ID
     * @return 高频错题目列表
     */
    List<PracticeQuestionSimpleVO> getHighFreqWrong(Long courseId);

    /**
     * 继续练习（返回会话进度）
     *
     * @param courseId 课程ID
     * @return 练习会话进度
     */
    PracticeSessionRespVO continuePractice(Long courseId);
}
