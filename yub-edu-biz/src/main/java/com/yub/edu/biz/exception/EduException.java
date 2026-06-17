package com.yub.edu.biz.exception;

import com.yub.common.exception.BaseException;

/**
 * 教育模块异常
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 教育模块业务异常
 * @Version: 1.0.0
 */
public class EduException extends BaseException {

    /**
     * 创建教育模块异常
     *
     * @param errorCode 错误码
     */
    public EduException(EduErrorCode errorCode) {
        super(errorCode);
    }
}
