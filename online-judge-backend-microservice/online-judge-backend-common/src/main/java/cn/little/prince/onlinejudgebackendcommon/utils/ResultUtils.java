package cn.little.prince.onlinejudgebackendcommon.utils;


import cn.little.prince.onlinejudgebackendcommon.common.BaseResponse;
import cn.little.prince.onlinejudgebackendcommon.common.ErrorCode;

/**
 * 返回工具类
 *
 * @author 349807102
 */
public class ResultUtils {
    private ResultUtils() {
    }

    /**
     * 成功返回
     *
     * @param data 数据
     * @param <T>  类型
     * @return 成功信息
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "OK");
    }

    /**
     * 失败返回
     *
     * @param errorCode 错误码对象
     * @return 错误信息
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 失败返回
     *
     * @param code    失败状态码
     * @param message 简要信息
     * @return 错误信息
     */
    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败返回
     *
     * @param errorCode 错误码对象
     * @return 错误信息
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
