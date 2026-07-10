package com.yub.edu.biz.service;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.edu.api.dto.app.MyCoursePageQueryDTO;
import com.yub.edu.api.vo.app.MyCourseVO;
import com.yub.edu.biz.dto.CourseCreateReqDTO;
import com.yub.edu.biz.dto.CourseQueryDTO;
import com.yub.edu.biz.dto.CourseUpdateReqDTO;
import com.yub.edu.biz.vo.CourseDetailRespVO;
import com.yub.edu.biz.vo.CourseOverviewRespVO;
import com.yub.edu.biz.vo.CoursePageRespVO;
import com.yub.edu.biz.entity.EduCourse;

import java.util.List;

/**
 * 课程服务接口
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 课程管理服务
 * @Version: 1.0.0
 */
public interface EduCourseService {

    /**
     * 分页查询课程
     *
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<CoursePageRespVO> page(PageQuery<CourseQueryDTO> pageQuery);

    /**
     * 获取课程详情
     *
     * @param id 课程ID
     * @return 课程详情
     */
    CourseDetailRespVO getDetail(Long id);

    /**
     * 获取课程综述
     *
     * @param id 课程ID
     * @return 课程综述
     */
    CourseOverviewRespVO getOverview(Long id);

    /**
     * 新增课程
     *
     * @param dto 新增参数
     * @return 课程ID
     */
    Long create(CourseCreateReqDTO dto);

    /**
     * 编辑课程
     *
     * @param dto 编辑参数
     */
    void update(CourseUpdateReqDTO dto);

    /**
     * 删除课程
     *
     * @param id 课程ID
     */
    void delete(Long id);

    /**
     * 切换课程状态
     *
     * @param id     课程ID
     * @param status 状态
     */
    void changeStatus(Long id, Integer status);

    /**
     * 设置推荐课程
     *
     * @param id          课程ID
     * @param recommended 是否推荐
     */
    void setRecommended(Long id, Integer recommended);

    /**
     * 查询推荐课程列表
     *
     * @return 推荐课程列表
     */
    List<EduCourse> listRecommended();

    /**
     * 根据ID查询课程
     *
     * @param id 课程ID
     * @return 课程
     */
    EduCourse selectById(Long id);

    /**
     * 学生端查询课程列表
     *
     * @param cateId  分类ID（可为null）
     * @param tabType tab类型（0=推荐 1=热门 2=直播 3=免费 4=会员）
     * @param keyword 搜索关键词（可为null）
     * @return 课程列表
     */
    List<EduCourse> studentList(Long cateId, Integer tabType, String keyword);

    /**
     * 我的课程
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResult<MyCourseVO> myCourse(PageQuery<MyCoursePageQueryDTO> pageQuery);
}
