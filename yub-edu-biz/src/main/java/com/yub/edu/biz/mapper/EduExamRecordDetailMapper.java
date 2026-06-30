package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduExamRecordDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试答题明细 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-25
 * @Description: 考试答题明细数据访问层
 * @Version: 1.0.0
 */
@Mapper
public interface EduExamRecordDetailMapper {

    /**
     * 批量插入答题明细
     *
     * @param list 答题明细列表
     * @return 影响行数
     */
    int insertBatch(List<EduExamRecordDetail> list);

    /**
     * 根据考试记录ID查询答题明细
     *
     * @param recordId 考试记录ID
     * @return 答题明细列表
     */
    List<EduExamRecordDetail> selectByRecordId(@Param("recordId") Long recordId);
}
