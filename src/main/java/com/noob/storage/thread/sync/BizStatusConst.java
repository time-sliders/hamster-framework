package com.noob.storage.thread.sync;

/**
 * 业务状态常量
 *
 * @author luyun
 * @version 2017.7.31 TB to HQB
 * @since 2017.07.27
 */
public interface BizStatusConst {

    /**
     * 初始化
     */
    Integer INIT = 0;

    /**
     * 成功
     */
    Integer SUCCESS = 1;

    /**
     * 处理中
     */
    Integer PROCESSING = 2;

    /**
     * 失败
     */
    Integer FAIL = -1;
}
