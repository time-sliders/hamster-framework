package com.noob.storage.exception;

/**
 * 业务异常
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
