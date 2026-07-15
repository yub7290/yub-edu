package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.dto.StudentGrowthPlanDTO;
import com.yub.edu.biz.dto.StudentGrowthStatsDTO;
import com.yub.edu.biz.entity.EduChapterKnowledgePoint;
import com.yub.edu.biz.entity.EduKnowledgeCategory;
import com.yub.edu.biz.entity.EduKnowledgePoint;
import com.yub.edu.biz.entity.EduKnowledgeRelation;
import com.yub.edu.biz.entity.EduStudent;
import com.yub.edu.biz.mapper.EduKnowledgePointMapper;
import com.yub.edu.biz.mapper.EduKnowledgeRelationMapper;
import com.yub.edu.biz.mapper.EduStudentGroupMapper;
import com.yub.edu.biz.mapper.EduStudentMapper;
import com.yub.edu.biz.mapper.StudentGrowthMapper;
import com.yub.edu.biz.mapper.StudyRecordMapper;
import com.yub.edu.biz.service.ChapterService;
import com.yub.edu.biz.service.EduKnowledgeCategoryService;
import com.yub.edu.biz.service.EduKnowledgePointService;
import com.yub.edu.biz.service.StudentGrowthService;
import com.yub.edu.biz.vo.StudentGrowthVO;
import com.yub.edu.biz.vo.StudentGroupDetailRespVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 学生成长档案服务实现
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 基于学习、练习、考试真实记录计算成长档案
 * @Version: 1.0.0
 */
@Service
@RequiredArgsConstructor
public class StudentGrowthServiceImpl implements StudentGrowthService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StudentGrowthServiceImpl.class);

    private static final int WEEK_COUNT = 4;
    private static final DateTimeFormatter DATE_LABEL = DateTimeFormatter.ofPattern("MM/dd");

    private final StudentGrowthMapper studentGrowthMapper;
    private final EduStudentMapper eduStudentMapper;
    private final EduStudentGroupMapper eduStudentGroupMapper;
    private final EduKnowledgeCategoryService knowledgeCategoryService;
    private final EduKnowledgePointService knowledgePointService;
    private final EduKnowledgePointMapper knowledgePointMapper;
    private final EduKnowledgeRelationMapper knowledgeRelationMapper;
    private final StudyRecordMapper studyRecordMapper;
    private final ChapterService chapterService;

    @Override
    public StudentGrowthVO.HomeData getHomeData(Long studentId) {
        EduStudent student = eduStudentMapper.selectById(studentId);
        StudentGrowthVO.HomeData data = new StudentGrowthVO.HomeData();
        data.setStudentName(student != null && student.getName() != null ? student.getName() : "");
        data.setTodayStats(buildTodayStats(studentId));
        data.setWeekDone(buildCurrentWeekDone(studentId));
        data.setWeekTotal(buildWeekTarget(data.getWeekDone()));
        data.setGrowthValue(calculateGrowthValue(studentId));
        data.setTodayTasks(buildTodayTasks(studentId));
        data.setBadgeList(buildBadges(data));
        data.setHighlightTip(buildHighlightTip(studentId));
        return data;
    }

    @Override
    public StudentGrowthVO.Overview getOverview(Long studentId) {
        EduStudent student = eduStudentMapper.selectById(studentId);
        StudentGrowthVO.Overview overview = new StudentGrowthVO.Overview();
        overview.setStudentId(studentId);
        overview.setStudentName(student != null && student.getName() != null ? student.getName() : "");
        overview.setAvatarUrl(student != null && student.getAvatarUrl() != null ? student.getAvatarUrl() : "");
        overview.setTotalStudyDays(defaultInt(studentGrowthMapper.selectStudyDays(studentId)));
        overview.setTotalStudyMinutes(defaultInt(studentGrowthMapper.selectTotalStudyMinutes(studentId)));
        overview.setCompletedCourseCount(defaultInt(studentGrowthMapper.selectCompletedCourseCount(studentId)));
        overview.setAverageExamScore(defaultInt(studentGrowthMapper.selectAverageExamScore(studentId)));
        overview.setAbilityDimensions(buildOverviewDimensions(studentId));
        overview.setRecentStudyRecords(buildRecentStudyRecords(studentId));
        return overview;
    }

    @Override
    public StudentGrowthVO.WeeklyPlan getWeeklyPlan(Long studentId, String weekStartDate) {
        StudentGrowthVO.WeekPlanDetail detail = getWeekPlanDetail(studentId, 0);
        StudentGrowthVO.WeeklyPlan plan = new StudentGrowthVO.WeeklyPlan();
        plan.setId(0L);
        plan.setWeekStartDate(weekStartDate != null ? weekStartDate : currentMonday().toString());
        plan.setWeekEndDate(currentMonday().plusDays(6).toString());
        plan.setTasks(detail.getDailyPlanList().stream()
                .flatMap(day -> day.getTasks().stream())
                .map(this::convertWeeklyTask)
                .toList());
        return plan;
    }

    @Override
    public StudentGrowthVO.KnowledgeGraphNode getKnowledgeGraph(Long studentId) {
        StudentGrowthVO.KnowledgeGraphNode root = new StudentGrowthVO.KnowledgeGraphNode();
        root.setId(0L);
        root.setName("全部知识点");
        root.setMasteryPercent(0);
        root.setChildren(studentGrowthMapper.selectKnowledgeMastery(studentId).stream().map(item -> {
            StudentGrowthVO.KnowledgeGraphNode node = new StudentGrowthVO.KnowledgeGraphNode();
            node.setId(item.getId());
            node.setName(item.getName());
            node.setMasteryPercent(defaultInt(item.getMastery()));
            node.setParentId(0L);
            return node;
        }).toList());
        return root;
    }

    @Override
    public List<StudentGrowthVO.WeekReport> getWeekReports(Long studentId) {
        LocalDate monday = currentMonday();
        LocalDate fromDate = monday.minusWeeks(WEEK_COUNT - 1L);
        List<StudentGrowthStatsDTO.WeekSummary> summaries =
                studentGrowthMapper.selectWeekSummaries(studentId, fromDate, WEEK_COUNT);
        List<StudentGrowthVO.WeekReport> reports = new ArrayList<>();
        for (int i = 0; i < summaries.size(); i++) {
            StudentGrowthStatsDTO.WeekSummary current = summaries.get(i);
            StudentGrowthStatsDTO.WeekSummary previous = i + 1 < summaries.size() ? summaries.get(i + 1) : null;
            reports.add(convertWeekReport(current, previous, studentId));
        }
        return reports;
    }

    @Override
    public List<StudentGrowthVO.SubjectItem> getSubjects(Long studentId) {
        List<StudentGrowthStatsDTO.KnowledgeMastery> masteryList = studentGrowthMapper.selectKnowledgeMastery(studentId);
        return masteryList.stream()
                .filter(item -> item.getCourseId() != null)
                .collect(Collectors.toMap(
                        item -> String.valueOf(item.getCourseId()),
                        item -> buildSubjectItem(item.getCourseId(), item.getCourseName()),
                        (oldVal, newVal) -> oldVal))
                .values().stream()
                .sorted(Comparator.comparing(StudentGrowthVO.SubjectItem::getName))
                .toList();
    }

    @Override
    public StudentGrowthVO.SubjectGraph getSubjectGraph(Long studentId, String subject) {
        List<StudentGrowthStatsDTO.KnowledgeMastery> source = studentGrowthMapper.selectKnowledgeMastery(studentId);
        List<StudentGrowthStatsDTO.KnowledgeMastery> filtered = filterBySubject(source, subject);
        StudentGrowthVO.SubjectGraph graph = new StudentGrowthVO.SubjectGraph();
        if (filtered.isEmpty()) {
            return graph;
        }
        Map<Long, List<StudentGrowthPlanDTO.QuestionTypeMastery>> typeMap = buildQuestionTypeMap(studentId, filtered);
        List<StudentGrowthVO.KnowledgeNode> nodes = filtered.stream()
                .sorted(Comparator.comparing(StudentGrowthStatsDTO.KnowledgeMastery::getMastery))
                .map(item -> convertKnowledgeNode(item, typeMap.get(item.getId())))
                .toList();
        graph.setCoreNode(nodes.get(0));
        graph.setPreNodes(nodes.stream().filter(n -> n.getMastery() >= 80).limit(4).toList());
        graph.setNextNodes(nodes.stream().filter(n -> n.getMastery() < 80).skip(1).limit(4).toList());
        graph.setGoodChain(joinNames(graph.getPreNodes()));
        graph.setWeakChain(joinNames(nodes.stream().filter(n -> n.getMastery() < 80).limit(4).toList()));
        return graph;
    }

    @Override
    public List<StudentGrowthVO.SubjectItem> getKnowledgeCategories(Long studentId) {
        List<EduKnowledgeCategory> allCategories = knowledgeCategoryService.selectTree();
        Set<Long> studentCourseKnowledgeIds = getStudentCourseKnowledgeIds(studentId);
        
        Map<Long, EduKnowledgePoint> knowledgeMap = knowledgePointService.selectBatchByIds(new ArrayList<>(studentCourseKnowledgeIds))
                .stream()
                .collect(Collectors.toMap(EduKnowledgePoint::getId, p -> p));
        
        Set<Long> studentCategoryIds = new HashSet<>();
        for (EduKnowledgePoint point : knowledgeMap.values()) {
            if (point.getCategoryId() != null) {
                studentCategoryIds.add(point.getCategoryId());
            }
        }
        
        Map<Long, EduKnowledgeCategory> categoryMap = new HashMap<>();
        fillCategoryMap(allCategories, categoryMap);
        
        Set<Long> topLevelCategoryIds = new HashSet<>();
        for (Long catId : studentCategoryIds) {
            Long topId = findTopLevelCategory(catId, categoryMap);
            if (topId != null) {
                topLevelCategoryIds.add(topId);
            }
        }
        
        List<StudentGrowthVO.SubjectItem> result = new ArrayList<>();
        for (EduKnowledgeCategory cat : allCategories) {
            if ((cat.getParentId() == null || cat.getParentId() == 0L) && topLevelCategoryIds.contains(cat.getId())) {
                StudentGrowthVO.SubjectItem item = new StudentGrowthVO.SubjectItem();
                item.setKey(String.valueOf(cat.getId()));
                item.setName(cat.getName());
                item.setColor(getCategoryColor(cat.getId()));
                result.add(item);
            }
        }
        return result;
    }

    private Long findTopLevelCategory(Long catId, Map<Long, EduKnowledgeCategory> categoryMap) {
        EduKnowledgeCategory cat = categoryMap.get(catId);
        if (cat == null) {
            return null;
        }
        if (cat.getParentId() == null || cat.getParentId() == 0L) {
            return cat.getId();
        }
        return findTopLevelCategory(cat.getParentId(), categoryMap);
    }

    @Override
    public StudentGrowthVO.SubjectGraph getCategoryGraph(Long studentId, String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) {
            return new StudentGrowthVO.SubjectGraph();
        }
        Long catId;
        try {
            catId = Long.parseLong(categoryId.trim());
        } catch (NumberFormatException e) {
            return new StudentGrowthVO.SubjectGraph();
        }
        List<EduKnowledgeCategory> allCategories = knowledgeCategoryService.selectTree();
        Set<Long> categoryIds = getCategoryAndChildIds(catId, allCategories);

        Set<Long> studentKnowledgeIds = getStudentCourseKnowledgeIds(studentId);

        Map<Long, EduKnowledgePoint> knowledgeMap = knowledgePointService.selectBatchByIds(new ArrayList<>(studentKnowledgeIds))
                .stream()
                .collect(Collectors.toMap(EduKnowledgePoint::getId, p -> p));

        List<StudentGrowthStatsDTO.KnowledgeMastery> masteryList = studentGrowthMapper.selectKnowledgeMastery(studentId);
        Map<Long, Integer> masteryMap = masteryList.stream()
                .collect(Collectors.toMap(StudentGrowthStatsDTO.KnowledgeMastery::getId, StudentGrowthStatsDTO.KnowledgeMastery::getMastery));

        List<StudentGrowthStatsDTO.KnowledgeMastery> filtered = knowledgeMap.values().stream()
                .filter(point -> point.getCategoryId() != null && categoryIds.contains(point.getCategoryId()))
                .map(point -> {
                    StudentGrowthStatsDTO.KnowledgeMastery m = new StudentGrowthStatsDTO.KnowledgeMastery();
                    m.setId(point.getId());
                    m.setName(point.getTitle());
                    m.setMastery(masteryMap.getOrDefault(point.getId(), 0));
                    return m;
                })
                .collect(Collectors.toList());

        StudentGrowthVO.SubjectGraph graph = new StudentGrowthVO.SubjectGraph();
        if (filtered.isEmpty()) {
            return graph;
        }

        Set<Long> filteredIds = filtered.stream().map(StudentGrowthStatsDTO.KnowledgeMastery::getId).collect(Collectors.toSet());

        List<EduKnowledgeRelation> relations = knowledgeRelationMapper.selectAll();

        List<StudentGrowthVO.KnowledgeNode> allNodes = new ArrayList<>();
        List<StudentGrowthVO.KnowledgeRelation> allRelations = new ArrayList<>();

        Map<Long, StudentGrowthVO.KnowledgeNode> nodeMap = new HashMap<>();
        for (StudentGrowthStatsDTO.KnowledgeMastery m : filtered) {
            StudentGrowthVO.KnowledgeNode node = new StudentGrowthVO.KnowledgeNode();
            node.setId(m.getId());
            node.setName(m.getName());
            node.setMastery(m.getMastery());
            node.setType("core");
            allNodes.add(node);
            nodeMap.put(m.getId(), node);
        }

        for (EduKnowledgeRelation rel : relations) {
            if (filteredIds.contains(rel.getSourceId()) && filteredIds.contains(rel.getTargetId())) {
                StudentGrowthVO.KnowledgeRelation relation = new StudentGrowthVO.KnowledgeRelation();
                relation.setSourceId(rel.getSourceId());
                relation.setTargetId(rel.getTargetId());
                relation.setRelationType(rel.getRelationType());
                allRelations.add(relation);
            }
        }

        graph.setAllNodes(allNodes);
        graph.setRelations(allRelations);

        if (!allNodes.isEmpty()) {
            graph.setCoreNode(allNodes.get(0));
            List<StudentGrowthVO.KnowledgeNode> otherNodes = allNodes.size() > 1 ? allNodes.subList(1, allNodes.size()) : new ArrayList<>();
            graph.setPreNodes(otherNodes);
        }

        graph.setGoodChain("");
        graph.setWeakChain("");

        return graph;
    }

    private Set<Long> getStudentCourseKnowledgeIds(Long studentId) {
        List<Long> chapterIds = studyRecordMapper.selectStudyChapterIds(studentId);
        log.info("getStudentCourseKnowledgeIds: studentId={}, found {} studied chapters", studentId, chapterIds.size());
        if (chapterIds.size() > 0) {
            log.info("getStudentCourseKnowledgeIds: chapterIds={}", chapterIds);
        }

        if (chapterIds.isEmpty()) {
            log.warn("getStudentCourseKnowledgeIds: no studied chapters found for studentId={}", studentId);
            return new HashSet<>();
        }

        List<EduChapterKnowledgePoint> chapterKnowledgePoints = chapterService.selectByChapterIds(chapterIds);
        log.info("getStudentCourseKnowledgeIds: found {} chapter-knowledge associations", chapterKnowledgePoints.size());
        
        Set<Long> knowledgeIds = chapterKnowledgePoints.stream()
                .map(EduChapterKnowledgePoint::getKnowledgePointId)
                .collect(Collectors.toSet());
        log.info("getStudentCourseKnowledgeIds: found {} unique knowledge points", knowledgeIds.size());

        return knowledgeIds;
    }

    private Set<Long> getCategoryAndChildIds(Long catId, List<EduKnowledgeCategory> allCategories) {
        Set<Long> ids = new HashSet<>();
        Map<Long, EduKnowledgeCategory> map = new HashMap<>();
        fillCategoryMap(allCategories, map);
        collectCategoryIds(catId, map, ids);
        return ids;
    }

    private void fillCategoryMap(List<EduKnowledgeCategory> categories, Map<Long, EduKnowledgeCategory> map) {
        for (EduKnowledgeCategory cat : categories) {
            map.put(cat.getId(), cat);
            if (cat.getChildren() != null) {
                fillCategoryMap(cat.getChildren(), map);
            }
        }
    }

    private void collectCategoryIds(Long catId, Map<Long, EduKnowledgeCategory> map, Set<Long> ids) {
        EduKnowledgeCategory cat = map.get(catId);
        if (cat != null) {
            ids.add(cat.getId());
            if (cat.getChildren() != null) {
                for (EduKnowledgeCategory child : cat.getChildren()) {
                    collectCategoryIds(child.getId(), map, ids);
                }
            }
        }
    }

    private String getCategoryColor(Long id) {
        String[] colors = {"#409EFF", "#67C23A", "#E6A23C", "#F56C6C", "#909399", "#C0C4CC"};
        return colors[(int) (id % colors.length)];
    }

    @Override
    public StudentGrowthVO.WeekPlanDetail getWeekPlanDetail(Long studentId, Integer weekIndex) {
        int safeIndex = weekIndex != null && weekIndex > 0 ? weekIndex : 0;
        LocalDate monday = currentMonday().minusWeeks(safeIndex);
        LocalDate sunday = monday.plusDays(6);
        StudentGrowthPlanDTO.WeekPlanRow plan = studentGrowthMapper.selectGrowthWeekPlan(studentId, monday);
        if (plan == null) {
            return generateAndPersistWeekPlan(studentId, safeIndex, monday, sunday);
        }
        return convertPersistedWeekPlan(studentId, safeIndex, plan);
    }

    @Override
    public StudentGrowthVO.WeekPlanDetail regenerateWeekPlan(Long studentId, Integer weekIndex) {
        int safeIndex = weekIndex != null && weekIndex > 0 ? weekIndex : 0;
        LocalDate monday = currentMonday().minusWeeks(safeIndex);
        studentGrowthMapper.deleteGrowthTasksByWeek(studentId, monday);
        studentGrowthMapper.deleteGrowthPlanByWeek(studentId, monday);
        return generateAndPersistWeekPlan(studentId, safeIndex, monday, monday.plusDays(6));
    }

    @Override
    public StudentGrowthVO.TaskStatus updateWeeklyTaskStatus(Long studentId, Long taskId, Boolean completed) {
        Boolean safeCompleted = Boolean.TRUE.equals(completed);
        studentGrowthMapper.updateGrowthTaskStatus(taskId, studentId, safeCompleted);
        StudentGrowthVO.TaskStatus status = new StudentGrowthVO.TaskStatus();
        status.setTaskId(taskId);
        status.setCompleted(safeCompleted);
        return status;
    }

    private StudentGrowthVO.WeekPlanDetail generateAndPersistWeekPlan(Long studentId, int weekIndex,
                                                                       LocalDate monday, LocalDate sunday) {
        List<StudentGrowthStatsDTO.DaySummary> daySummaries =
                studentGrowthMapper.selectDaySummaries(studentId, monday, sunday);
        List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints = studentGrowthMapper.selectKnowledgeMastery(studentId)
                .stream().filter(item -> item.getMastery() != null && item.getMastery() < 80).limit(5).toList();
        StudentGrowthPlanDTO.WeekPlanRow plan = buildPlanRow(studentId, weekIndex, monday, sunday, weakPoints);
        studentGrowthMapper.insertGrowthWeekPlan(plan);
        List<StudentGrowthPlanDTO.TaskRow> taskRows = buildTaskRows(plan, weakPoints);
        if (!taskRows.isEmpty()) {
            studentGrowthMapper.insertGrowthPlanTasks(taskRows);
        }
        return convertWeekPlan(studentId, weekIndex, plan, taskRows, daySummaries);
    }

    private StudentGrowthVO.WeekPlanDetail convertPersistedWeekPlan(Long studentId, int weekIndex,
                                                                     StudentGrowthPlanDTO.WeekPlanRow plan) {
        List<StudentGrowthPlanDTO.TaskRow> taskRows = studentGrowthMapper.selectGrowthPlanTasks(plan.getId());
        List<StudentGrowthStatsDTO.DaySummary> daySummaries =
                studentGrowthMapper.selectDaySummaries(studentId, plan.getWeekStartDate(), plan.getWeekEndDate());
        return convertWeekPlan(studentId, weekIndex, plan, taskRows, daySummaries);
    }

    private StudentGrowthVO.WeekPlanDetail convertWeekPlan(Long studentId, int weekIndex,
                                                            StudentGrowthPlanDTO.WeekPlanRow plan,
                                                            List<StudentGrowthPlanDTO.TaskRow> taskRows,
                                                            List<StudentGrowthStatsDTO.DaySummary> daySummaries) {
        StudentGrowthVO.WeekPlanDetail detail = new StudentGrowthVO.WeekPlanDetail();
        detail.setPlanId(plan.getId() != null ? plan.getId() : 0L);
        detail.setWeekName(buildWeekName(plan.getWeekStartDate()));
        detail.setStatusText(weekIndex == 0 ? "进行中" : "历史周");
        detail.setWeakPoints(plan.getWeakPoints());
        detail.setAdvantageSummary(plan.getAdvantageSummary());
        detail.setWeakSummary(plan.getWeakSummary());
        detail.setStudySuggestion(plan.getStudySuggestion());
        detail.setDailyPlanList(buildDailyPlansFromRows(plan, taskRows, daySummaries));
        StudentGrowthVO.WeekStats total = new StudentGrowthVO.WeekStats();
        total.setDuration(defaultInt(plan.getDurationTarget()));
        total.setQuestions(defaultInt(plan.getQuestionTarget()));
        total.setKnowledge(defaultInt(plan.getKnowledgeTarget()));
        detail.setWeekTotal(total);
        detail.setRecommendCourses(buildRecommendCourses(studentId));
        return detail;
    }

    @Override
    public List<StudentGrowthVO.WeekPlanDetail> getWeekPlanDetailList(Long studentId) {
        List<StudentGrowthVO.WeekPlanDetail> list = new ArrayList<>();
        for (int i = 0; i < WEEK_COUNT; i++) {
            StudentGrowthVO.WeekPlanDetail detail = getWeekPlanDetail(studentId, i);
            detail.setDailyPlanList(new ArrayList<>());
            detail.setRecommendCourses(new ArrayList<>());
            list.add(detail);
        }
        return list;
    }

    private StudentGrowthVO.TodayStats buildTodayStats(Long studentId) {
        LocalDate today = LocalDate.now();
        StudentGrowthVO.TodayStats stats = new StudentGrowthVO.TodayStats();
        stats.setDuration(defaultInt(studentGrowthMapper.selectStudySecondsByDate(studentId, today)) / 60);
        stats.setStreak(defaultInt(studentGrowthMapper.selectStudyStreak(studentId)));
        stats.setKnowledge(defaultInt(studentGrowthMapper.selectKnowledgeCountByDate(studentId, today)));
        return stats;
    }

    private StudentGrowthVO.WeekStats buildCurrentWeekDone(Long studentId) {
        LocalDate monday = currentMonday();
        LocalDate sunday = monday.plusDays(6);
        List<StudentGrowthStatsDTO.DaySummary> days = studentGrowthMapper.selectDaySummaries(studentId, monday, sunday);
        StudentGrowthVO.WeekStats stats = new StudentGrowthVO.WeekStats();
        stats.setDuration(days.stream().mapToInt(d -> defaultInt(d.getStudySeconds()) / 60).sum());
        stats.setQuestions(days.stream().mapToInt(d -> defaultInt(d.getPracticeCount())).sum());
        stats.setKnowledge(days.stream().mapToInt(d -> defaultInt(d.getKnowledgeCount())).sum());
        return stats;
    }

    private StudentGrowthVO.WeekStats buildWeekTarget(StudentGrowthVO.WeekStats done) {
        StudentGrowthVO.WeekStats target = new StudentGrowthVO.WeekStats();
        target.setDuration(Math.max(done.getDuration(), 0));
        target.setQuestions(Math.max(done.getQuestions(), 0));
        target.setKnowledge(Math.max(done.getKnowledge(), 0));
        return target;
    }

    private Integer calculateGrowthValue(Long studentId) {
        int minutes = defaultInt(studentGrowthMapper.selectTotalStudyMinutes(studentId));
        int examScore = defaultInt(studentGrowthMapper.selectAverageExamScore(studentId));
        int studyDays = defaultInt(studentGrowthMapper.selectStudyDays(studentId));
        return minutes + examScore * 10 + studyDays * 20;
    }

    private List<StudentGrowthVO.DailyTask> buildTodayTasks(Long studentId) {
        return studentGrowthMapper.selectWeakKnowledgeCourses(studentId, 3).stream().map(item -> {
            StudentGrowthVO.DailyTask task = new StudentGrowthVO.DailyTask();
            task.setId(item.getId());
            task.setName(item.getTitle());
            task.setKnowledge(item.getKnowledge());
            task.setDesc("建议学习并完成配套练习");
            task.setType("course");
            task.setTypeText("课程");
            task.setDone(false);
            return task;
        }).toList();
    }

    private List<StudentGrowthVO.Badge> buildBadges(StudentGrowthVO.HomeData data) {
        List<StudentGrowthVO.Badge> badges = new ArrayList<>();
        if (data.getTodayStats().getStreak() > 0) badges.add(badge(1L, "连续学习", "icon-xuexiao", "#FF7D00"));
        if (data.getWeekDone().getDuration() > 0) badges.add(badge(2L, "学习起步", "icon-shuji", "#409EFF"));
        if (data.getWeekDone().getQuestions() > 0) badges.add(badge(3L, "练习进阶", "icon-shijuan", "#67C23A"));
        if (data.getWeekDone().getKnowledge() > 0) badges.add(badge(4L, "知识积累", "icon-ai64", "#E6A23C"));
        return badges;
    }

    private String buildHighlightTip(Long studentId) {
        List<StudentGrowthVO.WeekReport> reports = getWeekReports(studentId);
        if (reports.isEmpty()) {
            return "";
        }
        StudentGrowthVO.WeekReport report = reports.get(0);
        return report.getTotalScore() > 0 ? "本周综合能力评分 " + report.getTotalScore() + " 分" : "";
    }

    private StudentGrowthVO.WeekReport convertWeekReport(StudentGrowthStatsDTO.WeekSummary current,
                                                          StudentGrowthStatsDTO.WeekSummary previous,
                                                          Long studentId) {
        StudentGrowthVO.WeekReport report = new StudentGrowthVO.WeekReport();
        int totalScore = calculateTotalScore(current);
        int lastScore = previous != null ? calculateTotalScore(previous) : totalScore;
        report.setWeekName(buildWeekName(current.getWeekStartDate()));
        report.setTotalScore(totalScore);
        report.setTotalDiff(totalScore - lastScore);
        report.setTotalDuration(roundHour(defaultInt(current.getStudySeconds())));
        report.setStudyDays(defaultInt(current.getStudyDays()));
        report.setMasteryOffset(calculateMastery(current) - (previous != null ? calculateMastery(previous) : calculateMastery(current)));
        report.setAbilityList(buildAbilityList(current, previous));
        report.setDurationList(buildDurationList(studentId, current.getWeekStartDate()));
        List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints = studentGrowthMapper.selectKnowledgeMastery(studentId)
                .stream().filter(item -> defaultInt(item.getMastery()) < 80).limit(3).toList();
        applyReportSummary(report, weakPoints);
        return report;
    }

    private List<StudentGrowthVO.AbilityScore> buildAbilityList(StudentGrowthStatsDTO.WeekSummary current,
                                                                StudentGrowthStatsDTO.WeekSummary previous) {
        return List.of(
                ability("专注力", scoreStudy(current), previous != null ? scoreStudy(previous) : scoreStudy(current)),
                ability("理解归纳", calculateMastery(current), previous != null ? calculateMastery(previous) : calculateMastery(current)),
                ability("逻辑解题", scoreExam(current), previous != null ? scoreExam(previous) : scoreExam(current)),
                ability("纠错复盘", scorePractice(current), previous != null ? scorePractice(previous) : scorePractice(current)),
                ability("自主规划", scorePlan(current), previous != null ? scorePlan(previous) : scorePlan(current))
        );
    }

    private List<StudentGrowthVO.DurationItem> buildDurationList(Long studentId, LocalDate weekStart) {
        List<StudentGrowthStatsDTO.DaySummary> summaries =
                studentGrowthMapper.selectDaySummaries(studentId, weekStart, weekStart.plusDays(6));
        Map<LocalDate, StudentGrowthStatsDTO.DaySummary> summaryMap = summaries.stream()
                .collect(Collectors.toMap(StudentGrowthStatsDTO.DaySummary::getStudyDate, item -> item));
        List<StudentGrowthVO.DurationItem> list = new ArrayList<>();
        List<String> labels = List.of("周一", "周二", "周三", "周四", "周五", "周六", "周日");
        for (int i = 0; i < labels.size(); i++) {
            StudentGrowthVO.DurationItem item = new StudentGrowthVO.DurationItem();
            item.setDay(labels.get(i));
            StudentGrowthStatsDTO.DaySummary summary = summaryMap.get(weekStart.plusDays(i));
            item.setMinutes(summary != null ? defaultInt(summary.getStudySeconds()) / 60 : 0);
            list.add(item);
        }
        return list;
    }

    private StudentGrowthPlanDTO.WeekPlanRow buildPlanRow(Long studentId, int weekIndex, LocalDate monday,
                                                           LocalDate sunday,
                                                           List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints) {
        StudentGrowthPlanDTO.WeekPlanRow plan = new StudentGrowthPlanDTO.WeekPlanRow();
        plan.setStudentId(studentId);
        plan.setWeekStartDate(monday);
        plan.setWeekEndDate(sunday);
        plan.setStatus(weekIndex == 0 ? 1 : 3);
        plan.setWeakPoints(joinKnowledgeNames(weakPoints));
        plan.setAdvantageSummary(buildAdvantageSummary(weakPoints));
        plan.setWeakSummary(buildWeakSummary(weakPoints));
        plan.setStudySuggestion(buildStudySuggestion(weakPoints));
        plan.setDurationTarget(Math.max(210, weakPoints.size() * 30 * 2));
        plan.setQuestionTarget(Math.max(35, weakPoints.size() * 10));
        plan.setKnowledgeTarget(Math.max(weakPoints.size(), 1));
        return plan;
    }

    private List<StudentGrowthPlanDTO.TaskRow> buildTaskRows(StudentGrowthPlanDTO.WeekPlanRow plan,
                                                              List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints) {
        if (weakPoints.isEmpty()) {
            return new ArrayList<>();
        }
        List<StudentGrowthPlanDTO.TaskRow> rows = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            StudentGrowthStatsDTO.KnowledgeMastery point = weakPoints.get(i % weakPoints.size());
            rows.add(buildTaskRow(plan, point, plan.getWeekStartDate().plusDays(i), i));
        }
        return rows;
    }

    private StudentGrowthPlanDTO.TaskRow buildTaskRow(StudentGrowthPlanDTO.WeekPlanRow plan,
                                                       StudentGrowthStatsDTO.KnowledgeMastery point,
                                                       LocalDate date,
                                                       int index) {
        StudentGrowthPlanDTO.TaskRow row = new StudentGrowthPlanDTO.TaskRow();
        row.setPlanId(plan.getId());
        row.setStudentId(plan.getStudentId());
        row.setPlanDate(date);
        row.setTaskType(index % 3 == 1 ? "practice" : "learn");
        row.setJumpType(index % 3 == 1 ? "question" : "course");
        row.setTitle(point.getName() + (index % 3 == 1 ? "专项练习" : "专项学习"));
        row.setKnowledgePointId(point.getId());
        row.setKnowledgeName(point.getName());
        row.setTargetId(point.getCourseId());
        row.setDurationTarget(index % 3 == 1 ? 0 : 30);
        row.setQuestionTarget(index % 3 == 1 ? 10 : 0);
        row.setCompleted(defaultInt(point.getMastery()) >= 80);
        return row;
    }

    private List<StudentGrowthVO.DailyPlan> buildDailyPlansFromRows(StudentGrowthPlanDTO.WeekPlanRow plan,
                                                                     List<StudentGrowthPlanDTO.TaskRow> taskRows,
                                                                     List<StudentGrowthStatsDTO.DaySummary> daySummaries) {
        Map<LocalDate, StudentGrowthStatsDTO.DaySummary> dayMap = daySummaries.stream()
                .collect(Collectors.toMap(StudentGrowthStatsDTO.DaySummary::getStudyDate, item -> item));
        Map<LocalDate, List<StudentGrowthPlanDTO.TaskRow>> taskMap = taskRows.stream()
                .collect(Collectors.groupingBy(StudentGrowthPlanDTO.TaskRow::getPlanDate));
        List<StudentGrowthVO.DailyPlan> plans = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = plan.getWeekStartDate().plusDays(i);
            plans.add(convertDailyPlan(date, dayMap.get(date), taskMap.getOrDefault(date, new ArrayList<>())));
        }
        return plans;
    }

    private StudentGrowthVO.DailyPlan convertDailyPlan(LocalDate date, StudentGrowthStatsDTO.DaySummary summary,
                                                        List<StudentGrowthPlanDTO.TaskRow> tasks) {
        StudentGrowthVO.DailyPlan plan = new StudentGrowthVO.DailyPlan();
        plan.setWeekday(weekday(date));
        plan.setDate(date.format(DATE_LABEL));
        plan.setDurationDone(summary != null ? defaultInt(summary.getStudySeconds()) / 60 : 0);
        plan.setQuestionDone(summary != null ? defaultInt(summary.getPracticeCount()) : 0);
        plan.setKnowledgeDone(summary != null ? defaultInt(summary.getKnowledgeCount()) : 0);
        plan.setDurationTarget(tasks.stream().mapToInt(t -> defaultInt(t.getDurationTarget())).sum());
        plan.setQuestionTarget(tasks.stream().mapToInt(t -> defaultInt(t.getQuestionTarget())).sum());
        plan.setKnowledgeTarget((int) tasks.stream().map(StudentGrowthPlanDTO.TaskRow::getKnowledgePointId)
                .filter(Objects::nonNull).distinct().count());
        plan.setTasks(tasks.stream().map(this::convertTaskItem).toList());
        return plan;
    }

    private StudentGrowthVO.TaskItem convertTaskItem(StudentGrowthPlanDTO.TaskRow row) {
        StudentGrowthVO.TaskItem task = new StudentGrowthVO.TaskItem();
        task.setTaskId(row.getId());
        task.setTargetId(row.getTargetId());
        task.setType(row.getTaskType());
        task.setJumpType(row.getJumpType());
        task.setTitle(row.getTitle());
        task.setKnowledge(row.getKnowledgeName());
        task.setDuration(defaultInt(row.getDurationTarget()));
        task.setQuestionCount(defaultInt(row.getQuestionTarget()));
        task.setDone(Boolean.TRUE.equals(row.getCompleted()));
        return task;
    }

    private List<StudentGrowthVO.DailyPlan> buildDailyPlans(List<StudentGrowthStatsDTO.DaySummary> daySummaries,
                                                             List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints) {
        Map<LocalDate, StudentGrowthStatsDTO.DaySummary> dayMap = daySummaries.stream()
                .collect(Collectors.toMap(StudentGrowthStatsDTO.DaySummary::getStudyDate, item -> item));
        LocalDate monday = daySummaries.isEmpty() ? currentMonday() : daySummaries.get(0).getStudyDate();
        List<StudentGrowthVO.DailyPlan> plans = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = monday.plusDays(i);
            plans.add(buildDailyPlan(date, dayMap.get(date), weakPoints));
        }
        return plans;
    }

    private StudentGrowthVO.DailyPlan buildDailyPlan(LocalDate date, StudentGrowthStatsDTO.DaySummary summary,
                                                      List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints) {
        StudentGrowthVO.DailyPlan plan = new StudentGrowthVO.DailyPlan();
        plan.setWeekday(weekday(date));
        plan.setDate(date.format(DATE_LABEL));
        plan.setDurationDone(summary != null ? defaultInt(summary.getStudySeconds()) / 60 : 0);
        plan.setQuestionDone(summary != null ? defaultInt(summary.getPracticeCount()) : 0);
        plan.setKnowledgeDone(summary != null ? defaultInt(summary.getKnowledgeCount()) : 0);
        plan.setDurationTarget(plan.getDurationDone());
        plan.setQuestionTarget(plan.getQuestionDone());
        plan.setKnowledgeTarget(plan.getKnowledgeDone());
        plan.setTasks(buildTaskItems(weakPoints));
        return plan;
    }

    private List<StudentGrowthVO.TaskItem> buildTaskItems(List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints) {
        return weakPoints.stream().limit(3).map(item -> {
            StudentGrowthVO.TaskItem task = new StudentGrowthVO.TaskItem();
            task.setTaskId(item.getId());
            task.setType("learn");
            task.setJumpType("course");
            task.setTitle(item.getName());
            task.setKnowledge(item.getName());
            task.setDuration(0);
            task.setQuestionCount(defaultInt(item.getPracticeCount()));
            task.setDone(defaultInt(item.getMastery()) >= 80);
            return task;
        }).toList();
    }

    private List<StudentGrowthVO.RecommendCourse> buildRecommendCourses(Long studentId) {
        return studentGrowthMapper.selectWeakKnowledgeCourses(studentId, 5).stream().map(item -> {
            StudentGrowthVO.RecommendCourse course = new StudentGrowthVO.RecommendCourse();
            course.setId(item.getId());
            course.setTitle(item.getTitle());
            course.setKnowledge(item.getKnowledge());
            course.setLessonCount(defaultInt(item.getLessonCount()));
            course.setDifficulty(difficultyText(item.getDifficulty()));
            course.setDuration(0);
            course.setCoverColor("#409EFF");
            return course;
        }).toList();
    }

    private List<StudentGrowthVO.AbilityDimension> buildOverviewDimensions(Long studentId) {
        List<StudentGrowthVO.WeekReport> reports = getWeekReports(studentId);
        if (reports.isEmpty()) {
            return new ArrayList<>();
        }
        return reports.get(0).getAbilityList().stream().map(item -> {
            StudentGrowthVO.AbilityDimension dimension = new StudentGrowthVO.AbilityDimension();
            dimension.setId((long) (reports.get(0).getAbilityList().indexOf(item) + 1));
            dimension.setName(item.getName());
            dimension.setScore(item.getCurrent());
            dimension.setFullScore(100);
            return dimension;
        }).toList();
    }

    private List<StudentGrowthVO.StudyRecord> buildRecentStudyRecords(Long studentId) {
        return studentGrowthMapper.selectRecentStudyRecords(studentId, 5).stream().map(item -> {
            StudentGrowthVO.StudyRecord record = new StudentGrowthVO.StudyRecord();
            record.setId(item.getId());
            record.setCourseName(item.getCourseName());
            record.setDurationMinutes(defaultInt(item.getDurationMinutes()));
            record.setStudyTime(item.getStudyTime());
            return record;
        }).toList();
    }

    private StudentGrowthVO.WeeklyTask convertWeeklyTask(StudentGrowthVO.TaskItem item) {
        StudentGrowthVO.WeeklyTask task = new StudentGrowthVO.WeeklyTask();
        task.setId(item.getTaskId());
        task.setTitle(item.getTitle());
        task.setTaskType("practice".equals(item.getType()) ? 2 : 1);
        task.setPlannedMinutes(item.getDuration());
        task.setCompletedMinutes(Boolean.TRUE.equals(item.getDone()) ? item.getDuration() : 0);
        task.setCompleted(item.getDone());
        return task;
    }

    private StudentGrowthVO.WeekStats sumWeekTarget(List<StudentGrowthVO.DailyPlan> plans) {
        StudentGrowthVO.WeekStats stats = new StudentGrowthVO.WeekStats();
        stats.setDuration(plans.stream().mapToInt(StudentGrowthVO.DailyPlan::getDurationTarget).sum());
        stats.setQuestions(plans.stream().mapToInt(StudentGrowthVO.DailyPlan::getQuestionTarget).sum());
        stats.setKnowledge(plans.stream().mapToInt(StudentGrowthVO.DailyPlan::getKnowledgeTarget).sum());
        return stats;
    }

    private StudentGrowthVO.SubjectItem buildSubjectItem(Long courseId, String courseName) {
        StudentGrowthVO.SubjectItem item = new StudentGrowthVO.SubjectItem();
        item.setKey(String.valueOf(courseId));
        item.setName(courseName != null ? courseName : "课程" + courseId);
        item.setColor("#409EFF");
        return item;
    }

    private List<StudentGrowthStatsDTO.KnowledgeMastery> filterBySubject(List<StudentGrowthStatsDTO.KnowledgeMastery> source,
                                                                          String subject) {
        if (subject == null || subject.isBlank()) {
            return source;
        }
        return source.stream().filter(item -> Objects.equals(String.valueOf(item.getCourseId()), subject)).toList();
    }

    private StudentGrowthVO.KnowledgeNode convertKnowledgeNode(StudentGrowthStatsDTO.KnowledgeMastery item) {
        return convertKnowledgeNode(item, null);
    }

    private StudentGrowthVO.KnowledgeNode convertKnowledgeNode(StudentGrowthStatsDTO.KnowledgeMastery item,
                                                               List<StudentGrowthPlanDTO.QuestionTypeMastery> types) {
        StudentGrowthVO.KnowledgeNode node = new StudentGrowthVO.KnowledgeNode();
        node.setId(item.getId());
        node.setName(item.getName());
        node.setMastery(defaultInt(item.getMastery()));
        node.setType(defaultInt(item.getMastery()) >= 80 ? "pre" : "next");
        if (types == null || types.isEmpty()) {
            StudentGrowthVO.QuestionType type = new StudentGrowthVO.QuestionType();
            type.setName("综合练习");
            type.setMastery(defaultInt(item.getMastery()));
            node.setQuestionTypes(List.of(type));
        } else {
            node.setQuestionTypes(types.stream().map(this::convertQuestionType).toList());
        }
        return node;
    }

    private StudentGrowthVO.QuestionType convertQuestionType(StudentGrowthPlanDTO.QuestionTypeMastery mastery) {
        StudentGrowthVO.QuestionType type = new StudentGrowthVO.QuestionType();
        type.setName(questionTypeText(mastery.getQuestionType()));
        type.setMastery(defaultInt(mastery.getMastery()));
        return type;
    }

    private Map<Long, List<StudentGrowthPlanDTO.QuestionTypeMastery>> buildQuestionTypeMap(
            Long studentId,
            List<StudentGrowthStatsDTO.KnowledgeMastery> points) {
        List<Long> ids = points.stream().map(StudentGrowthStatsDTO.KnowledgeMastery::getId)
                .filter(Objects::nonNull).distinct().toList();
        if (ids.isEmpty()) {
            return new HashMap<>();
        }
        return studentGrowthMapper.selectQuestionTypeMastery(studentId, ids).stream()
                .collect(Collectors.groupingBy(StudentGrowthPlanDTO.QuestionTypeMastery::getKnowledgePointId));
    }

    private void applyReportSummary(StudentGrowthVO.WeekReport report,
                                    List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints) {
        List<StudentGrowthVO.AbilityScore> abilities = report.getAbilityList();
        StudentGrowthVO.AbilityScore best = abilities.stream()
                .max(Comparator.comparing(StudentGrowthVO.AbilityScore::getCurrent))
                .orElse(null);
        String weakNames = joinKnowledgeNames(weakPoints);
        report.setAdvantageSummary(best == null ? "本周学习数据较少，建议先完成每日学习任务。"
                : best.getName() + "表现最好，当前得分 " + best.getCurrent() + " 分，请保持稳定投入。");
        report.setWeakSummary(weakNames.isBlank() ? "暂无明显薄弱知识点，继续通过练习巩固掌握度。"
                : weakNames + " 掌握度偏低，需要通过课程学习和专项练习补强。");
        report.setStudySuggestion(weakNames.isBlank() ? "优先保持每周学习节奏，并完成阶段测评验证掌握情况。"
                : "优先复习 " + weakNames + "，每天完成对应课程学习和不少于10道专项练习。");
    }

    private String buildAdvantageSummary(List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints) {
        if (weakPoints.isEmpty()) {
            return "当前知识点掌握较稳定，建议保持学习节奏并通过测评检验效果。";
        }
        return "已定位薄弱知识点，按计划完成后可提升理解归纳与纠错复盘能力。";
    }

    private String buildWeakSummary(List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints) {
        String names = joinKnowledgeNames(weakPoints);
        if (names.isBlank()) {
            return "暂无明显薄弱知识点。";
        }
        return names + " 掌握度低于80%，需要重点补强。";
    }

    private String buildStudySuggestion(List<StudentGrowthStatsDTO.KnowledgeMastery> weakPoints) {
        String names = joinKnowledgeNames(weakPoints);
        if (names.isBlank()) {
            return "优先保持每日学习和练习节奏，每周完成一次综合测评。";
        }
        return "优先围绕 " + names + " 安排课程学习、专项练习和错题复盘。";
    }

    private String joinKnowledgeNames(List<StudentGrowthStatsDTO.KnowledgeMastery> points) {
        return points.stream().map(StudentGrowthStatsDTO.KnowledgeMastery::getName)
                .filter(Objects::nonNull).distinct().collect(Collectors.joining("、"));
    }

    private StudentGrowthVO.Badge badge(Long id, String name, String iconClass, String color) {
        StudentGrowthVO.Badge badge = new StudentGrowthVO.Badge();
        badge.setId(id);
        badge.setName(name);
        badge.setIconClass(iconClass);
        badge.setColor(color);
        return badge;
    }

    private StudentGrowthVO.AbilityScore ability(String name, int current, int lastWeek) {
        StudentGrowthVO.AbilityScore ability = new StudentGrowthVO.AbilityScore();
        ability.setName(name);
        ability.setCurrent(current);
        ability.setLastWeek(lastWeek);
        ability.setDiff(current - lastWeek);
        return ability;
    }

    private int calculateTotalScore(StudentGrowthStatsDTO.WeekSummary summary) {
        return Math.round((scoreStudy(summary) + scorePractice(summary) + scoreExam(summary) + calculateMastery(summary) + scorePlan(summary)) / 5.0F);
    }

    private int scoreStudy(StudentGrowthStatsDTO.WeekSummary summary) {
        return clamp(defaultInt(summary.getStudySeconds()) / 60 * 100 / 300);
    }

    private int scorePractice(StudentGrowthStatsDTO.WeekSummary summary) {
        int total = defaultInt(summary.getPracticeCount());
        if (total == 0) return 0;
        return clamp(defaultInt(summary.getPracticeCorrectCount()) * 100 / total);
    }

    private int scoreExam(StudentGrowthStatsDTO.WeekSummary summary) {
        return clamp(defaultInt(summary.getExamAvgScore()));
    }

    private int calculateMastery(StudentGrowthStatsDTO.WeekSummary summary) {
        return clamp(defaultInt(summary.getKnowledgeCount()) * 100 / 10);
    }

    private int scorePlan(StudentGrowthStatsDTO.WeekSummary summary) {
        return clamp(defaultInt(summary.getStudyDays()) * 100 / 7);
    }

    private int defaultInt(Integer value) {
        return value != null ? value : 0;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }

    private double roundHour(int seconds) {
        return Math.round(seconds / 3600.0D * 10D) / 10D;
    }

    private LocalDate currentMonday() {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }

    private String buildWeekName(LocalDate monday) {
        if (monday == null) return "";
        return monday.format(DATE_LABEL) + "-" + monday.plusDays(6).format(DATE_LABEL);
    }

    private String weekday(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> "周一";
            case TUESDAY -> "周二";
            case WEDNESDAY -> "周三";
            case THURSDAY -> "周四";
            case FRIDAY -> "周五";
            case SATURDAY -> "周六";
            case SUNDAY -> "周日";
        };
    }

    private String joinNames(List<StudentGrowthVO.KnowledgeNode> nodes) {
        return nodes.stream().map(StudentGrowthVO.KnowledgeNode::getName).collect(Collectors.joining(" > "));
    }

    private String difficultyText(Integer difficulty) {
        if (difficulty == null) return "";
        if (difficulty <= 2) return "基础";
        if (difficulty == 3) return "进阶";
        return "挑战";
    }

    private String questionTypeText(Integer questionType) {
        if (questionType == null) return "综合题";
        return switch (questionType) {
            case 0 -> "单选题";
            case 1 -> "多选题";
            case 2 -> "判断题";
            case 3 -> "简答题";
            case 4 -> "填空题";
            default -> "综合题";
        };
    }
}
