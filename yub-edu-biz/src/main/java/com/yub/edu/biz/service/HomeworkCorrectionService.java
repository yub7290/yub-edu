package com.yub.edu.biz.service;

import com.yub.common.model.PageResult;
import com.yub.edu.biz.dto.HomeworkReviewDTO;
import com.yub.edu.biz.dto.HomeworkSubmitDTO;
import com.yub.edu.biz.vo.HomeworkCorrectionVO;
import com.yub.edu.biz.vo.HomeworkPageVO;

import java.util.List;
import java.util.Map;

/**
 * 作业批改服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-07
 * @Description: 作业批改管理服务
 * @Version: 1.0.0
 */
public interface HomeworkCorrectionService {

    /**
     * 学生提交作业（同步等待AI批改完成）
     *
     * @param studentId 学生ID
     * @param dto       提交参数
     * @return 批改结果详情
     */
    HomeworkCorrectionVO submit(Long studentId, HomeworkSubmitDTO dto);

    /**
     * 学生查看自己的作业列表
     *
     * @param studentId 学生ID
     * @param courseId   课程ID（可为null）
     * @param pageNum    页码
     * @param pageSize   每页条数
     * @return 分页结果
     */
    PageResult<HomeworkPageVO> listByStudent(Long studentId, Long courseId, int pageNum, int pageSize);

    /**
     * 学生查看作业详情
     *
     * @param studentId 学生ID
     * @param id         批改记录ID
     * @return 作业详情
     */
    HomeworkCorrectionVO getDetail(Long studentId, Long id);

    /**
     * 管理端分页查询
     *
     * @param queryParams 查询参数
     * @param pageNum     页码
     * @param pageSize    每页条数
     * @return 分页结果
     */
    PageResult<HomeworkPageVO> page(Map<String, Object> queryParams, int pageNum, int pageSize);

    /**
     * 管理端查看作业详情
     *
     * @param id 批改记录ID
     * @return 作业详情
     */
    HomeworkCorrectionVO getDetailForAdmin(Long id);

    /**
     * 复审题目
     *
     * @param adminId 管理员ID
     * @param dto     复审参数
     */
    void reviewQuestion(Long adminId, HomeworkReviewDTO dto);

    /**
     * 一键复查：将批改记录中「AI 判定正确」的题目批量标记为已复查
     * <p>采用 AI 判定答案与解析，不写回教师答案；教师身份受课程归属隔离
     *
     * @param operatorId   操作人ID
     * @param correctionId 批改记录ID
     * @return 已复查题数
     */
    int autoReviewCorrectQuestions(Long operatorId, Long correctionId);

    /**
     * 批量删除作业题目（物理删除，不可恢复）
     * <p>删除后自动重算批改记录的总题数、正确数与得分并写回；教师身份受课程归属隔离
     *
     * @param operatorId 操作人ID
     * @param ids        题目ID列表
     */
    void batchDeleteQuestions(Long operatorId, List<Long> ids);

    /**
     * 删除作业记录
     *
     * @param id 批改记录ID
     */
    void delete(Long id);
}
