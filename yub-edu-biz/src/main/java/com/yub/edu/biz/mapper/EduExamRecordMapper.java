package com.yub.edu.biz.mapper;

import com.yub.edu.biz.dto.CourseExamStatsResult;
import com.yub.edu.biz.entity.EduExamRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试记录 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 考试记录数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduExamRecordMapper {

    /**
     * 插入考试记录
     *
     * @param record 考试记录
     * @return 影响行数
     */
    int insert(EduExamRecord record);

    /**
     * 根据ID查询考试记录
     *
     * @param id 记录ID
     * @return 考试记录
     */
    EduExamRecord selectById(@Param("id") Long id);

    /**
     * 根据用户ID和试卷ID查询历史考试记录（已提交的）
     *
     * @param userId 用户ID
     * @param examId 试卷ID
     * @return 历史记录列表
     */
    List<EduExamRecord> selectByUserAndExam(@Param("userId") Long userId, @Param("examId") Long examId);

    /**
     * 查询用户指定考试进行中的记录
     *
     * @param userId 用户ID
     * @param examId 试卷ID
     * @return 进行中的记录
     */
    EduExamRecord selectInProgress(@Param("userId") Long userId, @Param("examId") Long examId);

    /**
     * 更新最后心跳时间
     *
     * @param id            记录ID
     * @param heartbeatTime 心跳时间
     * @return 影响行数
     */
    int updateHeartbeat(@Param("id") Long id, @Param("heartbeatTime") LocalDateTime heartbeatTime);

    /**
     * 更新记录状态（提交/超时交卷）
     *
     * @param id         记录ID
     * @param status     状态
     * @param score      得分
     * @param isPass     是否及格
     * @param duration   答题用时
     * @param submitTime 提交时间
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status,
                     @Param("score") Integer score, @Param("isPass") Integer isPass,
                     @Param("duration") Integer duration, @Param("submitTime") LocalDateTime submitTime);

    /**
     * 查询所有进行中且超时的记录（用于定时任务自动交卷）
     *
     * @param deadline 心跳时间截止
     * @return 超时记录列表
     */
    List<EduExamRecord> selectTimeoutRecords(@Param("deadline") LocalDateTime deadline);

    /**
     * 查询所有进行中且超过考试时长的记录（含 Exam JOIN，用于定时任务自动交卷）
     *
     * @return 超时记录列表（含 exam duration/totalScore/passScore 信息）
     */
    List<EduExamRecord> selectTimeoutRecordsByDuration();

    /**
     * 查询用户某考试的历史最高分
     *
     * @param userId 用户ID
     * @param examId 试卷ID
     * @return 最高分
     */
    Integer selectMaxScore(@Param("userId") Long userId, @Param("examId") Long examId);

    /**
     * 查询用户某考试的已提交记录数
     *
     * @param userId 用户ID
     * @param examId 试卷ID
     * @return 记录数
     */
    int selectSubmittedCount(@Param("userId") Long userId, @Param("examId") Long examId);

    /**
     * 逻辑删除当前用户指定试卷的所有历史记录
     *
     * @param userId 用户ID
     * @param examId 试卷ID
     * @return 影响行数
     */
    int deleteByUserAndExam(@Param("userId") Long userId, @Param("examId") Long examId);

    /**
     * 查询用户在某课程下的考试统计
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 统计结果（avgScore, maxScore, totalCount, passCount）
     */
    CourseExamStatsResult selectCourseExamStats(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 查询用户在某课程下的所有考试记录列表（含 exam 名称）
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 考试记录列表
     */
    List<EduExamRecord> selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
