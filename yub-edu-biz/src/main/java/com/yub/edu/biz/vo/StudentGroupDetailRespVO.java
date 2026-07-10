package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学员组详情响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-08
 * @Description: 学员组详情响应
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroupDetailRespVO {

    /** 学员组ID */
    private Long id;

    /** 学员组名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（1=启用 0=禁用） */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 学员人数 */
    private Integer memberCount;

    /** 课程数量 */
    private Integer courseCount;

    /** 成员列表 */
    private List<MemberItem> members;

    /** 课程列表 */
    private List<CourseItem> courses;

    /**
     * 成员项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberItem {
        /** 学员ID */
        private Long id;
        /** 姓名 */
        private String studentName;
        /** 学员编号 */
        private String studentNo;
        /** 手机号 */
        private String phone;
        /** 头像 */
        private String avatar;
    }

    /**
     * 课程项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseItem {
        /** 课程ID */
        private Long id;
        /** 课程名称 */
        private String courseName;
        /** 排序 */
        private Integer sortOrder;
    }
}
