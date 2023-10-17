package cn.little.prince.oj.utils;

import cn.little.prince.oj.common.ErrorCode;
import cn.little.prince.oj.exception.BusinessException;

/**
 * 抛异常工具类
 *
 * @author 349807102
 */
public class ThrowUtils {
    private ThrowUtils() {
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition        条件
     * @param runtimeException 运行时异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 错误码对象
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 错误码对象
     * @param message   消息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
