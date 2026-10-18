package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduVideoAsset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EduVideoAssetMapper {
    int insert(EduVideoAsset asset);

    EduVideoAsset selectById(@Param("id") Long id);

    EduVideoAsset selectBySourceKey(@Param("sourceKey") String sourceKey);

    int updateTask(EduVideoAsset asset);

    int markDeleted(@Param("id") Long id);
}
