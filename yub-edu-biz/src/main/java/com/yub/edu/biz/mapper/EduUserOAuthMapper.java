package com.yub.edu.biz.mapper;

import com.yub.edu.biz.entity.EduUserOAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户第三方账号绑定Mapper
 *
 * @Author: bing.yu
 * @CreateTime: 2026-07-06
 * @Description: 用户第三方账号绑定数据访问
 * @Version: 1.0.0
 */
@Mapper
public interface EduUserOAuthMapper {

    /**
     * 根据用户ID和平台查询绑定记录
     */
    EduUserOAuth selectByUserIdAndPlatform(@Param("userId") Long userId, @Param("platform") String platform);

    /**
     * 根据平台和openId查询绑定记录
     */
    EduUserOAuth selectByPlatformAndOpenId(@Param("platform") String platform, @Param("openId") String openId);

    /**
     * 新增绑定记录
     */
    int insert(EduUserOAuth oauth);

    /**
     * 更新绑定记录
     */
    int updateById(EduUserOAuth oauth);

    /**
     * 逻辑删除绑定记录
     */
    int deleteByUserIdAndPlatform(@Param("userId") Long userId, @Param("platform") String platform);
}
