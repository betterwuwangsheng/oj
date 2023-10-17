package cn.little.prince.onlinejudgebackendcommon.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @author 349807102
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 8350555285086486039L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 数据
     */
    private T data;

    /**
     * 简要信息
     */
    private String message;

    /**
     * 全参数构造
     *
     * @param code    状态码
     * @param data    数据
     * @param message 简要信息
     */
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 只需要传递状态码和数据的构造函数
     *
     * @param code 状态码
     * @param data 数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    /**
     * 返回错误信息
     *
     * @param errorCode 错误码对象
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
