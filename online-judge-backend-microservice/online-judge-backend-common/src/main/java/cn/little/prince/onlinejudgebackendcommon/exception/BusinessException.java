package cn.little.prince.onlinejudgebackendcommon.exception;


import cn.little.prince.onlinejudgebackendcommon.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author 349807102
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 6875319753673169236L;

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
