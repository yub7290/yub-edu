package com.yub.edu.biz.vo;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/** 学生端公开分类，仅包含展示字段。 */
@Data
public class CourseCategoryNodeVO {
    private Long id;
    private String name;
    private List<CourseCategoryNodeVO> children = new ArrayList<>();
}
