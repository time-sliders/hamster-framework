package com.noob.storage.pattern.chain;

import java.util.HashMap;

/**
 * 上下文
 *
 * @author luyun
 * @since app5.9
 */
public class ChainContext extends HashMap {
    private static final long serialVersionUID = 5262605597686605033L;

    private volatile int state = PROCESSING;//处理状态
    public static final int PROCESSING = 0;//处理中
    public static final int COMPLETED = 1;//处理完成
    public static final int EXCEPTION = 2;//处理异常
    private Throwable e;//处理异常

    /**
     * 当前上下文是否还需要继续被处理
     */
    public boolean isInProcessing() {
        return state == PROCESSING;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void saveException(Throwable throwable) {
        this.e = throwable;
        setState(EXCEPTION);
    }

    public Throwable getException() {
        return e;
    }

}
