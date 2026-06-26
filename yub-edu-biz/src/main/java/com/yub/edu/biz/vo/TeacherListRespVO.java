package com.yub.edu.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生端教师列表响应 VO
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-22
 * @Description: 移动端教师列表展示
 * @Version: 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherListRespVO {

    /** 教师ID */
    private Long id;

    /** 头像URL */
    private String avatarUrl;

    /** 姓名 */
    private String name;

    /** 职称名称 */
    private String titleName;

    /** 个人签名 */
    private String signature;

    /** 评分（1-5分） */
    private Integer rating;
}
