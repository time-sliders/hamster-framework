package com.noob.storage.exception;

/**
 * 业务异常
 * <p>该类异常用于体现程序可以预见的异常，是由于业务场景中不可避免的异常情况
 * 导致的，如无法建立连接、数据库操作失败等不可避免的原因导致，程序对于该
 * 类异常必须进行容错处理
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;

    private String errorCode;

    private String errorMessage;

    public BusinessException() {
    }

    public BusinessException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    public BusinessException(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
