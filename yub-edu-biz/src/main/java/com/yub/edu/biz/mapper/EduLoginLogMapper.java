package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduLoginLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学员登录日志 Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: 学员登录日志
 * @Version: 1.0.0
 */
public interface EduLoginLogMapper {

    int insert(EduLoginLog log);

    List<EduLoginLog> selectByStudentId(@Param("studentId") Long studentId,
                                        @Param("limit") int limit);
}
