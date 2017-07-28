package com.noob.storage.thread.sync;

/**
 * 业务处理结果
 *
 * @author luyun
 * @version 2017.7.31 TB to HQB
 * @since 2017.07.27
 */
public class BizTaskResult {

    /**
     * @see BizStatusConst
     */
    private Integer status;

    /**
     * 处理消息
     */
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
