package com.yub.edu.biz.service.impl;

import com.yub.edu.biz.entity.EduMajor;
import com.yub.edu.biz.mapper.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentCategoriesRepairTest {
    private EduMajor major(long id, long parent, int status) {
        EduMajor major = new EduMajor(); major.setId(id); major.setParentId(parent);
        major.setStatus(status); major.setName("分类" + id); return major;
    }
    @Test void buildsRealNestedCategoriesAndExcludesDisabledBranches() {
        EduMajorMapper majors = mock(EduMajorMapper.class);
        when(majors.selectTree()).thenReturn(List.of(major(1, 0, 1), major(2, 1, 1), major(3, 2, 1),
                major(4, 0, 0), major(5, 4, 1)));
        var service = new EduCourseServiceImpl(mock(EduCourseMapper.class), majors, mock(EduTeacherMapper.class));
        var categories = service.studentCategories();
        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getChildren().get(0).getChildren().get(0).getId()).isEqualTo(3L);
    }
}
