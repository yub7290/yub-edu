package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.entity.EduChapter;
import com.yub.edu.biz.entity.EduChapterKnowledgePoint;
import com.yub.edu.biz.entity.EduCourse;
import com.yub.edu.biz.entity.EduKnowledgeCategory;
import com.yub.edu.biz.entity.EduKnowledgePoint;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduChapterMapper;
import com.yub.edu.biz.mapper.EduChapterKnowledgePointMapper;
import com.yub.edu.biz.mapper.EduCourseMapper;
import com.yub.edu.biz.mapper.EduKnowledgeCategoryMapper;
import com.yub.edu.biz.mapper.EduKnowledgePointMapper;
import com.yub.edu.biz.vo.CourseKnowledgeRespVO;
import com.yub.edu.biz.vo.KnowledgeDetailRespVO;
import com.yub.edu.biz.vo.KnowledgeGroupVO;
import com.yub.edu.biz.vo.KnowledgeItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 学员端知识库 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-28
 * @Description: 学员端知识库相关接口
 * @Version: 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class StudentKnowledgeController {

    private final EduKnowledgePointMapper knowledgePointMapper;
    private final EduChapterMapper eduChapterMapper;
    private final EduChapterKnowledgePointMapper chapterKnowledgePointMapper;
    private final EduCourseMapper eduCourseMapper;
    private final EduKnowledgeCategoryMapper knowledgeCategoryMapper;

    /**
     * 获取课程知识库（按章节分组 + 未分组）
     *
     * <p>分组内知识点：章节关联的全局知识点</p>
     * <p>未分组知识点：该课程专属的知识点</p>
     *
     * @param courseId 课程ID
     * @return 课程知识库
     */
    @GetMapping("/student/course/{courseId}/knowledge")
    public Response<CourseKnowledgeRespVO> getCourseKnowledge(@PathVariable Long courseId) {
        // 查询课程信息
        EduCourse course = eduCourseMapper.selectById(courseId);
        if (course == null) {
            throw new EduException(EduErrorCode.COURSE_NOT_FOUND);
        }

        // 查询该课程的章节列表
        List<EduChapter> chapters = eduChapterMapper.selectTreeByCourseId(courseId);
        List<Long> chapterIds = chapters.stream().map(EduChapter::getId).toList();

        // 批量查询每个章节关联的知识点ID
        Map<Long, Set<Long>> chapterKpIds = new HashMap<>();
        Set<Long> allGlobalKpIds = new HashSet<>();
        if (!chapterIds.isEmpty()) {
            List<EduChapterKnowledgePoint> ckpList = chapterKnowledgePointMapper.selectByChapterIds(chapterIds);
            for (EduChapterKnowledgePoint ckp : ckpList) {
                chapterKpIds.computeIfAbsent(ckp.getChapterId(), k -> new HashSet<>()).add(ckp.getKnowledgePointId());
                allGlobalKpIds.add(ckp.getKnowledgePointId());
            }
        }

        // 查询章节关联的全局知识点
        List<EduKnowledgePoint> globalPoints = allGlobalKpIds.isEmpty()
                ? new ArrayList<>()
                : knowledgePointMapper.selectBatchByIds(new ArrayList<>(allGlobalKpIds));

        // 查询该课程专属的知识点（未分组）
        List<EduKnowledgePoint> coursePoints = knowledgePointMapper.selectByCourseId(courseId, null);

        // 批量查询分类名称
        Set<Long> categoryIds = new HashSet<>();
        for (EduKnowledgePoint p : globalPoints) {
            if (p.getCategoryId() != null) {
                categoryIds.add(p.getCategoryId());
            }
        }
        for (EduKnowledgePoint p : coursePoints) {
            if (p.getCategoryId() != null) {
                categoryIds.add(p.getCategoryId());
            }
        }
        Map<Long, String> categoryNames = new HashMap<>();
        if (!categoryIds.isEmpty()) {
            List<EduKnowledgeCategory> categories = knowledgeCategoryMapper.selectBatchByIds(new ArrayList<>(categoryIds));
            for (EduKnowledgeCategory cat : categories) {
                if (cat != null) {
                    categoryNames.put(cat.getId(), cat.getName());
                }
            }
        }

        // 按章节分组（只包含有关联知识点的章节）
        List<KnowledgeGroupVO> groups = new ArrayList<>();
        for (EduChapter ch : chapters) {
            Set<Long> kpIds = chapterKpIds.getOrDefault(ch.getId(), new HashSet<>());
            if (kpIds.isEmpty()) {
                continue;
            }
            List<KnowledgeItemVO> items = globalPoints.stream()
                    .filter(p -> kpIds.contains(p.getId()))
                    .map(p -> buildKnowledgeItem(p, categoryNames))
                    .toList();
            if (items.isEmpty()) {
                continue;
            }
            KnowledgeGroupVO group = new KnowledgeGroupVO();
            group.setChapterId(ch.getId());
            group.setChapterName(ch.getName());
            group.setKnowledgeList(items);
            groups.add(group);
        }

        // 未分组：课程专属知识点
        List<KnowledgeItemVO> unassigned = coursePoints.stream()
                .map(p -> buildKnowledgeItem(p, categoryNames))
                .toList();

        CourseKnowledgeRespVO resp = new CourseKnowledgeRespVO();
        resp.setCourseId(courseId);
        resp.setCourseName(course.getName());
        resp.setGroups(groups);
        resp.setUnassigned(unassigned);
        return Response.success(resp);
    }

    /**
     * 获取知识点详情
     *
     * @param id 知识点ID
     * @return 知识点详情
     */
    @GetMapping("/student/knowledge/{id}")
    public Response<KnowledgeDetailRespVO> getKnowledgeDetail(@PathVariable Long id) {
        EduKnowledgePoint point = knowledgePointMapper.selectById(id);
        if (point == null) {
            throw new EduException(EduErrorCode.KNOWLEDGE_POINT_NOT_FOUND);
        }

        String categoryName = null;
        if (point.getCategoryId() != null) {
            EduKnowledgeCategory cat = knowledgeCategoryMapper.selectById(point.getCategoryId());
            if (cat != null) {
                categoryName = cat.getName();
            }
        }

        KnowledgeDetailRespVO vo = new KnowledgeDetailRespVO();
        vo.setId(point.getId());
        vo.setTitle(point.getTitle());
        vo.setContent(point.getContent());
        vo.setCategoryId(point.getCategoryId());
        vo.setCategoryName(categoryName);
        return Response.success(vo);
    }

    /**
     * 构造知识点项 VO
     */
    private KnowledgeItemVO buildKnowledgeItem(EduKnowledgePoint point, Map<Long, String> categoryNames) {
        KnowledgeItemVO item = new KnowledgeItemVO();
        item.setId(point.getId());
        item.setTitle(point.getTitle());
        item.setCategoryId(point.getCategoryId());
        item.setCategoryName(categoryNames.get(point.getCategoryId()));
        return item;
    }
}
