package com.noob.storage.exception;

/**
 * 程序异常
 * <p>在这里继承RuntimeException，用于将jdk中抛出的无法预见的异常捕获并转换抛出.
 * 该类异常一般出现在开发阶段，生产上线之后基本不会出现，程序不需要对此类异常做特殊处理
 */
public class ProcessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProcessException(String message) {
        super(message);
    }

    public ProcessException(Throwable e) {
        super(e);
    }

    public ProcessException() {
        super();
    }

    public ProcessException(String message, Throwable cause) {
        super(message, cause);
    }

}
